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
package io.gromit.geolite2.model;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class City.
 */
public class City {

	/** The geoname id. */
	private Integer geonameId;
	
	/** The name. */
	private String name;
	
	/** The latitude. */
	private Double latitude;
	
	/** The longitude. */
	private Double longitude;
	
	/** The country iso code. */
	private String countryIsoCode;
	
	/** The subdivision one. */
	private String subdivisionOne;
	
	/** The subdivision two. */
	private String subdivisionTwo;
	
	/** The population. */
	private BigInteger population;
	
	/** The time zone. */
	private String timeZone;
	
	/**
	 * Gets the geoname id.
	 *
	 * @return the geoname id
	 */
	public Integer getGeonameId() {
		return geonameId;
	}
	
	/**
	 * Sets the geoname id.
	 *
	 * @param geonameId the new geoname id
	 */
	public void setGeonameId(Integer geonameId) {
		this.geonameId = geonameId;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the country iso code.
	 *
	 * @return the country iso code
	 */
	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	/**
	 * Sets the country iso code.
	 *
	 * @param countryIsoCode the new country iso code
	 */
	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}

	/**
	 * Gets the population.
	 *
	 * @return the population
	 */
	public BigInteger getPopulation() {
		return population;
	}
	
	/**
	 * Sets the population.
	 *
	 * @param population the new population
	 */
	public void setPopulation(BigInteger population) {
		this.population = population;
	}
	
	/**
	 * Gets the time zone.
	 *
	 * @return the time zone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	
	/**
	 * Sets the time zone.
	 *
	 * @param timeZone the new time zone
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	/**
	 * Gets the subdivision one.
	 *
	 * @return the subdivision one
	 */
	public String getSubdivisionOne() {
		return subdivisionOne;
	}

	/**
	 * Sets the subdivision one.
	 *
	 * @param subdivisionOne the new subdivision one
	 */
	public void setSubdivisionOne(String subdivisionOne) {
		this.subdivisionOne = subdivisionOne;
	}

	/**
	 * Gets the subdivision two.
	 *
	 * @return the subdivision two
	 */
	public String getSubdivisionTwo() {
		return subdivisionTwo;
	}

	/**
	 * Sets the subdivision two.
	 *
	 * @param subdivisionTwo the new subdivision two
	 */
	public void setSubdivisionTwo(String subdivisionTwo) {
		this.subdivisionTwo = subdivisionTwo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
