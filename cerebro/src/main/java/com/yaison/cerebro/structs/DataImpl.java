package com.yaison.cerebro.structs;

import com.yaison.cerebro.math.Matrix;

public class DataImpl implements Data {
	
	private final Header header;
	private final ColumnGroup[] cluster;
	
	public DataImpl(Header header, ColumnGroup[] cluster) {
		this.header = header;
		this.cluster = cluster;
	}
	
	public Data shuffle() {
		ColumnGroup[] newCluster = new ColumnGroup[cluster.length];
		
		int[] shift = StructUtils.randomIndices(rows());
		for (int i = 0; i < newCluster.length; i++) {
			ColumnGroup g = cluster[i];
			
			Column[] newColumns = new Column[g.columns()];
			for (int j = 0; j < newColumns.length; j++) {
				Column col = g.column(j);
				if (col instanceof ShiftedColumn) {
					// Since a column can be also a ShiftedColumn, we may end up
					// with many ShiftedColumn referencing other ShiftedColumn
					// if
					// random() is called many times. So, to prevent that, let's
					// use
					// the original Data's column.
					newColumns[j] = new ShiftedColumn(shift,
							((ShiftedColumn) col).original());
				} else {
					newColumns[j] = new ShiftedColumn(shift, col);
				}
			}
			
			newCluster[i] = new ColumnGroup(g.header(), newColumns);
		}
		
		return new DataImpl(header, newCluster);
	}
	
	@Override
	public Header header() {
		return header;
	}
	
	public Object get(int row, int column) {
		ColumnHeader h = header.get(column);
		if (h.isNumeric()) {
			// Original numeric columns are at first in the cluster.
			return cluster[column].column(0).getOriginal(row);
		}
		
		int n = cluster[column].columns();
		for (int i = 0; i < n; i++) {
			Column c = cluster[column].column(i);
			if (c.get(row) == 1) {
				return h.text(i);
			}
		}
		
		return null;
	}
	
	@Override
	public int columns() {
		return header.columns();
	}
	
	@Override
	public int rows() {
		return header.rows();
	}
	
	@Override
	public Matrix matrix() {
		return createMatrix(cluster);
	}
	
	public static Matrix createMatrix(Column[] columns) {
		int m = columns[0].rows();
		int total = columns.length;
		
		Matrix mtr = Matrix.create(m, total);
		for (int i = 0; i < m; i++) {
			
			for (int j = 0; j < total; j++) {
				
				mtr.set(i, j, columns[j].get(i));
			}
			
		}
		return mtr;
	}
	
	public static Matrix createMatrix(ColumnGroup[] cluster) {
		int m = cluster[0].column(0).rows();
		int total = totalColumns(cluster);
		
		Matrix mtr = Matrix.create(m, total);
		for (int i = 0; i < m; i++) {
			int j = 0;
			for (int g = 0; g < cluster.length; g++) {
				ColumnGroup group = cluster[g];
				for (int k = 0; k < group.columns(); k++) {
					
					mtr.set(i, j, group.column(k).get(i));
					j++;
				}
			}
		}
		return mtr;
	}
	
	private static int totalColumns(ColumnGroup[] cluster) {
		int sum = 0;
		
		for (int i = 0; i < cluster.length; i++) {
			sum += cluster[i].columns();
		}
		
		return sum;
	}
}
