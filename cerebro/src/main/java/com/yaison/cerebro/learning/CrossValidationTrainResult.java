package com.yaison.cerebro.learning;

public class CrossValidationTrainResult {
	
	
	private final double trainError;
	private final double crossError;
	private final double testError;
	
	private final int trainLength;
	private final int crossLength;
	private final int testLength;
	
	public CrossValidationTrainResult(double trainError, double crossError,
			double testError, int trainLength, int crossLength, int testLength) {
		super();
		this.trainError = trainError;
		this.crossError = crossError;
		this.testError = testError;
		this.trainLength = trainLength;
		this.crossLength = crossLength;
		this.testLength = testLength;
	}

	public double trainError() {
		return trainError;
	}

	public double crossError() {
		return crossError;
	}

	public double testError() {
		return testError;
	}

	public int trainLength() {
		return trainLength;
	}

	public int crossLength() {
		return crossLength;
	}

	public int testLength() {
		return testLength;
	}
	
	public double trainLengthPerc(){
		return trainLength() / (double)length();
	}
	
	public double crossLengthPerc(){
		return crossLength() / (double)length();
	}
	
	public double testLengthPerc(){
		return testLength() / (double)length();
	}
	
	public int length(){
		return trainLength() + crossLength() + testLength();
	}

	@Override
	public String toString() {
		return "CrossValidationTrainResult [trainError=" + trainError
				+ ", crossError=" + crossError + ", testError=" + testError
				+ "]";
	}
	
	
}
