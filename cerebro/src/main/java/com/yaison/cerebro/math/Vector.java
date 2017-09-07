package com.yaison.cerebro.math;

import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sqrt;

public interface Vector extends Matrix {
	public static Vector create(int d) {
		return new VectorImpl(d);
	}
	
	public static Vector rand(int d) {
		Vector v = create(d);
		
		for (int i = 0; i < d; i++) {
			v.set(i, -1 + 2 * random());
		}
		
		return v;
	}
	
	public static Vector from(double[] arr) {
		int d = arr.length;
		Vector v = create(d);
		for (int i = 0; i < d; i++) {
			v.set(i, arr[i]);
		}
		return v;
	}
	
	public static Vector from(Matrix... matrices) {
		int d = 0;
		for (int i = 0; i < matrices.length; i++) {
			d += matrices[i].size();
		}
		
		Vector v = Vector.create(d);
		
		int idx = 0;
		for (int i = 0; i < matrices.length; i++) {
			Matrix mtr = matrices[i];
			int m = mtr.m();
			int n = mtr.n();
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < n; k++) {
					v.set(idx, mtr.get(j, k));
					idx++;
				}
			}
		}
		
		return v;
	}
	
	public double get(int idx);
	
	public void set(int idx, double val);
	
	@Override
	default public double get(int row, int col) {
		int min = min(row, col);
		if (min != 0) {
			throw new IllegalArgumentException(
					"Invalid parameters. This is a vector with dimension "
							+ m() + "x" + n() + ", parameters row: " + row
							+ ", col: " + col + ".");
		}
		
		int m = m();
		if (m == 1) {
			return get(col);
		}
		int n = n();
		if (n == 1) {
			return get(row);
		}
		
		throw new IllegalArgumentException("Invalid vector: " + m + "x" + n
				+ ".");
	}
	
	@Override
	default public void set(int row, int col, double val) {
		if (col != 0) {
			throw new LinearAlgebraException(
					"Invalid col. Only 0 allowed, but got " + col + ".");
		}
		set(col, val);
	}
	
	default public void set(double[] vals, int start) {
		int l = size();
		for (int i = 0; i < l; i++) {
			set(i, vals[start + i]);
		}
	}
	
	default public void copy(double[] to, int start) {
		int l = size();
		for (int i = 0; i < l; i++) {
			to[start + i] = get(i);
		}
	}
	
	default public double dot(Vector v) {
		int l = size();
		int vl = v.size();
		if (l != vl) {
			throw new LinearAlgebraException("The vector length must be " + l
					+ ", but instead got: " + vl + ".");
		}
		
		double sum = 0;
		for (int i = 0; i < l; i++) {
			sum += get(i) * v.get(i);
		}
		
		return sum;
	}
	
	default public Vector proj(Vector v) {
		return scale(dot(v) / dot(this));
	}
	
	default public Vector scale(double scalar) {
		int d = size();
		Vector v = create(d);
		for (int i = 0; i < d; i++) {
			v.set(i, get(i) * scalar);
			
		}
		return v;
	}
	
	default public Vector unit() {
		int s = size();
		Vector v = create(s);
		double norm = norm();
		
		for (int i = 0; i < s; i++) {
			v.set(i, get(i) / norm);
		}
		
		return v;
	}
	
	default public Vector subtract(Vector b) {
		
		int d = size();
		
		int bd = b.size();
		if (d != bd) {
			throw new LinearAlgebraException(
					"Vector dimensions must be the same. This " + b
							+ ", other " + bd + ".");
		}
		
		Vector c = create(d);
		for (int i = 0; i < d; i++) {
			c.set(i, get(i) - b.get(i));
		}
		
		return c;
	}
	
	default public Vector add(Vector b) {
		
		int d = size();
		
		int bd = b.size();
		if (d != bd) {
			throw new LinearAlgebraException(
					"Vector dimensions must be the same. This " + b
							+ ", other " + bd + ".");
		}
		
		Vector c = create(d);
		for (int i = 0; i < d; i++) {
			c.set(i, get(i) + b.get(i));
		}
		
		return c;
	}
	
	default public double norm() {
		double sum = 0.0;
		int l = size();
		for (int i = 0; i < l; i++) {
			sum += pow(get(i), 2);
		}
		
		return sqrt(sum);
	}
	
	default public double[] toVectorArray() {
		
		final int d = size();
		double[] arr = new double[d];
		for (int j = 0; j < d; j++) {
			arr[j] = get(j);
		}
		
		return arr;
	}
	
	@Override
	default public Vector clone() {
		int d = size();
		Vector v = create(d);
		for (int i = 0; i < d; i++) {
			v.set(i, get(i));
		}
		return v;
	}
}
