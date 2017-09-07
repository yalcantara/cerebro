package com.yaison.cerebro.algorithms;

public class StochasticGradientDescent {
	
	private double[] temp;
	private double[] batchAvg;
	private double alpha = 0.1;
	private SupervisedAlgorithm algo;
	private final int m;
	private int idx = 0;
	private int batchSize = 10;
	
	public StochasticGradientDescent(SupervisedAlgorithm algo) {
		temp = new double[algo.dimensions()];
		batchAvg = new double[algo.dimensions()];
		this.algo = algo;
		this.m = algo.m();
	}
	
	public double alpha() {
		return alpha;
	}
	
	public void alpha(double alpha) {
		this.alpha = alpha;
	}
	
	public int idx() {
		return idx;
	}
	
	public int m() {
		return m;
	}
	
	private void miniBatch() {
		
		// init, just set the values
		for (int j = 0; j < batchAvg.length; j++) {
			batchAvg[j] = algo.partialDerivative(j, idx);
		}
		//
		
		next();
		for (int i = 1; i < batchSize; i++) {
			for (int j = 0; j < temp.length; j++) {
				batchAvg[j] += algo.partialDerivative(j, idx);
			}
			
			next();
		}
		
		// divide
		for (int i = 0; i < batchAvg.length; i++) {
			batchAvg[i] = batchAvg[i] / batchSize;
		}
	}
	
	private void next() {
		idx++;
		if (idx >= m) {
			idx = 0;
		}
	}
	
	public void miniBatchDescend(int iter){
		for(int i =0; i < iter; i++){
			miniBatchDescend();
		}
	}
	
	public void miniBatchDescend(){
		
		miniBatch();
		for (int j = 0; j < temp.length; j++) {
			double t = algo.getTheta(j) - alpha * batchAvg[j];
			algo.setTheta(j, t);
		}
	}
	
	public void descend() {
		
		for (int j = 0; j < temp.length; j++) {
			temp[j] = algo.partialDerivative(j, idx);
		}
		
		for (int j = 0; j < temp.length; j++) {
			double t = algo.getTheta(j) - alpha * temp[j];
			algo.setTheta(j, t);
		}
		
		next();
	}
	
	public void descend(int iter) {
		for (int i = 0; i < iter; i++) {
			descend();
		}
	}
	
	public double[] cloneTheta() {
		return algo.cloneTheta();
	}
}
