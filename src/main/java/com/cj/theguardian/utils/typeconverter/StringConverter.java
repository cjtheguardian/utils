package com.cj.theguardian.utils.typeconverter;

public interface StringConverter<T> {

	public default String toString(T value) {
		return value.toString();
	}
	
	public T fromString(String string) ;
	
}
