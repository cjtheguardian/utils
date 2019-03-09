package com.cj.theguardian.utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeHelper {

	public static final Duration ONE_MONTH = toDuration(DurationFieldType.days(), 28);
	public static final Duration ONE_WEEK = toDuration(DurationFieldType.days(), 7);
	public static final Duration TWO_WEEKS = toDuration(DurationFieldType.days(), 14);
	public static final Duration THREE_WEEKS = toDuration(DurationFieldType.days(), 21);
	public static final Duration[] ONE_TO_FOUR_WEEKS = {ONE_WEEK,TWO_WEEKS,THREE_WEEKS,ONE_MONTH};

	public static DateTime mergeTimeIntoDate(LocalTime time, DateTime date) {
		MutableDateTime mdt = date.toMutableDateTime();
		mdt.setHourOfDay(time.getHourOfDay());
		mdt.setMinuteOfHour(time.getMinuteOfHour());
		mdt.setSecondOfMinute(time.getSecondOfMinute());
		return mdt.toDateTime();
	}

	private static DateTimeFormatter yyyyMMdd = DateTimeFormat.forPattern("yyyyMMdd");
	
	public static DateTime yyyyMMdd(String yyyyMMdd) {
		return JodaTimeHelper.yyyyMMdd.parseDateTime(yyyyMMdd);
	}

	public static Interval getInterval_yyyyMMdd(String start, String end) {
		return new Interval(yyyyMMdd(start), yyyyMMdd(end));
	}

	public static LocalTime localTime(String hhmm) {
		String hh = hhmm.substring(0, 2);
		String mm = hhmm.substring(2);
		return new LocalTime(Integer.valueOf(hh),Integer.valueOf(mm));
	}

	public static long getUnitsByFieldType(DurationFieldType type, Duration duration) {
		if(type.equals(DurationFieldType.days())) {
			return duration.getStandardDays();
		}
		if(type.equals(DurationFieldType.hours())) {
			return duration.getStandardHours();
		}
		if(type.equals(DurationFieldType.minutes())) {
			return duration.getStandardMinutes();
		}
		if(type.equals(DurationFieldType.seconds())) {
			return duration.getStandardSeconds();
		}
		throw new IllegalArgumentException("Unsupported field Type: " + type);
	}

	public static Duration toDuration(DurationFieldType type, long thisIntLength) {
		if(type.equals(DurationFieldType.days())) {
			return Duration.standardDays(thisIntLength);
		}
		if(type.equals(DurationFieldType.hours())) {
			return Duration.standardHours(thisIntLength);
		}
		if(type.equals(DurationFieldType.minutes())) {
			return Duration.standardMinutes(thisIntLength);
		}
		if(type.equals(DurationFieldType.seconds())) {
			return Duration.standardSeconds(thisIntLength);
		}
		throw new IllegalArgumentException("Unsupported field Type: " + type);
	}
	
	public static List<Interval> breakUpIntervals(Interval interval, DurationFieldType breakUpBy, int numIntervals) {
		Duration duration = interval.toDuration();
		long units = JodaTimeHelper.getUnitsByFieldType(breakUpBy, duration);
		long intervalLength = units/numIntervals;
		long remainder = units % numIntervals;
		List<Interval> intervals = new ArrayList<>(numIntervals);
		Interval previousInterval = null;
		for(int i = 0;i < numIntervals;i++) {
			Interval intervalSegment = null;
			long thisIntLength = intervalLength;
			if(remainder > 0 ) {
				thisIntLength += 1;
				remainder--;
			}
			if(i==0) {
				// this is the first interval
				DateTime start = interval.getStart();
				DateTime end = start.plus(JodaTimeHelper.toDuration(breakUpBy, thisIntLength));
				intervalSegment = new Interval(start,end);
			} else if (i+1 == numIntervals) {
				DateTime start = previousInterval.getEnd();
				DateTime end = interval.getEnd();
				intervalSegment = new Interval(start,end);
			} else {
				DateTime start = previousInterval.getEnd();
				DateTime end = start.plus(JodaTimeHelper.toDuration(breakUpBy, thisIntLength));
				intervalSegment = new Interval(start,end);
			}
			previousInterval = intervalSegment;
			intervals.add(intervalSegment);
		}
		
		return intervals;
	}
	
	
}
