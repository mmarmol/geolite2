/**
 * Copyright 2016 gromit.it
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gromit.geolite2.geonames;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.gromit.geolite2.LoaderListener;
import io.gromit.geolite2.model.Country;

/**
 * The Class CountryFinder.
 */
public class CountryFinder {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(CountryFinder.class);

	/** The Constant COUNTRY_FAIL_SAFE_URL. */
	public static final String COUNTRY_FAIL_SAFE_URL = "io.gromit.geolite2.country.fail.safe.url";
	
	/** The countries url. */
	private String countriesUrl = "https://raw.githubusercontent.com/mmarmol/geonames/master/export/countries.zip";

	/** The crc. */
	private Long crc = -2l;
	
	/** The geoname map. */
	private Map<Integer, Country> geonameMap = new HashMap<>();
	
	/** The iso map. */
	private Map<String, Country> isoMap = new HashMap<>();
	
	/** The loader listener. */
	private LoaderListener loaderListener = LoaderListener.DEFAULT;
	
	/**
	 * Loader listener.
	 *
	 * @param loaderListener the loader listener
	 * @return the country finder
	 */
	public CountryFinder loaderListener(LoaderListener loaderListener){
		this.loaderListener = loaderListener;
		return this;
	}
	
	/**
	 * Countries url.
	 *
	 * @param countriesUrl the countries url
	 * @return the country finder
	 */
	public CountryFinder countriesUrl(String countriesUrl){
		this.countriesUrl = countriesUrl;
		return this;
	}
	
	/**
	 * Find.
	 *
	 * @param geonameId the geoname id
	 * @return the country
	 */
	public Country find(Integer geonameId){
		if(geonameId==null){
			return null;
		}
		return geonameMap.get(geonameId);
	}
	
	/**
	 * Find.
	 *
	 * @param iso the iso
	 * @return the country
	 */
	public Country find(String iso){
		if(StringUtils.isBlank(iso)){
			return null;
		}
		return isoMap.get(iso.trim().toUpperCase());
	}
	
	/**
	 * Read countries.
	 *
	 * @return the country finder
	 */
	public CountryFinder readCountries(){
		try{
			readCountries(countriesUrl);
			loaderListener.success(countriesUrl);
		}catch(Exception e){
			loaderListener.failure(countriesUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(COUNTRY_FAIL_SAFE_URL))){
				readCountries(System.getProperty(COUNTRY_FAIL_SAFE_URL));
			}
		}
		return this;
	}
	
	/**
	 * Read countries.
	 *
	 * @param countriesLocationUrl the countries location url
	 * @return the country finder
	 */
	private CountryFinder readCountries(String countriesLocationUrl){
		ZipInputStream zipis = null;
		try {
			logger.info("READING countries database at url {}", countriesLocationUrl);
			zipis = new ZipInputStream(new URL(countriesLocationUrl).openStream(), Charset.forName("UTF-8"));
			ZipEntry zipEntry = zipis.getNextEntry();
			logger.info("reading "+zipEntry.getName());
			if(crc==zipEntry.getCrc()){
				logger.info("skipp, same CRC");
				return this;
			}
		
			CsvParserSettings settings = new CsvParserSettings();
			settings.setSkipEmptyLines(true);
			settings.trimValues(true);
			CsvFormat format = new CsvFormat();
			format.setDelimiter('\t');
			format.setLineSeparator("\n");
			format.setCharToEscapeQuoteEscaping('\0');
			format.setQuote('\0');
			settings.setFormat(format);
			CsvParser parser = new CsvParser(settings);
			
			List<String[]> lines = parser.parseAll(new InputStreamReader(zipis, "UTF-8"));
			
			for(String[] entry : lines){
				Country country = new Country();
				country.setIso(entry[0]);
				country.setIso3(entry[1]);
				country.setName(entry[2]);
				country.setCapital(entry[3]);
				country.setContinent(entry[4]);
				country.setCurrencyCode(entry[5]);
				country.setCurrencyName(entry[6]);
				country.setPhone(entry[7]);
				country.setLanguage(StringUtils.substringBefore(entry[8], ","));
				country.setGeonameId(NumberUtils.toInt(entry[9]));
				geonameMap.put(country.getGeonameId(), country);
				isoMap.put(country.getIso(), country);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try{zipis.close();}catch(Exception e){};
		}
		logger.info("loaded "+geonameMap.size()+" countries");
		return this;
	}
	
}
