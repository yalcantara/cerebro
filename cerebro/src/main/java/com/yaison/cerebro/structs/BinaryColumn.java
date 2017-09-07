package com.yaison.cerebro.structs;

import com.yaison.cerebro.structs.primitives.BigArrayType;
import com.yaison.cerebro.structs.primitives.BigBitArray;

class BinaryColumn implements Column {
	
	private final BigBitArray arr;
	
	BinaryColumn(int length) {
		arr = new BigBitArray(length);
	}
	
	
	@Override
	public NumericMetaData meta() {
		return null;
	}
	
	@Override
	public double get(int row) {
		return arr.get(row);
	}
	
	@Override
	public double getOriginal(int row) {
		return arr.get(row);
	}
	
	@Override
	public DataTransformer transformer() {
		return null;
	}
	
	@Override
	public int rows() {
		return arr.length();
	}
	
	@Override
	public void setOriginal(int idx, double val) {
		if (val == 0.0) {
			arr.set(idx, false);
			return;
		}
		
		if (val == 1.0) {
			arr.set(idx, true);
			return;
		}
		
		throw new IllegalArgumentException("Only 1 or 0 allowed. Got: " + val
				+ ".");
	}
	
	@Override
	public void swap(int from, int to) {
		arr.swap(from, to);
	}

	public boolean isBinary(){
		return true;
	}
	
	@Override
	public BigArrayType type() {
		return BigArrayType.BIT;
	}
	
}
