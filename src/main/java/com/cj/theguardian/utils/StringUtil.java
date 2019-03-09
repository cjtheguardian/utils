package com.cj.theguardian.utils;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;

public class StringUtil {

	public static String toString(Object obj) {
		if(obj instanceof Duration) {
			return "" + JodaTimeHelper.getUnitsByFieldType(DurationFieldType.days(), (Duration)obj) + " days";
		} else {
			return obj.toString();
		}
	}
	
}
