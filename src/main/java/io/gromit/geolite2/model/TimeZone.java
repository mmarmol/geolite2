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
	private Integer id;
	
	/** The name. */
	private String name;
	
	/** The dtsoffset. */
	private Integer dtsOffset;
	
	/** The utc offset. */
	private Integer utcOffset;
	
	/** The current offset. */
	private Integer currentOffset;
	
	/** The changed at. */
	private Long changedAt;
	
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
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the dts offset.
	 *
	 * @return the dts offset
	 */
	public Integer getDtsOffset() {
		return dtsOffset;
	}

	/**
	 * Sets the dts offset.
	 *
	 * @param dtsOffset the new dts offset
	 */
	public void setDtsOffset(Integer dtsOffset) {
		this.dtsOffset = dtsOffset;
	}

	/**
	 * Gets the utc offset.
	 *
	 * @return the utc offset
	 */
	public Integer getUtcOffset() {
		return utcOffset;
	}

	/**
	 * Sets the utc offset.
	 *
	 * @param utcOffset the new utc offset
	 */
	public void setUtcOffset(Integer utcOffset) {
		this.utcOffset = utcOffset;
	}

	/**
	 * Gets the current offset.
	 *
	 * @return the current offset
	 */
	public Integer getCurrentOffset() {
		return currentOffset;
	}

	/**
	 * Sets the current offset.
	 *
	 * @param currentOffset the new current offset
	 */
	public void setCurrentOffset(Integer currentOffset) {
		this.currentOffset = currentOffset;
	}

	/**
	 * Gets the changed at.
	 *
	 * @return the changed at
	 */
	public Long getChangedAt() {
		return changedAt;
	}

	/**
	 * Sets the changed at.
	 *
	 * @param nextOffset the new changed at
	 */
	public void setChangedAt(Long nextOffset) {
		this.changedAt = nextOffset;
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
