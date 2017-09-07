package com.yaison.cerebro.structs;

import java.io.Serializable;

@FunctionalInterface
public interface ComputedFeatureFunc extends Serializable {
	
	
	public double apply(double[] row);
}
