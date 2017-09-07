package com.yaison.cerebro.descriptive;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.descriptive.OnlineVariance;
import com.yaison.cerebro.structs.NumericMetaData;

public class NumericMetaDataBuilder {
	
	private double min = Double.NaN;
	private double max = Double.NaN;
	private int n;
	private final OnlineVariance var;
	private boolean discrete = true;
	private boolean built = false;
	private String name = null;

	public NumericMetaDataBuilder() {
		var = new OnlineVariance();
	}

	public void name(String name) {
		this.name = name;
	}



	public NumericMetaData build() {
		checkBuilt();
		if (n < 1) {
			throw new CerebroException("No data was added.");
		}
		built = true;
		return new NumericMetaData(name, var.var(), var.mean(), max, min,
				discrete, n);
	}

	
	
	private void checkBuilt(){
		if (built) {
			throw new CerebroException(
					"The meta data was already built, thus can not add more data.");
		}
	}

	public void add(double val) {
		checkBuilt();
		
		n++;
		
		if (((long)val) != val) {
			discrete = false;
		}
		
		if (Double.isNaN(max) || val > max) {
			max = val;
		}

		if (Double.isNaN(min) || val < min) {
			min = val;
		}

		var.add(val);
	}
}
