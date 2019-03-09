package com.cj.theguardian.utils;

import java.math.BigDecimal;

public class AverageDetails {

	private BigDecimal totalSum;
	private int numParams;
	public BigDecimal getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}
	public int getNumParams() {
		return numParams;
	}
	public void setNumParams(int numParams) {
		this.numParams = numParams;
	}
	
}
