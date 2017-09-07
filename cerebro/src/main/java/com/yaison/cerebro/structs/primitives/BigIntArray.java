package com.yaison.cerebro.structs.primitives;

public class BigIntArray implements BigArray {


	private final int[] arr;
	public BigIntArray(int length){
		arr = new int[length];
	}

	
	public void set(int idx, int val){
		arr[idx] = val;
	}

	@Override
	public BigArrayType type() {
		return BigArrayType.INT;
	}

	@Override
	public void setInt(int idx, int val) {
		set(idx, val);
	}

	@Override
	public void setDouble(int idx, double val) {
		BigArray.checkInt(val);
		setInt(idx, (int)val);
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
