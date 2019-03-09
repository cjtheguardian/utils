package com.cj.theguardian.utils.timer;

public class StopWatch {

	private long duration;
	private long start;
	private long stop;
	
	public void start() {
		start = System.currentTimeMillis();
		stop = 0;
	}
	
	public void stop() {
		stop = System.currentTimeMillis();
		duration += (stop-start);
		start = 0;
		stop = 0;
	}
	
	public void reset() {
		duration = 0;
		start = 0;
		stop = 0;
	}
	
	public long read() {
		if(start != 0 && stop == 0) {
			long tempDur = System.currentTimeMillis() - start;
			return duration + tempDur;
		}
		return duration;
	}
	
}
