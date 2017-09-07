package com.yaison.cerebro.structs;

import com.yaison.cerebro.structs.primitives.BigArrayType;

public interface Column {
	
	public void swap(int from, int to);
	
	public NumericMetaData meta();
	
	public void setOriginal(int idx, double val);
	
	default public boolean isBinary() {
		return type() == BigArrayType.BIT;
	}
	
	public BigArrayType type();
	
	public double get(int row);
	
	public double getOriginal(int row);
	
	public DataTransformer transformer();
	
	public int rows();
	
}
