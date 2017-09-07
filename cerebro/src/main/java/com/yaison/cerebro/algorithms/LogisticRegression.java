package com.yaison.cerebro.algorithms;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.log;

import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.math.MultiVarFunc;


public class LogisticRegression extends SupervisedAlgorithm implements
		MultiVarFunc {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -376371893997443985L;
	
	public LogisticRegression(int n) {
		super(n);
	}
	
	public LogisticRegression(double[] theta) {
		super(theta);
	}
	
	public LogisticRegression(Matrix X, Matrix Y) {
		super(X, Y);
		
		final int m = Y.m();
		for (int i = 0; i < m; i++) {
			double y = Y.get(i, 0);
			if (y != 1.0 && y != 0.0) {
				throw new IllegalArgumentException(
						"The only allowed values for Y are 0 and 1. Got: " + y
								+ ", at idx: " + i + ".");
			}
		}
	}
	
	@Override
	public SupervisedAlgorithm model() {
		return new LogisticRegression(theta);
	}
	
	private static double g(double z) {
		return 1.0 / (1 + exp(-z));
	}
	
	public double h(double[] x) {
		double z = thetaMultByX(theta, x);
		
		return g(z);
	}
	
	public double h(int row) {
		double z = thetaMultByX(row);
		
		return g(z);
	}
	
	public double cost(int row) {
		final double h = h(row);
		final boolean y = Y.get(row, 0) == 1;
		
		return costFor(h, y);
	}
	
	protected double costFor(double h, boolean y) {
		
		if (y) {
			if (h == 1.0) {
				return 0.0;
			}
			
			if (h == 0.0) {
				return -log(AFTER_0);
			}
			
			return -log(limit(h));
		}
		
		if (h == 1.0) {
			return -log(AFTER_0);
		}
		
		if (h == 0.0) {
			return 0.0;
		}
		
		return -log(limit(1.0 - h));
	}
	
	public double j() {
		
		double sum = 0.0;
		
		final int m = m();
		for (int i = 0; i < m; i++) {
			sum += cost(i);
		}
		
		if (reg == Regularization.L1) {
			final int n = n();
			double regSum = 0.0;
			for (int j = 1; j <= n; j++) {
				regSum += abs(theta[j]);
			}
			
			sum += lambda * regSum / 2.0;
		} else if (reg == Regularization.L2) {
			final int n = n();
			double regSum = 0.0;
			for (int j = 1; j <= n; j++) {
				regSum += pow2(theta[j]);
			}
			
			sum += lambda * regSum / 2.0;
		}
		
		return sum / m;
	}
	
	
	

	
	@Override
	public double partialDerivative(int j) {
		
		double sum = 0.0;
		final int m = m();
		for (int i = 0; i < m; i++) {
			sum += partialDerivative(j, i);
		}
		
		if(j >= 1){
			if(reg == Regularization.L1){
				sum += lambda * abs(theta[j]);
			}else if(reg == Regularization.L2){
				sum += lambda * pow2(theta[j]);
			}
		}
		
		return sum / m;
	}
	
	private int ithDiffIdx = -1;
	private double ithDiffCache = 0.0;
	
	
	private double cache(int i){
		if(ithDiffIdx == i){
			return ithDiffCache;
		}
		ithDiffCache = cost(i);
		ithDiffIdx = i;
		return ithDiffCache;
	}
	
	@Override
	public double partialDerivative(int j, int i) {
		
		if(j == 0){
			return cache(i);
		}
		
		double terms;
		
		terms = cache(i) * X.get(i, j-1);
		
		return terms;
	}
	
	@Override
	public double apply(double... vars) {
		setTheta(vars);
		return j();
	}
	
}
