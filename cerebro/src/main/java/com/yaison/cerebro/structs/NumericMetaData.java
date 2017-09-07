package com.yaison.cerebro.structs;

import static java.lang.Math.sqrt;

import java.io.Serializable;

public class NumericMetaData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1176965849293413515L;
	
	private final String name;
	private final double var;
	
	private final double mean;
	private final double max;
	private final double min;
	private final boolean discrete;
	private final int length;
	
	public NumericMetaData(String name, double var, double mean, double max,
			double min, boolean discrete, int length) {
		super();
		this.name = name;
		this.var = var;
		this.mean = mean;
		this.max = max;
		this.min = min;
		this.discrete = discrete;
		this.length = length;
	}

	
	public boolean zeroed() {
		return max == 0.0 && min == 0.0;
	}
	
	public boolean binary(){
		return discrete && max == 1.0 && min == 0.0;
	}
	
	public double stdDev() {
		return sqrt(var);
	}
	
	public double var() {
		return var;
	}
	
	public double mean() {
		return mean;
	}
	
	public double max() {
		return max;
	}
	
	public double min() {
		return min;
	}
	
	public boolean discrete() {
		return discrete;
	}
	
	public int length() {
		return length;
	}
	
	
	@Override
	public String toString() {
		return "ColumnMetaData [name=" + name + ", var=" + var + ", stdDev="
				+ stdDev() + ", mean=" + mean + ", max=" + max + ", min=" + min
				+ ", discrete=" + discrete + ", length=" + length + "]";
	}
	
}
