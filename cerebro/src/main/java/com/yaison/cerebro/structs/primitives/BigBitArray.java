package com.yaison.cerebro.structs.primitives;

import java.util.Random;

public class BigBitArray implements BigArray {
	
	
	
	public static void main(String[] args) {
		BigBitArray arr = new BigBitArray(100_000_000);
		
		
		
		boolean[] p = new boolean[arr.length()];
		
		Random r = new Random();
		for (int i = 0; i < arr.length(); i++) {
			p[i] = r.nextBoolean();
			arr.set(i, p[i]);
		}
		
		for (int i = 0; i < p.length; i++) {
			if ((arr.get(i) == 1.0) != p[i]) {
				System.out.println("wrong at idx: " + i);
			}
		}
	}
	
	
	private final boolean[] arr;
	public BigBitArray(int length) {
		arr = new boolean[length];
	}
	
	@Override
	public final double get(int idx) {
		return arr[idx]?1:0;
	}
	
	
	public final void set(int idx, boolean val) {
		arr[idx] = val;
	}
	
	@Override
	public BigArrayType type() {
		return BigArrayType.BIT;
	}
	
	@Override
	public void setInt(int idx, int val) {
		if (val < 0 || val > 1) {
			throw new IllegalArgumentException("The value " + val
					+ " is out of range [0, 1].");
		}
		
		set(idx, val == 1);
	}
	
	public int length(){
		return arr.length;
	}
	
	@Override
	public void setDouble(int idx, double val) {
		BigArray.checkInt(val);
		setInt(idx, (int) val);
	}
}
