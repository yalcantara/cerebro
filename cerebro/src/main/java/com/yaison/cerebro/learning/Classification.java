package com.yaison.cerebro.learning;

import com.yaison.cerebro.structs.Row;

public interface Classification {
	
	public static final double DEFAULT_THRESHOLD = 2.0 / 3.0;
	
	
	public String key();
	public double[] parameters();
	
	default public double[] tetha() {
		return parameters();
	}
	
	public double error();
	
	public double confidence(Object... record);
	public double confidence(Row row);
	
	default public Prediction predict(Object... record){
		return build(confidence(record));
	}
	
	default public Prediction predict(Row row){
		return build(confidence(row));
	}
	
	public static  Prediction build(final double confidence){
		Prediction p = new Prediction() {
			
			
			@Override
			public double confidence() {
				return confidence;
			}
			
		};
		
		return p;
	}
	
	
	
	
}
