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

import io.gromit.geolite2.model.TimeZone;

/**
 * The Class TimeZoneFinder.
 */
public class TimeZoneFinder {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(TimeZoneFinder.class);
	
	/** The countries url. */
	private String timeZonesUrl = "http://download.geonames.org/export/dump/timeZones.txt";

	/** The time zones m d5. */
	private String timeZonesMD5 = null;
	
	/** The id map. */
	private Map<String, TimeZone> idMap = new HashMap<>();
	
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
		byte[] bytes = null;
		String newMD5 = null;
		try {
			bytes = IOUtils.toByteArray(new URL(timeZonesUrl).openStream());
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
