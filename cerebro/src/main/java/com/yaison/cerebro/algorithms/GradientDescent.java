package com.yaison.cerebro.algorithms;


public class GradientDescent {
	
	
	
	private double[] temp;
	
	private double alpha = 0.1;
	private SupervisedAlgorithm algo;
	
	public GradientDescent(SupervisedAlgorithm algo) {
		temp = new double[algo.dimensions()];
		this.algo = algo;
	}
	
	public double alpha(){
		return alpha;
	}
	
	public void alpha(double alpha){
		this.alpha = alpha;
	}
	
	public void descend(int iter){
		for(int i =0; i < iter; i++){
			descend();
		}
	}
		
	public void descend() {
		for (int j = 0; j < temp.length; j++) {
			temp[j] = algo.partialDerivative(j);
		}
		
		for (int j = 0; j < temp.length; j++) {
			double t = algo.getTheta(j) - alpha * temp[j];
			algo.setTheta(j, t);
		}
	}
	
	public double[] cloneTheta(){
		return algo.cloneTheta();
	}
}
