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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class Continent.
 */
public class Continent {

	/** The geoname id. */
	private Integer geonameId;
	
	/** The iso. */
	private String iso;
	
	/** The name. */
	private String name;
	
	/**
	 * Instantiates a new continent.
	 */
	public Continent() {
	}
	
	/**
	 * Instantiates a new continent.
	 *
	 * @param geonameId the geoname id
	 * @param iso the iso
	 * @param name the name
	 */
	public Continent(Integer geonameId, String iso, String name) {
		this.geonameId = geonameId;
		this.iso = iso;
		this.name = name;
	}

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
	 * Gets the iso.
	 *
	 * @return the iso
	 */
	public String getIso() {
		return iso;
	}

	/**
	 * Sets the iso.
	 *
	 * @param iso the new iso
	 */
	public void setIso(String iso) {
		this.iso = iso;
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
