package com.cj.theguardian.utils.time;

import org.joda.time.DateTime;

public enum TimeType {

	YEAR(1970, 3000), MONTH(1, 12), DAY(1, 31), HOUR(0,23), MINUTE(0,59);

	public final static TimeType HIGH_TYPE = YEAR;
	
	private int minValue;
	private int maxValue;

	private TimeType(int min, int max) {
		this.minValue = min;
		this.maxValue = max;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public TimeType next() {
		switch (this) {
		case HOUR:
			return MINUTE;
		case DAY:
			return HOUR;
		case MONTH:
			return DAY;
		case YEAR:
			return MONTH;
		
		default:
			return null;
		}
	}
	
	public int getUnit(DateTime time) {
		switch(this) {
		case YEAR:
			return time.getYear();
		case MONTH:
			return time.getMonthOfYear();
		case DAY:
			return time.getDayOfMonth();
		case HOUR:
			return time.getHourOfDay();
		case MINUTE:
			return time.getMinuteOfHour();
		}
		throw new RuntimeException("invalid type");
	}

	/**
	 * returns if the dates match up to, but not including this level 
	 * 
	 * ie, if this is DAY, the YEAR, and MONTH must match, but DAY, HOUR and SECOND is ok
	 * 
	 * @param openTime
	 * @param closeTime
	 * @return
	 */
	public boolean datesMatch(DateTime d1, DateTime d2) {
		TimeType next = HIGH_TYPE;
		while(next != null && next != this) {
			if(next.getUnit(d2) != next.getUnit(d1)) {
				return false;
			}
			next = next.next();
		}
		return true;
	}

}
