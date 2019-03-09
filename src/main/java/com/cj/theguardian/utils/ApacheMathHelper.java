package com.cj.theguardian.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class ApacheMathHelper {

	public static BigDecimal stdDeviation(Collection<BigDecimal> values, boolean removeOutliers) {
		
		SummaryStatistics stats = new SummaryStatistics();
		values.forEach(value -> stats.addValue(value.doubleValue()));
		
		double mean = stats.getMean();
		double stdDev = stats.getStandardDeviation();
		double threeSdAbove = mean + (stdDev*5);
		double threeSdBelow = mean - (stdDev*5);
		if(removeOutliers) {
			List<BigDecimal> nonOutliers = values.stream().filter(value -> value.doubleValue() < threeSdAbove || value.doubleValue() <= threeSdBelow).collect(Collectors.toList());
			if(nonOutliers.size() != values.size()) {
				return stdDeviation(nonOutliers,removeOutliers);
			}
		}
		
		return new BigDecimal("" + stdDev).setScale(8, RoundingMode.HALF_EVEN);
		
	}
	
}
