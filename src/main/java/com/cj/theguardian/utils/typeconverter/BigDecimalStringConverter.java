package com.cj.theguardian.utils.typeconverter;

import java.math.BigDecimal;

import com.cj.theguardian.utils.exception.UtilityException;

public class BigDecimalStringConverter implements StringConverter<BigDecimal> {

	@Override
	public BigDecimal fromString(String string) {
		if(string == null) {
			return null;
		}
		try {
			return new BigDecimal(string);
		} catch(Exception e) {
			throw new UtilityException(String.format("Error converting %s to BigDecimal", string), e);
		}
	}

}
