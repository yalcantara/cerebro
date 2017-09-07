package com.yaison.cerebro.structs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RowImpl implements Row {

	private final Data data;
	private final int idx;
	private final List<ColumnHeader> headers;
	private final List<Object> values;
	
	RowImpl(Data data, int idx){
		this.data = data;
		this.idx = idx;
		this.headers = data.header().list();
		
		List<Object> values = new ArrayList<>(columns());
		
		for(int i =0; i < columns(); i++){
			values.add(data.get(idx, i));
		}
		
		this.values = Collections.unmodifiableList(values);
	}

	
	@Override
	public List<ColumnHeader> headers() {
		return headers;
	}

	@Override
	public Object get(int col) {
		return values.get(col);
	}

	@Override
	public boolean isNumeric(int col) {
		return headers.get(col).isNumeric();
	}

	@Override
	public int columns() {
		return headers.size();
	}


	@Override
	public Data data() {
		return data;
	}


	@Override
	public int idx() {
		return idx;
	}

	@Override
	public List<Object> values() {
		return values;
	}
	
}
