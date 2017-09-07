package com.yaison.cerebro.math;

public class MatrixImpl implements Matrix {
	
	protected final int m;
	protected final int n;
	protected final double[] arr;
	
	public MatrixImpl(int m, int n) {
		arr = new double[m * n];
		this.m = m;
		this.n = n;
	}
	
	@Override
	public void set(int row, int col, double val) {
		if (row >= m) {
			throw new IndexOutOfBoundsException("The row parameter is "
					+ "out of bounds. Rows " + m + ", row parameter: " + row
					+ ".");
		}
		
		if (col >= n) {
			throw new IndexOutOfBoundsException("The col parameter is "
					+ "out of bounds. Columns " + n + ", col parameter: " + col
					+ ".");
		}
		arr[row * n + col] = val;
	}
	
	@Override
	public int n() {
		return n;
	}
	
	@Override
	public int m() {
		return m;
	}
	
	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= m) {
			throw new IllegalArgumentException(
					"The row param must be between [0, " + (m - 1)
							+ "], but instead got: " + row + ".");
		}
		
		if (col < 0 || col >= n) {
			throw new IllegalArgumentException(
					"The col param must be between [0, " + (n - 1)
							+ "], but instead got: " + col + ".");
		}
		return arr[row * n + col];
	}
	
	public Matrix clone() {
		return Matrix.super.clone();
	}
	
	@Override
	public String toString() {
		return "MatrixImpl [m=" + m + ", n=" + n + "]";
	}
	
}
