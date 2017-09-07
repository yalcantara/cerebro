package com.yaison.cerebro.learning;

import java.util.List;

import com.yaison.cerebro.algorithms.SupervisedAlgorithm;
import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.Row;

class ClassificationImpl extends MLProblem implements Classification {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1468508604343573326L;
	
	private final String key;
	ClassificationImpl(SupervisedAlgorithm algo, double error,
		List<ColumnHeader> featureHeaders,
		List<ComputedFeatureFunc> computedFeatures,
		ColumnHeader hipotesisHeader, String key) {
		super(algo, error, featureHeaders, computedFeatures, hipotesisHeader);
		this.key = key;
	}
	
	public String key(){
		return key;
	}

	public double confidence(Row row){
		double[] x = new double[converter.totalColumns()];
		
		converter.convert(row, x);
		
		return unnorm(algo.h(x));
	}
	
	@Override
	public double confidence(Object... record) {
		double[] x = new double[converter.totalColumns()];
		
		converter.convert(record, x);
		
		return unnorm(algo.h(x));
	}
	
}
