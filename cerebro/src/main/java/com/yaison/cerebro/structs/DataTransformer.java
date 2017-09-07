package com.yaison.cerebro.structs;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.structs.primitives.BigArrayType;

public class DataTransformer {
	
	private final double mean;
	private final double stdDev;
	private final double min;
	private final double max;
	private final BigArrayType type;
	
	public DataTransformer(NumericMetaData meta, BigArrayType type) {
		super();
		this.mean = meta.mean();
		this.stdDev = meta.stdDev();
		this.min = meta.min();
		this.max = meta.max();
		this.type = type;
	}
	
	
	public double unscale(double val) {
		switch (type) {
			case BIT:
				if (max == 1) {
					return val;
				}
				return min + val;
			case BYTE:
				return min + (val - Byte.MIN_VALUE);
			case CHAR:
				return min + (val - Character.MIN_VALUE);
			case INT:
				return min + (val - Integer.MIN_VALUE);
			default:
				throw new CerebroException("Invalid type of array: " + type
						+ ".");
		}
	}
	
	public int rescale(double val) {
		switch (type) {
			case BIT:
				if (max == 1) {
					return (int) val;
				}
				return (int) (val - min);
			case BYTE:
				return (int) (Byte.MIN_VALUE + (val - min));
			case CHAR:
				return (char) (Character.MIN_VALUE + (val - min));
			case INT:
				return (int) (Integer.MIN_VALUE + (val - min));
			default:
				throw new CerebroException("Invalid type of array: " + type
						+ ".");
		}
	}
	
	public double normalize(double val){
		return (val - mean) / stdDev;
	}
	
	public double unnorm(double norm){
		return norm * stdDev + mean;
	}
}
