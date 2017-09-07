package com.yaison.cerebro.structs;

import com.yaison.cerebro.CerebroException;

public class GroupHeader {
	
	private final DataTransformer transformer;
	private final int columns;
	private final boolean numeric;
	
	GroupHeader(int columns, boolean numeric, DataTransformer transformer) {
		this.columns = columns;
		this.numeric = numeric;
		this.transformer = transformer;
	}

	public int columns() {
		return columns;
	}

	public boolean isNumeric() {
		return numeric;
	}
	
	public boolean isText(){
		return !numeric;
	}
	
	public DataTransformer transformer(){
		if(isNumeric()){
			return transformer;
		}
		
		throw new CerebroException(
			"Non-numeric column doesn't have a "
					+ "transformer. Please call isNumeric first to make "
					+ "sure the column is numeric.");
	}
	
}
