package com.yaison.cerebro.structs;

import java.util.Iterator;
import java.util.List;

public interface Row extends Iterable<Object> {
	
	public Data data();
	public int idx();
	public int columns();
	public List<ColumnHeader> headers();
	default ColumnHeader header(int col){
		return headers().get(col);
	}
	public Object get(int col);
	public List<Object> values();
	default public Iterator<Object> iterator(){
		return values().iterator();
	}
	public boolean isNumeric(int col);
}
