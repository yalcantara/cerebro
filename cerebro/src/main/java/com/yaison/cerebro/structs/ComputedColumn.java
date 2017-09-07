package com.yaison.cerebro.structs;

import com.yaison.cerebro.math.Func;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.primitives.BigArrayType;

public class ComputedColumn implements Column {
	
	private final Matrix source;
	private final ComputedColumnFunc func;
	
	public ComputedColumn(Matrix source, int col, Func func) {
		this.source = source;
		this.func = (m,i)->func.apply(m.get(i, col));
	}
	
	public ComputedColumn(Matrix source, ComputedColumnFunc func) {
		this.source = source;
		this.func = func;
	}
	
	@Override
	public void swap(int from, int to) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public NumericMetaData meta() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setOriginal(int idx, double val) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BigArrayType type() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double get(int row) {
		return func.apply(source, row);
	}
	
	@Override
	public double getOriginal(int row) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DataTransformer transformer() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int rows() {
		return source.rows();
	}
}
