package com.yaison.cerebro.algorithms;

import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sqrt;

import com.yaison.cerebro.math.Matrix;

public class KMeans {
	
	
	
	private transient final Matrix X;
	private transient final byte[] c;
	private final double[][] u;
	
	public KMeans(Matrix X, int clusters) {
		c = new byte[X.m()];
		
		// at u[0] we are going to store the number of instances that are mapped
		// to the point.
		int n = X.n();
		u = new double[clusters][n + 1];
		
		this.X = X;
		init();
	}
	
	public KMeans(Matrix X, double[][] start) {
		c = new byte[X.m()];
		
		// at u[0] we are going to store the number of instances that are mapped
		// to the point.
		int n = X.n();
		u = new double[start.length][n + 1];
		for (int i = 0; i < start.length; i++) {
			u[i][0] = 0;
			System.arraycopy(start[i], 0, u[i], 1, start[i].length);
		}
		
		this.X = X;
	}
	
	public int count(int cluster){
		return (int)u[cluster][0];
	}
	
	public int clusters(){
		return u.length;
	}
	
	public void iterate(int iter) {
		for (int i = 0; i < iter; i++) {
			assign();
			avg();
		}
	}
	
	private void assign() {
		
		int m = X.rows();
		int n = X.n();
		int K = u.length;
		for (int i = 0; i < m; i++) {
			
			double mind = distance(i, 0, n);
			c[i] = 0;
			for (byte k = 1; k < K; k++) {
				double crtd = distance(i, k, n);
				if (crtd < mind) {
					mind = crtd;
					c[i] = k;
				}
			}
		}
		
	}
	
	private void avg() {
		
		int m = X.rows();
		int n = X.columns();
		int clusters = u.length;
		
		for (int k = 0; k < clusters; k++) {
			fill(u[k], 0.0);
		}
		
		for (int i = 0; i < m; i++) {
			int k = c[i];
			double[] point = u[k];
			point[0]++;
			
			for (int j = 0; j < n; j++) {
				point[j + 1] += X.get(i, j);
			}
			
		}
		
		for (int k = 0; k < clusters; k++) {
			double instances = u[k][0];
			if (instances > 0) {
				for (int j = 0; j < n; j++) {
					u[k][j + 1] /= instances;
				}
			}
		}
	}
	
	private void init() {
		int n = X.n();
		for (int k = 0; k < u.length; k++) {
			for (int j = 0; j < n; j++) {
				
				// [0] is reserved for the number of instances
				// random from -5 to 5;
				u[k][j + 1] = -5 + random() * (5.0);
			}
		}
	}
	
	private void fill(double[] arr, double val) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = val;
		}
	}
	
	public double j() {
		
		double sum = 0.0;
		
		int m = X.m();
		int n = X.n();
		for (int i = 0; i < m; i++) {
			sum += pow2Distance(i, n);
		}
		
		return (1.0 / m) * sum;
	}
	
	double pow2Distance(int i, int n) {
		double sum = 0.0;
		
		for (int j = 0; j < n; j++) {
			sum += pow(X.get(i, j) - u[c[i]][j + 1], 2);
		}
		
		return sum;
	}
	
	double distance(int i, int k, int n) {
		double sum = 0.0;
		
		for (int j = 0; j < n; j++) {
			sum += pow(X.get(i, j) - u[k][j + 1], 2);
		}
		
		return sqrt(sum);
	}
	
}
