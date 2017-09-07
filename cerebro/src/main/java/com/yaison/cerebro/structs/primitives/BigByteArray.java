package com.yaison.cerebro.structs.primitives;

public class BigByteArray implements BigArray {
	
	private final byte[] arr;
	
	public BigByteArray(int length) {
		arr = new byte[length];
	}
	
	public void set(int idx, byte val) {
		arr[idx] = val;
	}
	
	@Override
	public BigArrayType type() {
		return BigArrayType.BYTE;
	}
	
	@Override
	public void setInt(int idx, int val) {
		if (val < Byte.MIN_VALUE || val > Byte.MAX_VALUE) {
			throw new IllegalArgumentException("The value " + val
					+ " is out of range [-128, 127].");
		}
		
		set(idx, (byte) val);
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
