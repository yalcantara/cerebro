package com.yaison.cerebro.structs.primitives;

public class BigFloatArray implements BigArray {
	
	private final float[] arr;
	public BigFloatArray(int length) {
		arr = new float[length];
	}
	
	public void set(int idx, float val) {
		arr[idx] = val;
	}
	
	@Override
	public void setInt(int idx, int val) {
		set(idx, val);
	}
	
	@Override
	public BigArrayType type() {
		return BigArrayType.FLOAT;
	}
	
	@Override
	public void setDouble(int idx, double val) {
		if (Double.isNaN(val)) {
			throw new IllegalArgumentException(
					"The value for the val argument is NaN.");
		}
		
		set(idx, (float)val);
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
