package com.cj.theguardian.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollectorUtils {

	public static <T> AverageCollector<T> averageCollector(ToBDFunction<T> function, RoundingMode mode) {
		
		return new AverageCollector<T>() {
			
			{
				this.roundingMode = mode;
			}

			@Override
			protected BigDecimal toBigDecimal(T operand) {
				return function.applyBD(operand);
			}
			
		};
	}
	
}
