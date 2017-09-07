package com.yaison.cerebro.descriptive;

import java.util.Collections;
import java.util.Iterator;

import com.yaison.cerebro.parsers.smart.TextColumnInfo;
import com.yaison.cerebro.structs.NumericMetaData;

public class DataInfo implements Iterable<NumericMetaData> {

	private final NumericMetaData[] meta;
	private final TextColumnInfo[] info;
	
	public DataInfo(NumericMetaData[] meta){
		this(meta, null);
	}
	
	public DataInfo(NumericMetaData[] meta, TextColumnInfo[] info) {
		super();
		this.meta = meta;
		this.info = info;
	}
	
	public int columns(){
		return meta.length;
	}
	
	public NumericMetaData meta(int idx){
		return meta[idx];
	}
	
	public TextColumnInfo info(int idx){
		return info[idx];
	}

	@Override
	public Iterator<NumericMetaData> iterator() {
		if(meta == null || meta.length == 0){
			return Collections.emptyIterator();
		}
		
		return new ArrayIterator<NumericMetaData>(meta);
	}
	
	private static final class ArrayIterator<E> implements Iterator<E>{

		final E[] arr;
		int crt;
		private  ArrayIterator(E[] arr) {
			this.arr = arr;
			crt = -1;
		}
		
		@Override
		public boolean hasNext() {
			return arr != null && (crt +1) < arr.length;
		}

		@Override
		public E next() {
			return arr[++crt];
		}
	}
}
