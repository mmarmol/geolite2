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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.gromit.geolite2.LoaderListener;
import io.gromit.geolite2.model.TimeZone;

/**
 * The Class TimeZoneFinder.
 */
public class TimeZoneFinder {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(TimeZoneFinder.class);
	
	/** The Constant TIMEZONES_FAIL_SAFE_URL. */
	public static final String TIMEZONES_FAIL_SAFE_URL = "io.gromit.geolite2.timezones.fail.safe.url";
	
	/** The countries url. */
	private String timeZonesUrl = "https://raw.githubusercontent.com/mmarmol/geonames/master/data/timeZones.txt";

	/** The time zones m d5. */
	private String timeZonesMD5 = null;
	
	/** The id map. */
	private Map<String, TimeZone> idMap = new HashMap<>();
	
	/** The loader listener. */
	private LoaderListener loaderListener = LoaderListener.DEFAULT;
	
	/**
	 * Loader listener.
	 *
	 * @param loaderListener the loader listener
	 * @return the time zone finder
	 */
	public TimeZoneFinder loaderListener(LoaderListener loaderListener){
		this.loaderListener = loaderListener;
		return this;
	}
	
	/**
	 * Time zones url.
	 *
	 * @param timeZonesUrl the time zones url
	 * @return the time zone finder
	 */
	public TimeZoneFinder timeZonesUrl(String timeZonesUrl){
		this.timeZonesUrl = timeZonesUrl;
		return this;
	}
	
	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the time zone
	 */
	public TimeZone find(String id){
		if(StringUtils.isBlank(id)){
			return null;
		}
		return idMap.get(id.trim().toUpperCase());
	}
	
	/**
	 * Read time zones.
	 *
	 * @return the time zone finder
	 */
	public TimeZoneFinder readTimeZones(){
		try{
			readTimeZones(timeZonesUrl);
			loaderListener.success(timeZonesUrl);
		}catch(Exception e){
			loaderListener.failure(timeZonesUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(TIMEZONES_FAIL_SAFE_URL))){
				readTimeZones(System.getProperty(TIMEZONES_FAIL_SAFE_URL));
			}
		}
		return this;
	}
	
	/**
	 * Read time zones.
	 *
	 * @param timeZonesLocationUrl the time zones location url
	 * @return the time zone finder
	 */
	private TimeZoneFinder readTimeZones(String timeZonesLocationUrl){
		byte[] bytes = null;
		String newMD5 = null;
		try {
			bytes = IOUtils.toByteArray(new URL(timeZonesLocationUrl).openStream());
			newMD5 = new String(MessageDigest.getInstance("MD5").digest(bytes));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(newMD5.equals(timeZonesMD5)){
			logger.info("same MD5 content for both files, do not load it");
			return this;
		}
		timeZonesMD5 = newMD5;
		CsvParserSettings settings = new CsvParserSettings();
		settings.setSkipEmptyLines(true);
		settings.setNumberOfRowsToSkip(1);
		settings.trimValues(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter('\t');
		format.setLineSeparator("\n");
		format.setCharToEscapeQuoteEscaping('\0');
		format.setQuote('\0');
		settings.setFormat(format);
		CsvParser parser = new CsvParser(settings);
		
		Map<String, TimeZone> idMapRead = new HashMap<>();
		List<String[]> lines = parser.parseAll(new ByteArrayInputStream(bytes), "UTF-8");
		
		for(String[] entry : lines){
			TimeZone timeZone = new TimeZone();
			timeZone.setCountryIso(entry[0]);
			timeZone.setId(entry[1]);
			timeZone.setJanOffset(NumberUtils.toFloat(entry[2]));
			timeZone.setJulOffset(NumberUtils.toFloat(entry[3]));
			timeZone.setRawOffset(NumberUtils.toFloat(entry[4]));
			idMapRead.put(timeZone.getId().trim().toUpperCase(), timeZone);
		}
		this.idMap = idMapRead;
		logger.info("loaded "+idMapRead.size()+" time zones");
		return this;
	}
	
}
