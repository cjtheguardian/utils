package com.cj.theguardian.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public abstract class AverageCollector<T> implements Collector<T,AverageDetails,BigDecimal>{

		protected RoundingMode roundingMode = RoundingMode.HALF_EVEN;
	
		public Supplier<AverageDetails> supplier() {
			return new Supplier<AverageDetails>() {

				public AverageDetails get() {
					AverageDetails ad = new AverageDetails();
					ad.setTotalSum(BigDecimal.ZERO);
					ad.setNumParams(0);
					return ad;
				}
				
			};
			
		}

		public BiConsumer<AverageDetails, T> accumulator() {
			return new BiConsumer<AverageDetails,T>() {
				public void accept(AverageDetails t, T u) {
					t.setTotalSum(t.getTotalSum().add(toBigDecimal(u)));
					t.setNumParams(t.getNumParams() + 1);
				}
				
			};
		}

		protected abstract BigDecimal toBigDecimal(T operand) ;

		public BinaryOperator<AverageDetails> combiner() {
			return new BinaryOperator<AverageDetails> () {
				public AverageDetails apply(AverageDetails t, AverageDetails u) {
					AverageDetails ad = new AverageDetails();
					ad.setTotalSum(t.getTotalSum().add(u.getTotalSum()));
					ad.setNumParams(t.getNumParams() + u.getNumParams());
					return ad;
				}
				
			};
		}

		public Function<AverageDetails, BigDecimal> finisher() {
			return new Function<AverageDetails,BigDecimal> () {
				public BigDecimal apply(AverageDetails t) {
					return t.getTotalSum().divide(new BigDecimal(""+t.getNumParams()),roundingMode);
				}
			};
		}

		public Set<Characteristics> characteristics() {
			return new HashSet<>();
		}
		
	

}
