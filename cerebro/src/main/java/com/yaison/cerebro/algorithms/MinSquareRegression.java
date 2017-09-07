package com.yaison.cerebro.algorithms;

import static java.lang.Math.abs;

import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.math.MultiVarFunc;


public class MinSquareRegression extends SupervisedAlgorithm implements
		MultiVarFunc {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4198183356520764013L;
	
	public MinSquareRegression(int n) {
		super(n);
	}
	
	public MinSquareRegression(double[] theta) {
		super(theta);
	}
	
	public MinSquareRegression(Matrix X, Matrix Y) {
		super(X, Y);
	}
	
	public SupervisedAlgorithm model() {
		return new MinSquareRegression(theta);
	}
	
	private static double h(double[] theta, double[] x) {
		return thetaMultByX(theta, x);
	}
	
	public double h(double[] x) {
		return h(theta, x);
	}
	
	public double h(int row) {
		return thetaMultByX(row);
	}
	
	public double j() {
		
		double sum = 0.0;
		
		final int m = m();
		for (int i = 0; i < m; i++) {
			sum += pow2(h(i) - Y.get(i, 0));
		}
		
		if (reg == Regularization.L1) {
			
			double regSum = 0.0;
			for (int j = 1; j < theta.length; j++) {
				regSum += abs(theta[j]);
			}
			
			sum += lambda * regSum;
		} else if (reg == Regularization.L2) {
			
			double regSum = 0.0;
			for (int j = 1; j < theta.length; j++) {
				regSum += pow2(theta[j]);
			}
			
			sum += lambda * regSum;
		}
		
		return sum / (2.0 * m);
	}
	
	public double partialDerivative(int j) {
		
		double sum = 0.0;
		final int m = m();
		for (int i = 0; i < m; i++) {
			sum += partialDerivative(j, i);
		}
		
		if (j >= 1) {
			if (reg == Regularization.L1) {
				sum += lambda * abs(theta[j]);
			} else if (reg == Regularization.L2) {
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
		ithDiffCache = (h(i) - Y.get(i, 0));
		ithDiffIdx = i;
		return ithDiffCache;
	}
	
	@Override
	public double partialDerivative(int j, int i) {
		
		if (j == 0) {
			return cache(i);
		}
		
		double terms = cache(i) * X.get(i, j - 1);
		
		return terms;
	}
	
	public void setTheta(double[] T){
		super.setTheta(T);
		ithDiffIdx = -1;
	}
	
	public void setTheta(int j, double val) {
		super.setTheta(j, val);
		ithDiffIdx = -1;
	}
	
	@Override
	public double apply(double... vars) {
		setTheta(vars);
		return j();
	}
	
}
