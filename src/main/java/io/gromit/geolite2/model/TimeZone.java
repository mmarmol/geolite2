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
 * The Class TimeZone.
 */
public class TimeZone {

	/** The country iso. */
	private String countryIso;
	
	/** The id. */
	private String id;
	
	/** The jan offset. */
	private Float janOffset;
	
	/** The jul offset. */
	private Float julOffset;
	
	/** The raw offset. */
	private Float rawOffset;

	/**
	 * Gets the country iso.
	 *
	 * @return the country iso
	 */
	public String getCountryIso() {
		return countryIso;
	}

	/**
	 * Sets the country iso.
	 *
	 * @param countryIso the new country iso
	 */
	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the jan offset.
	 *
	 * @return the jan offset
	 */
	public Float getJanOffset() {
		return janOffset;
	}

	/**
	 * Sets the jan offset.
	 *
	 * @param janOffset the new jan offset
	 */
	public void setJanOffset(Float janOffset) {
		this.janOffset = janOffset;
	}

	/**
	 * Gets the jul offset.
	 *
	 * @return the jul offset
	 */
	public Float getJulOffset() {
		return julOffset;
	}

	/**
	 * Sets the jul offset.
	 *
	 * @param julOffset the new jul offset
	 */
	public void setJulOffset(Float julOffset) {
		this.julOffset = julOffset;
	}

	/**
	 * Gets the raw offset.
	 *
	 * @return the raw offset
	 */
	public Float getRawOffset() {
		return rawOffset;
	}

	/**
	 * Sets the raw offset.
	 *
	 * @param rawOffset the new raw offset
	 */
	public void setRawOffset(Float rawOffset) {
		this.rawOffset = rawOffset;
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
