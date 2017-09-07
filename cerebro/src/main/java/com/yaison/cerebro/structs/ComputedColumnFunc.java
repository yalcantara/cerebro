package com.yaison.cerebro.structs;

import com.yaison.cerebro.math.Matrix;

@FunctionalInterface
public interface ComputedColumnFunc {
	
	public double apply(Matrix m, int row);
}
