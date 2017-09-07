package com.yaison.cerebro.math;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.tan;

public class Functions {
	
	public static final Func LINEAR = (x) -> x;
	public static final Func LINEAR_DX = (x) -> 1;
	public static final Func TANH = Math::tanh;
	public static final Func TANH_DX = (x) -> 1 - pow(tan(x), 2);
	public static final Func TANSIG = (x) -> 2 / (1 + exp(-2 * x)) - 1;
	public static final Func TANSIG_DX = (x) -> 4 * exp(2 * x)
			/ pow(1 + exp(2 * x), 2);
	public static final Func SIGMOID = (x) -> 1 / (1 + exp(-x));
	public static final Func SIGMOID_DX = (x) -> exp(x) / pow(1 + exp(x), 2);
}
