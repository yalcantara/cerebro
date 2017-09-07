package com.yaison.cerebro.learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.DataTransformer;
import com.yaison.cerebro.structs.GroupHeader;
import com.yaison.cerebro.structs.Row;

public class DataConverter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1040313291978565792L;
	
	
	// converts raw data into matrix data
	private final List<ColumnHeader> headers;
	private final double[] originalMatrixRow;
	private final List<ComputedFeatureFunc> computed;
	
	public DataConverter(List<ColumnHeader> headers, ComputedFeatureFunc... computed){
		this(headers, Arrays.asList(computed));
	}
	
	public DataConverter(List<ColumnHeader> headers, List<ComputedFeatureFunc> computed) {
		List<ColumnHeader> headersClone = new ArrayList<ColumnHeader>(headers.size());
		headersClone.addAll(headers);
		
		
		
		this.headers = Collections.unmodifiableList(headersClone);
		
		
		if(computed == null){
			this.computed = null;
		}else{
			List<ComputedFeatureFunc> computedClone = new ArrayList<ComputedFeatureFunc>(computed.size());
			computedClone.addAll(computed);
			this.computed = Collections.unmodifiableList(computedClone);
		}
		
		
		int n = toMatrix(headers.size());
		originalMatrixRow = new double[n];
	}
	
	public int originalMatrixColumns() {
		return originalMatrixRow.length;
	}
	
	public int computedMatrixColumns(){
		return computed == null? 0: computed.size();
	}
	
	public int totalColumns(){
		if(computed == null){
			return originalMatrixColumns();
		}
		
		return originalMatrixColumns() + computed.size();
	}
	
	public void convert(Row src, double[] dest) {
		clear(dest);
		final int n = headers.size();
		for (int i = 0; i < n; i++) {
			convert(i, src.get(i), dest);
		}
		fillRow(dest);
		setComputed(dest);
	}
	
	public void convert(Object[] src, double[] dest) {
		clear(dest);
		final int n = headers.size();
		for (int i = 0; i < n; i++) {
			convert(i, src[i], dest);
		}
		fillRow(dest);
		setComputed(dest);
	}
	
	private void setComputed(double[] dest){
		if(computed != null){
			int matrixColumns = originalMatrixColumns();
			for(int i =0; i < computed.size(); i++){
				dest[matrixColumns + i] = computed.get(i).apply(originalMatrixRow);
			}
		}
	}
	
	private void fillRow(double[] dest){
		for(int i =0; i < originalMatrixRow.length; i++){
			originalMatrixRow[i] = dest[i];
		}
	}
	
	public void convert(int srcIdx, Object srcVal, double[] dest) {
		GroupHeader gh = headers.get(srcIdx).groupHeader();
		
		if (gh.isNumeric()) {
			double val = ((Number) srcVal).doubleValue();
			
			DataTransformer dt = gh.transformer();
			int target = toMatrix(srcIdx);
			
			dest[target] = dt.normalize(val);
		} else {
			String val = (String) srcVal;
			
			int target = toMatrix(srcIdx, val);
			dest[target] = 1;
		}
	}
	
	private void clear(double[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = 0.0;
		}
	}
	
	private int toMatrix(int idx) {
		
		int mi = 0;
		for (int i = 0; i < idx; i++) {
			mi += headers.get(i).groupHeader().columns();
		}
		
		return mi;
	}
	
	private int toMatrix(int idx, String key) {
		
		int mi = 0;
		for (int i = 0; i < idx; i++) {
			mi += headers.get(i).groupHeader().columns();
		}
		
		mi += headers.get(idx).localIdx(key);
		return mi;
	}
}
