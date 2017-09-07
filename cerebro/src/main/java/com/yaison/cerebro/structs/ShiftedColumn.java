package com.yaison.cerebro.structs;

import com.yaison.cerebro.structs.primitives.BigArrayType;

class ShiftedColumn implements Column {
	
	private final int[] shift;
	private final Column column;
	
	ShiftedColumn(int[] shift, Column column) {
		
		if (shift.length != column.rows()) {
			throw new IllegalArgumentException(
					"The length of the shift parameter must match the "
							+ "number of rows of the column parameter. Got: "
							+ shift.length + ", expected: " + column.rows()
							+ ".");
		}
		
		
		
		this.shift = shift;
		this.column = column;
	}
	
	Column original(){
		return column;
	}
	
	@Override
	public void swap(int from, int to) {
		column.swap(shift[from], shift[to]);
	}
	
	@Override
	public NumericMetaData meta() {
		return column.meta();
	}
	
	@Override
	public void setOriginal(int idx, double val) {
		column.setOriginal(shift[idx], val);
	}
	
	@Override
	public BigArrayType type() {
		return column.type();
	}
	
	@Override
	public double get(int row) {
		return column.get(shift[row]);
	}
	
	@Override
	public double getOriginal(int row) {
		return column.getOriginal(shift[row]);
	}
	
	@Override
	public DataTransformer transformer() {
		return column.transformer();
	}
	
	@Override
	public int rows() {
		return column.rows();
	}
}
