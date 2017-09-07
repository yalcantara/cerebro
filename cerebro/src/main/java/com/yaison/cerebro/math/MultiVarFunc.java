package com.yaison.cerebro.math;

@FunctionalInterface
public interface MultiVarFunc {
	
	public double apply(double... vars);
}
