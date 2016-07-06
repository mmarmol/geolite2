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
import io.gromit.geolite2.model.Subdivision;

/**
 * The Class SubdivisionFinder.
 */
public class SubdivisionFinder {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SubdivisionFinder.class);

	/** The Constant ADMIN1_FAIL_SAFE_URL. */
	public static final String ADMIN1_FAIL_SAFE_URL = "io.gromit.geolite2.admin1.fail.safe.url";

	/** The Constant ADMIN2_FAIL_SAFE_URL. */
	public static final String ADMIN2_FAIL_SAFE_URL = "io.gromit.geolite2.admin2.fail.safe.url";

	/** The subdivision one url. */
	private String subdivisionOneUrl = "https://raw.githubusercontent.com/mmarmol/geonames/master/export/subdivisions1.zip";

	/** The subdivision one m d5. */
	private Long crc1 = -2l;

	/** The subdivision two url. */
	private String subdivisionTwoUrl = "https://raw.githubusercontent.com/mmarmol/geonames/master/export/subdivisions2.zip";

	/** The subdivision two m d5. */
	private Long crc2 = -2l;

	/** The id one map. */
	private Map<String, Subdivision> idOneMap = new HashMap<>();

	/** The id tow map. */
	private Map<String, Subdivision> idTowMap = new HashMap<>();
	
	/** The geoname id map. */
	private Map<Integer, Subdivision> geonameIdMap = new HashMap<>();
	
	/** The loader listener. */
	private LoaderListener loaderListener = LoaderListener.DEFAULT;
	
	/**
	 * Loader listener.
	 *
	 * @param loaderListener the loader listener
	 * @return the subdivision finder
	 */
	public SubdivisionFinder loaderListener(LoaderListener loaderListener){
		this.loaderListener = loaderListener;
		return this;
	}
	
	/**
	 * Subdivision one url.
	 *
	 * @param subdivisionOneUrl the subdivision one url
	 * @return the subdivision finder
	 */
	public SubdivisionFinder subdivisionOneUrl(String subdivisionOneUrl){
		this.subdivisionOneUrl = subdivisionOneUrl;
		return this;
	}
	
	/**
	 * Subdivision two url.
	 *
	 * @param subdivisionTwoUrl the subdivision two url
	 * @return the subdivision finder
	 */
	public SubdivisionFinder subdivisionTwoUrl(String subdivisionTwoUrl){
		this.subdivisionTwoUrl = subdivisionTwoUrl;
		return this;
	}
	
	/**
	 * Find.
	 *
	 * @param country the country
	 * @param one the one
	 * @return the subdivision
	 */
	public Subdivision find(String country, String one) {
		if (StringUtils.isBlank(country) || StringUtils.isBlank(one)) {
			return null;
		}
		return idOneMap.get(country.trim().toUpperCase() + "." + one.trim());
	}

	/**
	 * Find.
	 *
	 * @param country the country
	 * @param one the one
	 * @param two the two
	 * @return the subdivision
	 */
	public Subdivision find(String country, String one, String two) {
		if (StringUtils.isBlank(country) || StringUtils.isBlank(one) || StringUtils.isBlank(two)) {
			return null;
		}
		return idTowMap.get(country.trim().toUpperCase() + "." + one.trim() + "." + two.trim());
	}

	/**
	 * Find.
	 *
	 * @param geonameId the geoname id
	 * @return the subdivision
	 */
	public Subdivision find(Integer geonameId){
		if(geonameId==null){
			return null;
		}
		return geonameIdMap.get(geonameId);
	}
	
	/**
	 * Read level one.
	 *
	 * @return the subdivision finder
	 */
	public SubdivisionFinder readLevelOne() {
		try{
			readLevelOne(subdivisionOneUrl);
			loaderListener.success(subdivisionOneUrl);
		}catch(Exception e){
			loaderListener.failure(subdivisionOneUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(ADMIN1_FAIL_SAFE_URL))){
				readLevelOne(System.getProperty(ADMIN1_FAIL_SAFE_URL));
			}
		}
		return this;
	}
	
	/**
	 * Read countries.
	 *
	 * @param subdivisionOneLocationUrl the subdivision one location url
	 * @return the time zone finder
	 */
	public SubdivisionFinder readLevelOne(String subdivisionOneLocationUrl) {
		ZipInputStream zipis = null;
		try {
			zipis = new ZipInputStream(new URL(subdivisionOneLocationUrl).openStream(), Charset.forName("UTF-8"));
			ZipEntry zipEntry = zipis.getNextEntry();
			logger.info("reading "+zipEntry.getName());
			if(crc1==zipEntry.getCrc()){
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
	
			for (String[] entry : lines) {
				Subdivision subdivision = new Subdivision();
				subdivision.setId(entry[0]);
				subdivision.setName(entry[1]);
				subdivision.setGeonameId(NumberUtils.toInt(entry[2]));
				idOneMap.put(subdivision.getId(), subdivision);
				geonameIdMap.put(subdivision.getGeonameId(), subdivision);
			}
			logger.info("loaded " + lines.size() + " subdivisions level 1");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			try{zipis.close();}catch(Exception e){};
		}
		return this;
	}
	
	/**
	 * Read level one.
	 *
	 * @return the subdivision finder
	 */
	public SubdivisionFinder readLevelTwo() {
		try{
			readLevelTwo(subdivisionTwoUrl);
			loaderListener.success(subdivisionTwoUrl);
		}catch(Exception e){
			loaderListener.failure(subdivisionTwoUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(ADMIN2_FAIL_SAFE_URL))){
				readLevelTwo(System.getProperty(ADMIN2_FAIL_SAFE_URL));
			}
		}
		return this;
	}
	
	/**
	 * Read level two.
	 *
	 * @param subdivisionTwoLocationUrl the subdivision two location url
	 * @return the subdivision finder
	 */
	public SubdivisionFinder readLevelTwo(String subdivisionTwoLocationUrl) {
		ZipInputStream zipis = null;
		try {
			zipis = new ZipInputStream(new URL(subdivisionTwoLocationUrl).openStream(), Charset.forName("UTF-8"));
			ZipEntry zipEntry = zipis.getNextEntry();
			logger.info("reading "+zipEntry.getName());
			if(crc2==zipEntry.getCrc()){
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
			
			for (String[] entry : lines) {
				Subdivision subdivision = new Subdivision();
				subdivision.setId(entry[0]);
				subdivision.setName(entry[1]);
				subdivision.setGeonameId(NumberUtils.toInt(entry[2]));
				idTowMap.put(subdivision.getId(), subdivision);
				geonameIdMap.put(subdivision.getGeonameId(), subdivision);
			}
			logger.info("loaded " + lines.size() + " subdivisions level 2");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			try{zipis.close();}catch(Exception e){};
		}
		return this;
	}
}
