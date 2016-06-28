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
package io.gromit.geolite2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.db.NoCache;
import com.maxmind.db.NodeCache;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import io.gromit.geolite2.geonames.CityFinder;
import io.gromit.geolite2.geonames.ContinentFinder;
import io.gromit.geolite2.geonames.CountryFinder;
import io.gromit.geolite2.geonames.SubdivisionFinder;
import io.gromit.geolite2.geonames.TimeZoneFinder;
import io.gromit.geolite2.model.City;
import io.gromit.geolite2.model.Continent;
import io.gromit.geolite2.model.Country;
import io.gromit.geolite2.model.Subdivision;
import io.gromit.geolite2.model.TimeZone;

/**
 * The Class ScheduledDatabaseReader.
 */
public class GeoLocation {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(GeoLocation.class);

	/** The Constant GEOLITE_FAIL_SAFE_URL. */
	public static final String GEOLITE_FAIL_SAFE_URL = "io.gromit.geolite2.fail.safe.url";
	
	/** The scheduled executor service. */
	private ScheduledExecutorService scheduledExecutorService;

	/** The reader. */
	private DatabaseReader databaseReader;

	/** The local m d5 checksum. */
	private String localMD5Checksum;

	/** The md5 checksum url. */
	private String md5ChecksumUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.md5";

	/** The database url. */
	private String databaseUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz";
	
	/** The cache. */
	private NodeCache cache = NoCache.getInstance();
	
	/** The city finder. */
	private CityFinder cityFinder = new CityFinder();
	
	/** The continent finder. */
	private ContinentFinder continentFinder = new ContinentFinder();
	
	/** The country finder. */
	private CountryFinder countryFinder = new CountryFinder();
	
	/** The subdivision finder. */
	private SubdivisionFinder subdivisionFinder = new SubdivisionFinder();
	
	/** The time zone finder. */
	private TimeZoneFinder timeZoneFinder = new TimeZoneFinder();
	
	/** The loader listener. */
	private LoaderListener loaderListener = LoaderListener.DEFAULT;

	/**
	 * Instantiates a new scheduled database reader.
	 */
	public GeoLocation() {
	}

	/**
	 * Cities url.
	 *
	 * @param citiesUrl the cities url
	 * @return the geo location
	 */
	public GeoLocation citiesUrl(String citiesUrl){
		this.cityFinder.citiesUrl(citiesUrl);
		return this;
	}
	
	/**
	 * Countries url.
	 *
	 * @param countriesUrl the countries url
	 * @return the geo location
	 */
	public GeoLocation countriesUrl(String countriesUrl){
		this.countryFinder.countriesUrl(countriesUrl);
		return this;
	}
	
	/**
	 * Time zones url.
	 *
	 * @param timeZonesUrl the time zones url
	 * @return the geo location
	 */
	public GeoLocation timeZonesUrl(String timeZonesUrl){
		this.timeZoneFinder.timeZonesUrl(timeZonesUrl);
		return this;
	}
	
	/**
	 * Subdivision one url.
	 *
	 * @param subdivisionOneUrl the subdivision one url
	 * @return the geo location
	 */
	public GeoLocation subdivisionOneUrl(String subdivisionOneUrl){
		this.subdivisionFinder.subdivisionOneUrl(subdivisionOneUrl);
		return this;
	}
	
	/**
	 * Subdivision two url.
	 *
	 * @param subdivisionTwoUrl the subdivision two url
	 * @return the geo location
	 */
	public GeoLocation subdivisionTwoUrl(String subdivisionTwoUrl){
		this.subdivisionFinder.subdivisionTwoUrl(subdivisionTwoUrl);
		return this;
	}
	
	
	/**
	 * Md5 checksum url.
	 *
	 * @param md5ChecksumUrl
	 *            the md5 checksum url
	 * @return the scheduled database reader
	 */
	public GeoLocation md5ChecksumUrl(String md5ChecksumUrl) {
		this.md5ChecksumUrl = md5ChecksumUrl;
		return this;
	}

	/**
	 * Database url.
	 *
	 * @param databaseUrl
	 *            the database url
	 * @return the scheduled database reader
	 */
	public GeoLocation databaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
		return this;
	}

	/**
	 * Cache.
	 *
	 * @param cache the cache
	 * @return the scheduled database readers
	 */
	public GeoLocation cache(NodeCache cache) {
		this.cache = cache;
		return this;
	}
	
	/**
	 * Loader listener.
	 *
	 * @param loaderListener the loader listener
	 * @return the geo location
	 */
	public GeoLocation loaderListener(LoaderListener loaderListener){
		this.loaderListener = loaderListener;
		this.cityFinder.loaderListener(loaderListener);
		this.countryFinder.loaderListener(loaderListener);
		this.subdivisionFinder.loaderListener(loaderListener);
		this.timeZoneFinder.loaderListener(loaderListener);
		return this;
	}

	public Map<String, Object> location(Double latitude, Double longitude){
		City city = null;
		Country country = null;
		Continent continent = null;
		Subdivision one = null;
		Subdivision two = null;
		Map<String, Object> data = new LinkedHashMap<>();
		Map<String, Object> location = new LinkedHashMap<>();
		data.put("latitude", latitude);
		data.put("longitude", longitude);
		city = this.cityFinder.find(longitude, latitude);
		if(city!=null){
			location.put("geoNameId", city.getGeonameId());
			location.put("name", city.getName());
			location.put("population", city.getPopulation());
			TimeZone timeZone = timeZoneFinder.find(city.getTimeZone());
			if(timeZone!=null){
				data.put("timeZone", timeZone);
			}
			one = subdivisionFinder.find(city.getCountryIsoCode(), city.getSubdivisionOne());
			two = subdivisionFinder.find(city.getCountryIsoCode(), city.getSubdivisionOne(), city.getSubdivisionTwo());
			country=countryFinder.find(city.getCountryIsoCode());
			List<Map<String, Object>> subdivisions = new ArrayList<>();
			if(two!=null){
				Map<String, Object> sub = new LinkedHashMap<>();
				sub.put("name", two.getName());
				sub.put("geonNameId", two.getGeonameId());
				subdivisions.add(sub);
			}
			if(one!=null){
				Map<String, Object> sub = new LinkedHashMap<>();
				sub.put("name", one.getName());
				sub.put("geonNameId", one.getGeonameId());
				subdivisions.add(sub);
			}
			if(subdivisions!=null && subdivisions.size()>0){
				data.put("subdivisions", subdivisions);
			}
			Map<String, Object> countryMap = new LinkedHashMap<>();
			if(country!=null){
				countryMap.put("area", country.getArea());
				countryMap.put("capital", country.getCapital());
				countryMap.put("currencyCode", country.getCurrencyCode());
				countryMap.put("currencyName", country.getCurrencyName());
				countryMap.put("language", country.getLanguage());
				countryMap.put("name", country.getName());
				countryMap.put("phone", country.getPhone());
				countryMap.put("population", country.getPopulation());
				countryMap.put("iso", country.getIso());
				countryMap.put("geoNameId", country.getGeonameId());
				data.put("country", countryMap);
				continent = continentFinder.find(country.getContinent());
				if(continent!=null){
					Map<String, Object> continentMap = new LinkedHashMap<>();
					continentMap.put("geoNameId", continent.getGeonameId());
					continentMap.put("iso", continent.getIso());
					continentMap.put("name", continent.getName());
					data.put("continent", continent);
				}
			}
			data.put("city", location);	
		}
		return data;
	}
	
	
	/**
	 * Location.
	 *
	 * @param ip the ip
	 * @return the map
	 */
	public Map<String, Object> location(String ip){
		CityResponse cityResponse;
		try {
			cityResponse = this.databaseReader.city(InetAddress.getByName(ip));
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(ip+" is not valid",e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		if(cityResponse==null){
			return null;
		}
		City city = null;
		Country country = null;
		Continent continent = null;
		Subdivision one = null;
		Subdivision two = null;
		Map<String, Object> data = new LinkedHashMap<>();
		Map<String, Object> location = new LinkedHashMap<>();
		data.put("ip", ip);
		if(cityResponse.getLocation()!=null){
			data.put("latitude",cityResponse.getLocation().getLatitude());
			data.put("longitude",cityResponse.getLocation().getLongitude());
		}
		if(cityResponse.getCity()!=null){
			location.put("name", cityResponse.getCity().getName());
			location.put("geoNameId", cityResponse.getCity().getGeoNameId());
			city = this.cityFinder.find(cityResponse.getCity().getGeoNameId());
		}
		if(city==null && cityResponse.getLocation()!=null){
			city = this.cityFinder.find(cityResponse.getLocation().getLongitude(), cityResponse.getLocation().getLatitude());
		}
		//if city does not match country, remove it
		if(city!=null && cityResponse.getCountry()!=null 
				&& !cityResponse.getCountry().getIsoCode().equals(city.getCountryIsoCode())){
			city=null;
		}
		if(cityResponse.getCountry()!=null){
			country=countryFinder.find(cityResponse.getCountry().getGeoNameId());
		}
		if(country==null && city!=null){
			country=countryFinder.find(city.getCountryIsoCode());
		}
		if(continent==null && cityResponse.getContinent()!=null){
			continent=continentFinder.find(cityResponse.getContinent().getCode());
		}
		if(continent==null && country!=null){
			continent=continentFinder.find(country.getContinent());
		}
		if(city!=null){
			location.put("geoNameId", city.getGeonameId());
			location.put("name", city.getName());
			location.put("population", city.getPopulation());
			TimeZone timeZone = timeZoneFinder.find(city.getTimeZone());
			if(timeZone!=null){
				data.put("timeZone", timeZone);
			}
			one = subdivisionFinder.find(city.getCountryIsoCode(), city.getSubdivisionOne());
			two = subdivisionFinder.find(city.getCountryIsoCode(), city.getSubdivisionOne(), city.getSubdivisionTwo());
		}else if(cityResponse.getSubdivisions()!=null 
				&& cityResponse.getSubdivisions().size()>0){
			one=subdivisionFinder.find(cityResponse.getSubdivisions().get(0).getGeoNameId());
			if(cityResponse.getSubdivisions().size()>1){
				two=subdivisionFinder.find(cityResponse.getSubdivisions().get(1).getGeoNameId());
			}
		}
		List<Map<String, Object>> subdivisions = new ArrayList<>();
		if(two!=null){
			Map<String, Object> sub = new LinkedHashMap<>();
			sub.put("name", two.getName());
			sub.put("geonNameId", two.getGeonameId());
			subdivisions.add(sub);
		}
		if(one!=null){
			Map<String, Object> sub = new LinkedHashMap<>();
			sub.put("name", one.getName());
			sub.put("geonNameId", one.getGeonameId());
			subdivisions.add(sub);
		}
		if(subdivisions!=null && subdivisions.size()>0){
			data.put("subdivisions", subdivisions);
		}
		Map<String, Object> countryMap = new LinkedHashMap<>();
		if(country!=null){
			countryMap.put("area", country.getArea());
			countryMap.put("capital", country.getCapital());
			countryMap.put("currencyCode", country.getCurrencyCode());
			countryMap.put("currencyName", country.getCurrencyName());
			countryMap.put("language", country.getLanguage());
			countryMap.put("name", country.getName());
			countryMap.put("phone", country.getPhone());
			countryMap.put("population", country.getPopulation());
			countryMap.put("iso", country.getIso());
			countryMap.put("geoNameId", country.getGeonameId());
		}else if(cityResponse.getCountry()!=null){
			countryMap.put("name", cityResponse.getCountry().getName());
			countryMap.put("geoNameId", cityResponse.getCountry().getGeoNameId());
			countryMap.put("iso", cityResponse.getCountry().getIsoCode());
		}
		if(countryMap.size()>0){
			data.put("country", countryMap);
		}
		Map<String, Object> continentMap = new LinkedHashMap<>();
		if(continent!=null){
			continentMap.put("geoNameId", continent.getGeonameId());
			continentMap.put("iso", continent.getIso());
			continentMap.put("name", continent.getName());
		}else if(cityResponse.getContinent()!=null){
			continentMap.put("geoNameId", cityResponse.getContinent().getGeoNameId());
			continentMap.put("iso", cityResponse.getContinent().getCode());
			continentMap.put("name", cityResponse.getContinent().getName());
		}
		if(continentMap.size()>0){
			data.put("continent", continent);
		}
		data.put("city", location);
		return data;
	}
	
	/**
	 * Start.
	 *
	 * @return the scheduled database reader
	 * @throws IllegalStateException the illegal state exception
	 */
	public GeoLocation start() throws IllegalStateException{
		if (scheduledExecutorService != null) {
			throw new IllegalStateException("it is already started");
		}
		readDatabase();
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				readDatabase();
			}
		}, 1, 1, TimeUnit.DAYS);
		return this;
	}

	/**
	 * Stop.
	 *
	 * @return the scheduled database reader
	 * @throws IllegalStateException the illegal state exception
	 */
	public GeoLocation stop() throws IllegalStateException{
		if (scheduledExecutorService == null) {
			throw new IllegalStateException("it was never started");
		}
		scheduledExecutorService.shutdown();
		try {
			if (databaseReader != null) {
				this.databaseReader.close();
			}
		} catch (IOException e) {
			logger.warn("error closing reader: {}", e.getMessage());
		}
		return this;
	}

	/**
	 * Read database.
	 */
	public void readDatabase() {
		try{
			readDatabase(databaseUrl);
			loaderListener.success(databaseUrl);
		}catch(Exception e){
			loaderListener.failure(databaseUrl, e);
			logger.error("error loading from remote",e);
			if(StringUtils.isNotBlank(System.getProperty(GEOLITE_FAIL_SAFE_URL))){
				readDatabase(System.getProperty(GEOLITE_FAIL_SAFE_URL));
			}
		}
	}
	
	/**
	 * Read database.
	 *
	 * @param databaseLocationUrl the database location url
	 */
	private void readDatabase(String databaseLocationUrl){
		String onlineMD5Checksum = null;
		try{
			onlineMD5Checksum = IOUtils.toString(new URL(md5ChecksumUrl).openStream()).trim();
		}catch(Exception e){
			logger.error("could not read MD5 online: {}",e.getMessage());
			return;
		}
		if(!onlineMD5Checksum.equals(localMD5Checksum)){
			try{
				logger.info("UPDATING local database with online database");
				DatabaseReader newReader = new DatabaseReader.Builder(new GZIPInputStream(new URL(databaseLocationUrl).openStream())).locales(Collections.singletonList("en")).fileMode(FileMode.MEMORY).withCache(cache).build();
				if(databaseReader!=null){
					final DatabaseReader readerToClose = databaseReader;
					new Timer().schedule(new TimerTask() {				
						@Override
						public void run() {
							try {
								readerToClose.close();
							} catch (IOException e) {
								logger.warn("error closing reader: {}",e.getMessage());
							}
						}
					}, 60*1000);
				}
				this.localMD5Checksum=onlineMD5Checksum;
				this.databaseReader = newReader;
				logger.info("UPDATED local database with online database");
			}catch(Exception e){
				logger.error("could not read Database online: {}",e.getMessage());
			}
		}else{
			logger.info("local and online database are the same");
		}
		this.cityFinder.readCities();
		this.countryFinder.readCountries();
		this.subdivisionFinder.readLevelOne();
		this.subdivisionFinder.readLevelTwo();
		this.timeZoneFinder.readTimeZones();
	}

}
