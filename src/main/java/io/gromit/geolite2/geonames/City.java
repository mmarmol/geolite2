package io.gromit.geolite2.geonames;

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
