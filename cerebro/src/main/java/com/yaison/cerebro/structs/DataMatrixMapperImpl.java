package com.yaison.cerebro.structs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMatrixMapperImpl implements DataMatrixMapper {
	
	private final WeakReference<ColumnHeader[]> headers;
	private final Map<Integer, List<Integer>> dataToMatrix;
	private final Map<Integer, Integer> matrixToData;
	
	public DataMatrixMapperImpl(ColumnHeader[] headers) {
		super();
		Map<Integer, List<Integer>> dm = new HashMap<>();
		Map<Integer, Integer> md = new HashMap<>();
		
		int matrixIdx = 0;
		for (int i = 0; i < headers.length; i++) {
			List<Integer> arr = new ArrayList<Integer>();
			
			ColumnHeader h = headers[i];
			int groupColumns = h.groupHeader().columns();
			for (int j = 0; j < groupColumns; j++) {
				arr.add(matrixIdx);
				
				md.put(matrixIdx, i);
				matrixIdx++;
			}
			
			dm.put(i, Collections.unmodifiableList(arr));
		}
		
		this.dataToMatrix = Collections.unmodifiableMap(dm);
		this.matrixToData = Collections.unmodifiableMap(md);
		this.headers = new WeakReference<ColumnHeader[]>(headers);
	}
	
	private ColumnHeader header(int idx){
		return headers.get()[idx];
	}
	
	@Override
	public int dataColumns() {
		return dataToMatrix.size();
	}
	
	@Override
	public int matrixColumns() {
		return matrixToData.size();
	}
	
	public int toMatrixColumn(int dataCol, String word) {
		int matrixIdx = 0;
		for (int i = 0; i < dataCol; i++) {
			ColumnHeader h = header(i);
			matrixIdx += h.groupHeader().columns();
		}
		
		return matrixIdx + header(dataCol).localIdx(word);
	}
	
	public List<Integer> toMatrixColumns(int[] dataCols) {
		List<Integer> arr = new ArrayList<>();
		
		for (int i = 0; i < dataCols.length; i++) {
			arr.addAll(toMatrixColumns(dataCols[i]));
		}
		
		return arr;
	}
	
	@Override
	public List<Integer> toMatrixColumns(int dataCol) {
		return dataToMatrix.get(dataCol);
	}
	
	@Override
	public int toDataColumn(int matrixCol) {
		return matrixToData.get(matrixCol);
	}
	
}
