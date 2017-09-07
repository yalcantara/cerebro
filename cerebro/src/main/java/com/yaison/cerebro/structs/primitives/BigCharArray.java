package com.yaison.cerebro.structs.primitives;

public class BigCharArray implements BigArray {
	
	private final char[] arr;
	
	public BigCharArray(int length) {
		arr = new char[length];
	}
	
	public void set(int idx, char val) {
		arr[idx] = val;
	}
	
	@Override
	public BigArrayType type() {
		return BigArrayType.CHAR;
	}
	
	@Override
	public void setInt(int idx, int val) {
		if (val < Character.MIN_VALUE || val > Character.MAX_VALUE) {
			throw new IllegalArgumentException("The value " + val
					+ " is out of range [0, 65535].");
		}
		
		set(idx, (char) val);
	}
	
	@Override
	public void setDouble(int idx, double val) {
		BigArray.checkInt(val);
		setInt(idx, (int) val);
	}
	
	@Override
	public int length() {
		return arr.length;
	}
	
	@Override
	public double get(int idx) {
		return arr[idx];
	}
	
}
