package io.gromit.geolite2.model;

/**
 * The Class Location.
 */
public class Location {

	/** The city name. */
	private String cityName;
	
	private Integer cityGeonameId;
	
	/** The continent code. */
	private String continentCode;
	
	/** The continent name. */
	private String continentName;
	
	private Integer continentGeonameId;
	
	/** The country iso code. */
	private String countryIsoCode;
	
	/** The country name. */
	private String countryName;
	
	private Integer countryGeonameId;
	
	/** The latitude. */
	private Double latitude;
	
	/** The longitude. */
	private Double longitude;
	
	/** The subdivision name. */
	private String subdivisionName;
	
	/** The subdivision iso code. */
	private String subdivisionIsoCode;
	
	private Integer subdivisionGeonameId;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getCityGeonameId() {
		return cityGeonameId;
	}

	public void setCityGeonameId(Integer cityGeonameId) {
		this.cityGeonameId = cityGeonameId;
	}

	public String getContinentCode() {
		return continentCode;
	}

	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public Integer getContinentGeonameId() {
		return continentGeonameId;
	}

	public void setContinentGeonameId(Integer continentGeonameId) {
		this.continentGeonameId = continentGeonameId;
	}

	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getCountryGeonameId() {
		return countryGeonameId;
	}

	public void setCountryGeonameId(Integer countryGeonameId) {
		this.countryGeonameId = countryGeonameId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getSubdivisionName() {
		return subdivisionName;
	}

	public void setSubdivisionName(String subdivisionName) {
		this.subdivisionName = subdivisionName;
	}

	public String getSubdivisionIsoCode() {
		return subdivisionIsoCode;
	}

	public void setSubdivisionIsoCode(String subdivisionIsoCode) {
		this.subdivisionIsoCode = subdivisionIsoCode;
	}

	public Integer getSubdivisionGeonameId() {
		return subdivisionGeonameId;
	}

	public void setSubdivisionGeonameId(Integer subdivisionGeonameId) {
		this.subdivisionGeonameId = subdivisionGeonameId;
	}

}
