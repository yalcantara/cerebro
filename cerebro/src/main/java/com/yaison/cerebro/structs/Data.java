package com.yaison.cerebro.structs;

import com.yaison.cerebro.math.Matrix;

public interface Data {

	public Header header();

	public Data shuffle();

	default public ColumnHeader header(int col) {
		return header().select(col);
	}

	public int columns();

	public int rows();

	default public boolean isText(int col) {
		return header().isText(col);
	}

	default public boolean isNumeric(int col) {
		return header().isNumeric(col);
	}

	default public Row row(int idx) {
		return new RowImpl(this, idx);
	}

	default public Object[] get(int idx) {
		final int columns = columns();
		Object[] row = new Object[columns];

		for (int j = 0; j < columns; j++) {
			row[j] = get(idx, j);
		}

		return row;
	}

	public Object get(int row, int col);

	public Matrix matrix();

	default public void print() {
		print(10);
	}

	default public void print(int maxRows) {
		int rows = rows();
		int cols = columns();
		System.out.println("\nData");
		System.out.printf("rows:    %,10d\n", rows);
		System.out.printf("columns: %,10d\n\n", cols);

		maxRows = (maxRows < rows) ? maxRows : rows;

		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < cols; j++) {
				Object val = get(i, j);
				if (j > 0) {
					System.out.print(",  ");
				}

				if (val == null) {
					System.out.print("      (null)");
				} else if (val instanceof Double || val instanceof Float) {
					System.out.printf("%,12.4f", val);
				} else if (val instanceof Long || val instanceof Integer || val instanceof Short
						|| val instanceof Byte) {
					System.out.printf("%,12d", val);
				} else if (val instanceof String) {
					String s = (String) val;
					if (s.length() > 12) {
						s = s.substring(0, 12);
					} else {
						s = "            ".substring(0, 12 - s.length()) + s;
					}
					System.out.print(s);
				}

			}
			System.out.println();
		}
	}

}
