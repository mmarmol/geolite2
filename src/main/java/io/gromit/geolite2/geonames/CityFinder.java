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
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.gromit.geolite2.LoaderListener;
import io.gromit.geolite2.model.City;
import rx.Observable;

/**
 * The Class CityFinder.
 */
public class CityFinder {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(CityFinder.class);
	
	/** The Constant CITY_FAIL_SAFE_URL. */
	public static final String CITY_FAIL_SAFE_URL = "io.gromit.geolite2.city.fail.safe.url";
	
	/** The cities url. */
	private String citiesUrl = "https://raw.githubusercontent.com/mmarmol/geonames/master/data/cities1000.zip";
	
	/** The crc. */
	private Long crc = -2l;
	
	/** The rtree. */
	private RTree<City,Geometry> rtree = RTree.create();
	
	/** The geoname map. */
	private Map<Integer, City> geonameMap = new HashMap<>();
	
	/** The loader listener. */
	private LoaderListener loaderListener = LoaderListener.DEFAULT;
	
	/**
	 * Loader listener.
	 *
	 * @param loaderListener the loader listener
	 * @return the city finder
	 */
	public CityFinder loaderListener(LoaderListener loaderListener){
		this.loaderListener = loaderListener;
		return this;
	}
	
	/**
	 * Cities url.
	 *
	 * @param citiesUrl the cities url
	 * @return the city finder
	 */
	public CityFinder citiesUrl(String citiesUrl){
		this.citiesUrl = citiesUrl;
		return this;
	}
	
	/**
	 * Find.
	 *
	 * @param geonameId the geoname id
	 * @return the city
	 */
	public City find(Integer geonameId){
		if(geonameId==null){
			return null;
		}
		return geonameMap.get(geonameId);
	}
	
	/**
	 * Find.
	 *
	 * @param longitude the longitude
	 * @param latitude the latitude
	 * @return the city
	 */
	public City find(Double longitude, Double latitude){
		if(longitude == null || latitude == null){
			return null;
		}
		Observable<Entry<City, Geometry>> result =  this.rtree.nearest(Geometries.pointGeographic(longitude, latitude), 10000, 1);
		if(result==null){
			return null;
		}
		try{
			return result.toBlocking().single().value();
		}catch(NoSuchElementException e){
			return null;
		}
	}
	
	/**
	 * Read cities.
	 *
	 * @return the city finder
	 */
	public CityFinder readCities(){
		try{
			readCities(citiesUrl);
			loaderListener.success(citiesUrl);
		}catch(Exception e){
			loaderListener.failure(citiesUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(CITY_FAIL_SAFE_URL))){
				readCities(System.getProperty(CITY_FAIL_SAFE_URL));
			}
		}
		return this;
	}
	
	/**
	 * Read cities.
	 *
	 * @param citiesLocationUrl the cities location url
	 * @return the city finder
	 */
	private CityFinder readCities(String citiesLocationUrl){
		ZipInputStream zipis;
		CsvParserSettings settings = new CsvParserSettings();
		settings.selectIndexes(0,2,4,5,8,10,11,14,17);
		settings.setSkipEmptyLines(true);
		settings.trimValues(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter('\t');
		format.setLineSeparator("\n");
		format.setComment('\0');
		format.setCharToEscapeQuoteEscaping('\0');
		format.setQuote('\0');
		settings.setFormat(format);
		CsvParser parser = new CsvParser(settings);
		try {
			zipis = new ZipInputStream(new URL(citiesLocationUrl).openStream(), Charset.forName("UTF-8"));
			ZipEntry zipEntry = zipis.getNextEntry();
			logger.info("reading "+zipEntry.getName());
			if(crc==zipEntry.getCrc()){
				logger.info("skipp, same CRC");
				return this;
			}
			RTree<City,Geometry> rtreeRead = RTree.create();
			List<String[]> lines = parser.parseAll(new InputStreamReader(zipis, "UTF-8"));
			for(String[] entry : lines){
				City city = new City();
				city.setGeonameId(Integer.decode(entry[0]));
				city.setName(entry[1]);
				try{
					try{
						city.setLatitude(Double.valueOf(entry[2]));
						city.setLongitude(Double.valueOf(entry[3]));
						rtreeRead = rtreeRead.add(city, Geometries.pointGeographic(city.getLongitude(), city.getLatitude()));
					}catch(NumberFormatException | NullPointerException e){}
					city.setCountryIsoCode(entry[4]);
					city.setSubdivisionOne(entry[5]);
					city.setSubdivisionTwo(entry[6]);
					try{
						city.setPopulation(new BigInteger(entry[7]));
					}catch(NumberFormatException | NullPointerException e){}
					city.setTimeZone(entry[8]);
				}catch(ArrayIndexOutOfBoundsException e){}
				geonameMap.put(city.getGeonameId(), city);	
			}
			this.rtree = rtreeRead;
			logger.info("loaded "+geonameMap.size()+" cities");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return this;
	}
	
}
