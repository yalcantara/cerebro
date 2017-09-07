package com.yaison.cerebro.learning;

import java.util.List;

import com.yaison.cerebro.algorithms.SupervisedAlgorithm;
import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.Row;

class RegressionImpl extends MLProblem implements Regression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1655061919429802315L;
	
	
	RegressionImpl(SupervisedAlgorithm algo, double error,
			List<ColumnHeader> featureHeaders,
			List<ComputedFeatureFunc> computedFeatures,
			ColumnHeader hipotesisHeader) {
		super(algo, error, featureHeaders, computedFeatures, hipotesisHeader);
	}
	
	public double predict(Row row) {
		double[] x = new double[converter.totalColumns()];
		
		converter.convert(row, x);
		
		return unnorm(algo.h(x));
	}
	
	public double predict(Object... record) {
		double[] x = new double[converter.totalColumns()];
		
		converter.convert(record, x);
		
		return unnorm(algo.h(x));
	}
}
