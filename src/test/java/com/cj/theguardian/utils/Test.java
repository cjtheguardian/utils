package com.cj.theguardian.utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class Test {

	
	static Duration ONE_SECOND = Duration.standardSeconds(1);
	static Duration FIVE_MIN = Duration.standardMinutes(5);
	static Duration TEN_MIN = Duration.standardMinutes(10);
	

	static List<Duration> durations = new ArrayList<>();
	static List<Integer> intensities = new ArrayList<>();
	
	static {		
		durations.set(0, FIVE_MIN);
		intensities.set(0, 20);
		
		durations.set(1, TEN_MIN);
		intensities.set(1, 50);
		
		durations.set(2, FIVE_MIN);
		intensities.set(2, 30);
	}
	
	public static void main(String[] args) {
		
		DateTime startTime = DateTime.now();
		
		Duration updateInterval = ONE_SECOND;
		DateTime lastUpdateTime = DateTime.now();
		
		while(true) {
			DateTime nextUpdateTime = lastUpdateTime.plus(updateInterval);
			DateTime now = DateTime.now();
			if(now.isAfter(nextUpdateTime)) {
				lastUpdateTime = now;
				updateIntensity(startTime);
			}
		}		
	}

	private static void updateIntensity(DateTime startTime) {
		DateTime now = DateTime.now();
		int i = getIntensityIndex(startTime,now);
		int intensity = intensities.get(i);
		// send intensity value to UI
	}

	private static int getIntensityIndex(DateTime startTime, DateTime now) {
		Duration totalDur = new Interval(startTime,now).toDuration();
		Duration sumDur = Duration.ZERO;
		for(int i = 0;i<durations.size();i++) {
			sumDur = sumDur.plus(durations.get(i));
			if(sumDur.isLongerThan(totalDur)) {
				return i;
			}
		}
		return durations.size() - 1 ;
		
	}
	
}

