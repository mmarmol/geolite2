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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.gromit.geolite2.LoaderListener;
import io.gromit.geolite2.model.TimeZone;
import io.gromit.geolite2.utils.ClosableZipInputStream;
import io.gromit.geolite2.model.Offset;

/**
 * The Class TimeZoneFinder.
 */
public class TimeZoneFinder {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(TimeZoneFinder.class);
	
	private static final String TIMEZONE_FILE_NAME = "timezone.csv";
	
	private static final String ZONE_FILE_NAME = "zone.csv";
	
	private static final Double OFFSET_UNIT = (60d*60d);
	
	/** The Constant TIMEZONES_FAIL_SAFE_URL. */
	public static final String TIMEZONES_FAIL_SAFE_URL = "io.gromit.geolite2.timezones.fail.safe.url";
	
	/** The countries url. */
	private String timeZonesUrl = "https://timezonedb.com/files/timezonedb.csv.zip";

	/** The time zones m d5. */
	private Long crcZone = -2l;
	
	private Long crcTimezones = -2l;
	
	/** The id map. */
	private Map<String, TimeZone> idMap = new HashMap<>();
	
	private Map<Integer, TreeSet<Offset>> offsetMap = new HashMap<>();
	
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
		TimeZone timeZone = idMap.get(id.trim().toUpperCase());
		Long currentTimeInSeconds = System.currentTimeMillis()/1000;
		Offset ceiling = offsetMap.get(timeZone.getId()).floor(new Offset(currentTimeInSeconds));
		Offset other = offsetMap.get(timeZone.getId()).higher(new Offset(currentTimeInSeconds));
		if(other==null){
			other = offsetMap.get(timeZone.getId()).lower(new Offset(ceiling.getTimeStart()));
		}
		timeZone.setCurrentOffset(ceiling.getGmtOffset()/OFFSET_UNIT);
		timeZone.setChangedAt(ceiling.getTimeStart());
		if(ceiling.getDst()){
			timeZone.setDtsOffset(ceiling.getGmtOffset()/OFFSET_UNIT);
			if(other!=null){
				timeZone.setUtcOffset(other.getGmtOffset()/OFFSET_UNIT);
			}
		}else{
			timeZone.setDtsOffset(other.getGmtOffset()/OFFSET_UNIT);
			if(other!=null){
				timeZone.setUtcOffset(ceiling.getGmtOffset()/OFFSET_UNIT);
			}
		}
		return timeZone;
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
		ClosableZipInputStream zipis = null;
		try {
			logger.info("READING timezones database at url {}", timeZonesLocationUrl);
			zipis = new ClosableZipInputStream(new URL(timeZonesLocationUrl).openStream(), Charset.forName("UTF-8"));
			ZipEntry zipEntry = null;
			while((zipEntry=zipis.getNextEntry())!=null){
				logger.info("reading "+zipEntry.getName());
				if(zipEntry.getName().equalsIgnoreCase(TIMEZONE_FILE_NAME)){
					if(crcTimezones==zipEntry.getCrc()){
						logger.info("skipp, same CRC");
						return this;
					}else{
						loadOffsets(zipis);
						zipis.closeEntry();
					}
				}else if(zipEntry.getName().equalsIgnoreCase(ZONE_FILE_NAME)){
					if(crcZone==zipEntry.getCrc()){
						logger.info("skipp, same CRC");
						return this;
					}else{
						loadZones(zipis);
						zipis.closeEntry();
					}
				}else{
					logger.info("skip entry: "+zipEntry.getName());
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			try{zipis.manualClose();}catch(Exception e){};
		}
		return this;
	}
	
	private void loadOffsets(ZipInputStream zipis) throws UnsupportedEncodingException{
		CsvParserSettings settings = new CsvParserSettings();
		settings.setSkipEmptyLines(true);
		settings.setNumberOfRowsToSkip(1);
		settings.trimValues(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter(',');
		format.setLineSeparator("\n");
		format.setCharToEscapeQuoteEscaping('\0');
		format.setQuote('"');
		settings.setFormat(format);
		CsvParser parser = new CsvParser(settings);
		
		Map<Integer, TreeSet<Offset>> offsetMapRead = new HashMap<>();
		List<String[]> lines = parser.parseAll(new InputStreamReader(zipis, "UTF-8"));
		
		for(String[] entry : lines){
			Offset offset = new Offset(Long.parseLong(entry[2]));
			offset.setTimezoneId(Integer.parseInt(entry[0]));
			offset.setGmtOffset(Integer.parseInt(entry[3]));
			offset.setDst(BooleanUtils.toBooleanObject(Integer.parseInt(entry[4])));
			if(!offsetMapRead.containsKey(offset.getTimezoneId())){
				offsetMapRead.put(offset.getTimezoneId(), new TreeSet<>());
			}
			offsetMapRead.get(offset.getTimezoneId()).add(offset);
		}
		this.offsetMap = offsetMapRead;
		logger.info("loaded "+offsetMapRead.size()+" time zones offsets");
	}
	
	private void loadZones(ZipInputStream zipis) throws UnsupportedEncodingException{
		CsvParserSettings settings = new CsvParserSettings();
		settings.setSkipEmptyLines(true);
		settings.setNumberOfRowsToSkip(1);
		settings.trimValues(true);
		CsvFormat format = new CsvFormat();
		format.setDelimiter(',');
		format.setLineSeparator("\n");
		format.setCharToEscapeQuoteEscaping('\0');
		format.setQuote('"');
		settings.setFormat(format);
		CsvParser parser = new CsvParser(settings);
		
		Map<String, TimeZone> idMapRead = new HashMap<>();
		List<String[]> lines = parser.parseAll(new InputStreamReader(zipis, "UTF-8"));
		
		for(String[] entry : lines){
			TimeZone timeZone = new TimeZone();
			timeZone.setId(Integer.parseInt(entry[0]));
			timeZone.setCountryIso(entry[1]);
			timeZone.setName(entry[2]);
			idMapRead.put(timeZone.getName().trim().toUpperCase(), timeZone);
		}
		this.idMap = idMapRead;
		logger.info("loaded "+idMapRead.size()+" time zones");
	}
	
}
