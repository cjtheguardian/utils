package com.cj.theguardian.utils.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Watches {

	private static Logger logger = LogManager.getLogger(Watches.class);
	private static Map<String, StopWatch> watches = new HashMap<>();
	
	public static StopWatch getOrCreateWatch(String name) {
		StopWatch watch = watches.get(name);
		if(watch== null) {
			watch = new StopWatch();
			watches.put(name, watch);
		}
		return watch;
	}
	
	public static void printWatches() {
		for(Entry<String,StopWatch> watch : watches.entrySet()) {
			logger.info(watch.getKey()+": " + watch.getValue().read()/1000 +"s");
		}
	}
	
}
