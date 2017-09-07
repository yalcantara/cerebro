package com.yaison.cerebro.learning;

import com.yaison.cerebro.structs.Row;

public interface Regression {
	
	public double error();
	public double[] parameters();
	
	default public double[] tetha(){
		return parameters();
	}
	
	public double predict(Row record);
	public double predict(Object... record);
	
}
