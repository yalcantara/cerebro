package com.yaison.cerebro.descriptive;

import static java.lang.Math.sqrt;

public class OnlineVariance {
	
	private int n = 0;
	private double mean = 0.0;
	private double m2 = 0.0;

	public void add(double x) {
		// http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
		// check for online algorithm
		n++;
		double delta = x - mean;
		mean = mean + delta / n;
		m2 = m2 + delta * (x - mean);
	}
	
	public double mean(){
		return mean;
	}

	public double dev(){
		return dev(true);
	}
	
	public double dev(boolean sample) {
		return sqrt(var(sample));
	}

	public double var() {
		return var(true);
	}
	
	public double var( boolean sample) {
		if(sample){
			return m2 / (n - 1);
		}
		
		return m2 / (n);				
	}
}
