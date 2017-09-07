package com.yaison.cerebro.learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yaison.cerebro.algorithms.SupervisedAlgorithm;
import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.DataTransformer;

public abstract class MLProblem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4559908333654715599L;
	
	protected final SupervisedAlgorithm algo;
	protected final double error;
	protected final List<ColumnHeader> featureHeaders;
	protected final ColumnHeader hipotesisHeader;
	protected final DataConverter converter;
	
	protected MLProblem(SupervisedAlgorithm algo, double error,
			List<ColumnHeader> featureHeaders,
			List<ComputedFeatureFunc> computedFeatures,
			ColumnHeader hipotesisHeader) {
		converter = new DataConverter(featureHeaders, computedFeatures);
		this.algo = algo.model();
		this.error = error;
		this.featureHeaders = unmodifiable(featureHeaders);
		this.hipotesisHeader = hipotesisHeader;
		
	}
	
	private List<ColumnHeader> unmodifiable(List<ColumnHeader> featureHeaders) {
		List<ColumnHeader> arr = new ArrayList<ColumnHeader>(
				featureHeaders.size());
		arr.addAll(featureHeaders);
		return Collections.unmodifiableList(arr);
	}
	
	public double error() {
		return error;
	}
	
	public ColumnHeader feature(int idx) {
		return featureHeaders.get(idx);
	}
	
	protected double unnorm(double val) {
		
		DataTransformer trans = null;
		if (hipotesisHeader.groupHeader().isNumeric()) {
			trans = hipotesisHeader.groupHeader().transformer();
			if (trans == null) {
				return val;
			}
			
			return trans.unnorm(val);
		}
		
		return val;
	}
	
	protected double normalize(int idx, double val) {
		
		DataTransformer trans = feature(idx).groupHeader().transformer();
		if (trans == null) {
			return val;
		}
		
		return trans.normalize(val);
	}
	
	public double[] parameters() {
		return algo.cloneTheta();
	}
}
