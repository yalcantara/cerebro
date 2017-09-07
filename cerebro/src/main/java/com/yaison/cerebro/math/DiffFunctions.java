package com.yaison.cerebro.math;

public enum DiffFunctions implements DiffFunc {
	SIGMOID(Functions.SIGMOID, Functions.SIGMOID_DX),
	TANH(Functions.TANH, Functions.TANH_DX),
	TANSIG(Functions.TANSIG, Functions.TANSIG_DX),
	LINEAR(Functions.LINEAR, Functions.LINEAR_DX);

	private final Func fx;
	private final Func dx;
	
	DiffFunctions(Func fx, Func dx){
		this.fx = fx;
		this.dx = dx;
	}
	
	@Override
	public Func fx() {
		return fx;
	}

	@Override
	public Func dx() {
		return dx;
	}
}
