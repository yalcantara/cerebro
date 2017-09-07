package com.yaison.cerebro.math;

import java.util.Arrays;



public class VectorImpl implements Vector {
	
	private final int m;
	private final int n;
	private final double[] arr;
	
	public VectorImpl(int d) {
		this.arr = new double[d];
		this.m = d;
		this.n = 1;
	}
	
	@Override
	public int m() {
		return m;
	}
	
	@Override
	public int n() {
		return n;
	}
	
	@Override
	public void set(int idx, double val) {
		arr[idx] = val;
	}
	
	@Override
	public double get(int idx) {
		return arr[idx];
	}
	
	public Vector clone(){
		return Vector.super.clone();
	}

	@Override
	public String toString() {
		return "VectorImpl [" + m() +"x" +n() + ", arr=" + Arrays.toString(arr) + "]";
	}
}
