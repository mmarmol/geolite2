package io.gromit.geolite2.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class Offset.
 */
public class Offset implements Comparable<Offset>{

	/** The timezone id. */
	private Integer timezoneId;
	
	/** The time start. */
	private Long timeStart;
	
	/** The gmt offset. */
	private Integer gmtOffset;
	
	/** The dst. */
	private Boolean dst;
	
	/**
	 * Instantiates a new offset.
	 *
	 * @param timeStart the time start
	 */
	public Offset(Long timeStart){
		this.timeStart = timeStart;
	}

	/**
	 * Gets the timezone id.
	 *
	 * @return the timezone id
	 */
	public Integer getTimezoneId() {
		return timezoneId;
	}

	/**
	 * Sets the timezone id.
	 *
	 * @param timezoneId the new timezone id
	 */
	public void setTimezoneId(Integer timezoneId) {
		this.timezoneId = timezoneId;
	}

	/**
	 * Gets the time start.
	 *
	 * @return the time start
	 */
	public Long getTimeStart() {
		return timeStart;
	}

	/**
	 * Sets the time start.
	 *
	 * @param timeStart the new time start
	 */
	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}

	/**
	 * Gets the gmt offset.
	 *
	 * @return the gmt offset
	 */
	public Integer getGmtOffset() {
		return gmtOffset;
	}

	/**
	 * Sets the gmt offset.
	 *
	 * @param gmtOffset the new gmt offset
	 */
	public void setGmtOffset(Integer gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	/**
	 * Gets the dst.
	 *
	 * @return the dst
	 */
	public Boolean getDst() {
		return dst;
	}

	/**
	 * Sets the dst.
	 *
	 * @param dst the new dst
	 */
	public void setDst(Boolean dst) {
		this.dst = dst;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int compareTo(Offset o) {
		return timeStart.compareTo(o.getTimeStart());
	}
}
