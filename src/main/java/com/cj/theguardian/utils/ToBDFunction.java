package com.cj.theguardian.utils;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBDFunction<T> {

	BigDecimal applyBD(T t);
	
}
