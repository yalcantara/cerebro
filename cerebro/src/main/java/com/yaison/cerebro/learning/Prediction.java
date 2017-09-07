package com.yaison.cerebro.learning;


public interface Prediction {
	
	public static final double DEFAULT_THRESHOLD = 2.0 / 3.0;
	
	default public boolean answer(){
		return confident(DEFAULT_THRESHOLD);
	}
	public double confidence();
	
	default public int confidencePerc(){
		return (int)Math.round(confidence()* 100);
	}
	
	default public boolean confident(double threshold){
		return confidence() >= threshold;
	}
}
