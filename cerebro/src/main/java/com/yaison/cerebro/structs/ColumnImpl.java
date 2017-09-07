package com.yaison.cerebro.structs;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.structs.primitives.BigArray;
import com.yaison.cerebro.structs.primitives.BigArrayType;

class ColumnImpl implements Column{
	
	
	private final NumericMetaData meta;
	private final DataTransformer trans;
	private final BigArrayType type;
	private final BigArray arr;
	
	ColumnImpl(NumericMetaData meta, BigArray arr) {
		this.meta = meta;
		this.arr = arr;
		type = arr.type();
		trans = new DataTransformer(meta, type);
	}

	public void setOriginal(int idx, double val) {
		if (val > meta.max() || val < meta.min()) {
			throw new IllegalArgumentException(
					"Invalid value for parameter val. The allowed range is ["
							+ meta.min() + ", " + meta.max() + "]. Got: " + val
							+ ".");
		}
		
		switch (type) {
			case BIT:
			case BYTE:
			case CHAR:
			case INT:
				arr.setInt(idx, trans.rescale(val));
			break;
			case FLOAT:
				arr.setDouble(idx, val);
			break;
			default:
				throw new CerebroException("Unsupported BigArray type: " + type
						+ ".");
		}
		
	}
	
	public NumericMetaData meta() {
		return meta;
	}
	
	
	public void swap(int from, int to){
		arr.swap(from, to);
	}
	
	public double get(int row) {
		double val = arr.get(row);
		
		
		switch (type) {
			case BIT:
				return val;
			case BYTE:
			case CHAR:
			case INT:
				val = trans.unscale(val);
				return trans.normalize(val);
			case FLOAT:
				return trans.normalize(val);
			default:
				throw new CerebroException("Unsupported BigArray type: " + type
						+ ".");
		}
		
		
		
	}
	
	public double getOriginal(int row) {
		double val = arr.get(row);
		
		
		
		switch (type) {
			case BIT:
			case BYTE:
			case CHAR:
			case INT:
				return trans.unscale(val);
			case FLOAT:
				return val;
			default:
				throw new CerebroException("Unsupported BigArray type: " + type
						+ ".");
		}
	}
	
	public int rows() {
		return meta.length();
	}


	@Override
	public DataTransformer transformer() {
		return trans;
	}

	@Override
	public BigArrayType type() {
		return type;
	}	
}
