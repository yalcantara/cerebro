package com.yaison.cerebro.algorithms;

import static java.lang.Math.nextDown;
import static java.lang.Math.pow;

import java.io.Serializable;

import com.yaison.cerebro.math.Matrix;

public abstract class SupervisedAlgorithm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5129017844815856746L;
	
	protected static final double BEFORE_1 = nextDown(1.0);
	protected static final double AFTER_0 = 1.0 - BEFORE_1;
	
	protected transient final Matrix X;
	protected transient final Matrix Y;
	
	
	protected final double[] theta;
	
	protected Regularization reg = Regularization.NONE;
	protected double lambda = 0.0;
	
	public SupervisedAlgorithm(int n) {
		Y = null;
		X = null;
		theta = new double[n + 1];
		setRandomTheta();
	}
	
	public SupervisedAlgorithm(double[] theta) {
		Y = null;
		X = null;
		this.theta = new double[theta.length];
		System.arraycopy(theta, 0, this.theta, 0, theta.length);
	}
	
	
	
	public SupervisedAlgorithm(Matrix X, Matrix Y) {
		int m = Y.m();
		if (m < 1) {
			throw new IllegalArgumentException(
					"The Y length must be higher than 0. Got: " + m + ".");
		}
		
		if (X.m() != m) {
			throw new IllegalArgumentException(
					"The length of the X matrix must match the length of the Y matrix. Got: "
							+ X.m() + ", expected: " + m + ".");
		}
		
		this.X = X;
		this.Y = Y;
		// we assume the columns does not include the bias
		theta = new double[X.n() + 1];
		setRandomTheta();
	}
	
	protected static double limit(double val) {
		if (val > BEFORE_1) {
			return BEFORE_1;
		}
		
		if (val < AFTER_0) {
			return AFTER_0;
		}
		
		return val;
	}
	
	protected static double thetaMultByX(double[] t, double[] x) {
		if (t.length < 2) {
			throw new IllegalArgumentException(
					"The length of the t parameter must be equal "
							+ "or higher than 2. Got: " + t.length + ".");
		}
		
		if (x.length + 1 != t.length) {
			throw new IllegalArgumentException(
					"Invalid length of the x parameter. Expected t.length-1 ("
							+ (t.length - 1) + ") but instead got: " + x.length
							+ ".");
		}
		
		double sum = t[0];
		
		for (int i = 0; i < x.length; i++) {
			sum += x[i] * t[i + 1];
		}
		
		return sum;
	}
	
	public abstract SupervisedAlgorithm model();
	
	public abstract double h(double[] x);
	
	public abstract double j();
	
	public abstract double partialDerivative(int j, int i);
	
	public abstract double partialDerivative(int j);
	
	
	public void regularized(Regularization reg){
		this.reg = reg;
	}
	
	public void regParameter(double lambda){
		this.lambda = lambda;
	}
	
	public void setRandomTheta() {
		// set it from [-1, -1)
		
		for (int i = 0; i < theta.length; i++) {
			theta[i] = -1 + Math.random() * 2;
		}
	}
	
	protected static final double pow2(double x) {
		return pow(x, 2);
	}
	
	public int dimensions() {
		return theta.length;
	}
	
	public int n() {
		return theta.length - 1;
	}
	
	public int m() {
		return Y.m();
	}
	
	public double getTheta(int idx) {
		return theta[idx];
	}
	
	protected double thetaMultByX(int row) {
		// theta[0] = bias
		double sum = theta[0];
		
		final int n = X.columns();
		for (int j = 0; j < n; j++) {
			sum += X.get(row, j) * theta[j + 1];
		}
		
		return sum;
	}
	
	public double[] cloneTheta() {
		double[] arr = new double[theta.length];
		System.arraycopy(theta, 0, arr, 0, theta.length);
		return arr;
	}
	
	public void setTheta(int j, double val) {
		theta[j] = val;
	}
	
	public void setTheta(double[] T) {
		if (T.length != theta.length) {
			throw new IllegalArgumentException(
					"Invalid length for the T argument. Expected: "
							+ theta.length + ", got: " + T.length + ".");
		}
		
		System.arraycopy(T, 0, theta, 0, T.length);
	}
}
