package com.yaison.cerebro.structs;

import java.util.List;

import com.yaison.cerebro.CerebroException;

public interface DataMatrixMapper {
	
	public int dataColumns();
	
	public int matrixColumns();
	
	public List<Integer> toMatrixColumns(int[] dataCols);
	
	public int toMatrixColumn(int dataCol, String word);
	default public int toMatrixColumn(int dataCol) {
		List<Integer> list = toMatrixColumns(dataCol);
		if (list.isEmpty()) {
			throw new IllegalArgumentException("No maping for column "
					+ dataCol + ".");
		}
		
		if (list.size() > 1) {
			throw new CerebroException(
					"More than one mapping found for column "
							+ dataCol
							+ ". If you need multiple mappings, please use the plural "
							+ "version of this method, which is: toMatrixColumns(int).");
		}
		
		return list.get(0);
	}
	
	public List<Integer> toMatrixColumns(int dataCol);
	
	public int toDataColumn(int matrixCol);
}
