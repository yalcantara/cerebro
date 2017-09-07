package com.yaison.cerebro.structs;

import com.yaison.cerebro.parsers.smart.TextColumnInfo;

public class ColumnGroup {
	
	private final GroupHeader header;
	private final Column[] columns;
	
	ColumnGroup(GroupHeader header, Column[] columns){
		this.header = header;
		this.columns = columns;
	}
	
	public ColumnGroup(TextColumnInfo info) {
		
		if (info.isAllNumbers()) {
			columns = new Column[] {ColumnFactory.create(info.meta())};
			header = new GroupHeader(1, info.isAllNumbers(), columns[0].transformer());
		} else if (info.isAllWords()) {
			int count = info.diffWords();
			header = new GroupHeader(count, info.isAllNumbers(), null);
			
			int length = info.length();
			
			columns = new Column[count];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = ColumnFactory.binary(length);
			}
		} else {
			throw new IllegalArgumentException("Invalid column at idx "
					+ info.getColumnIdx()
					+ ". The column must be either full numeric "
					+ "or full text. Got: " + info + ".");
		}
	}
	
	public void setOriginal(int row, double val) {
		columns[0].setOriginal(row, val);
	}
	
	public DataTransformer tranformer() {
		return header.transformer();
	}
	
	public GroupHeader header(){
		return header;
	}
	
	public boolean isNumeric() {
		return header().isNumeric();
	}
	
	public Column column(int idx) {
		return columns[idx];
	}
	
	public int columns() {
		return columns.length;
	}
	
	
}
