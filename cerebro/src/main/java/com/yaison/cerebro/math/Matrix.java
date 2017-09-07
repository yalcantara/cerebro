package com.yaison.cerebro.math;

import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sqrt;

import java.util.List;

public interface Matrix {
	
	public static Matrix create(int m, int n) {
		return new MatrixImpl(m, n);
	}
	
	public static Matrix create(int d) {
		return create(d, d);
	}
	
	public static Matrix I(int d) {
		Matrix I = create(d, d);
		
		for (int i = 0; i < d; i++) {
			I.set(i, i, 1);
		}
		
		return I;
	}
	
	public static Matrix rand(int m, int n) {
		
		Matrix mtr = create(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, -1 + 2 * random());
			}
		}
		return mtr;
	}
	
	public static Matrix from(double[][] arr) {
		int m = arr.length;
		int n = arr[0].length;
		
		Matrix mtr = create(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, arr[i][j]);
			}
		}
		return mtr;
	}
	
	public double get(int row, int column);
	
	public void set(int row, int col, double val);
	
	public int m();
	
	public int n();
	
	default public int size() {
		return m() * n();
	}
	
	default public int columns() {
		return n();
	}
	
	default public int rows() {
		return m();
	}
	
	default public double[] toArray(int row) {
		double[] arr = new double[columns()];
		
		final int n = n();
		for (int j = 0; j < n; j++) {
			arr[j] = get(row, j);
		}
		
		return arr;
	}
	
	default public double[][] toArray() {
		double[][] arr = new double[rows()][columns()];
		
		final int m = m();
		final int n = n();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				arr[i][j] = get(i, j);
			}
		}
		
		return arr;
	}
	
	default public double[] toSingleArray() {
		final int m = m();
		final int n = n();
		double[] arr = new double[m * n];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				arr[i * n + j] = get(i, j);
			}
		}
		
		return arr;
	}
	
	default public double[] toColumnArray(int idx) {
		double[] arr = new double[rows()];
		
		final int m = m();
		for (int i = 0; i < m; i++) {
			arr[i] = get(i, idx);
		}
		
		return arr;
	}
	
	default public Vector diag() {
		int d = min(m(), n());
		Vector v = Vector.create(d);
		for (int i = 0; i < d; i++) {
			v.set(i, get(i, i));
		}
		
		return v;
	}
	
	default public void setDiag(double val) {
		int d = min(m(), n());
		
		for (int i = 0; i < d; i++) {
			set(i, i, val);
		}
		
	}
	
	default public Vector getRow(int r) {
		int n = n();
		Vector v = Vector.create(n);
		for (int j = 0; j < n; j++) {
			v.set(j, get(r, j));
		}
		
		return v;
	}
	
	default public double colNorm(int col) {
		int m = m();
		double sum = 0.0;
		for (int i = 0; i < m; i++) {
			sum += pow(get(i, col), 2);
		}
		
		return sqrt(sum);
	}
	
	default public void setRow(int row, Vector v) {
		int n = n();
		if (v.size() != n) {
			throw new LinearAlgebraException("The length of the vector must be " + n
					+ ", but instead got " + v.size() + ".");
		}
		
		for (int j = 0; j < n; j++) {
			set(row, j, v.get(j));
		}
	}
	
	default public void setColumn(int col, Vector v) {
		int m = m();
		if (v.size() != m) {
			throw new LinearAlgebraException("The length of the vector must be " + m
					+ ", but instead got " + v.size() + ".");
		}
		
		for (int i = 0; i < m; i++) {
			set(i, col, v.get(i));
		}
	}
	
	default public Vector getColumn(int c) {
		int m = m();
		Vector v = Vector.create(m);
		for (int i = 0; i < m; i++) {
			v.set(i, get(i, c));
		}
		
		return v;
	}
	
	default public Matrix apply(Func func) {
		int m = m();
		int n = n();
		
		Matrix dest = Matrix.create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				dest.set(i, j, func.apply(get(i, j)));
			}
		}
		
		return dest;
	}
	
	default public void copyRow(int srcRow, Matrix dest, int destRow) {
		int n = n();
		for (int j = 0; j < n; j++) {
			dest.set(destRow, j, get(srcRow, j));
		}
	}
	
	default public Matrix selectRows(int start, int end) {
		return selectRows(start, end, false);
	}
	
	default public Matrix selectRows(int start, int end, boolean roll) {
		int m = m();
		int n = n();
		int l = end - start;
		
		if (l < 1) {
			throw new IllegalArgumentException(
					"The length of the selection must be 1 or higher, but instead got: " + l + ".");
		}
		
		Matrix mtr;
		if (roll) {
			mtr = Matrix.create(l, n);
			start = start % m;
			
			int crtIdx = start;
			for (int i = 0; i < l; i++) {
				copyRow(crtIdx, mtr, i);
				if (crtIdx >= m - 1) {
					crtIdx = 0;
				}
			}
		} else {
			
			if (l > m) {
				throw new IllegalArgumentException(
						"For roll=false the length of the selection can not be higher than this matrix number of rows. Number of rows: "
								+ m + ", selection length: " + l + ".");
			}
			
			if (end > m) {
				throw new IllegalArgumentException(
						"For roll=false the end parameter must be less or equals than the number of rows. Number of rows: "
								+ m + ", end parameter: " + m + ".");
			}
			
			mtr = Matrix.create(l, n);
			for (int i = 0; i < l; i++) {
				copyRow(start + i, mtr, i);
			}
		}
		
		return mtr;
	}
	
	default public Matrix selectColumn(int column) {
		return selectColumns(new int[] { column });
	}
	
	default public Matrix selectColumns(List<Integer> columns) {
		int[] arr = new int[columns.size()];
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = columns.get(i);
		}
		
		return selectColumns(arr);
	}
	
	default public double colMin(int col) {
		final int rows = rows();
		double min = get(0, col);
		for (int i = 1; i < rows; i++) {
			min = Math.min(min, get(i, col));
		}
		
		return min;
	}
	
	default public double colMax(int col) {
		final int rows = rows();
		double max = get(0, col);
		for (int i = 0; i < rows; i++) {
			max = Math.max(max, get(i, col));
		}
		
		return max;
	}
	
	default public Matrix selectColumns(int[] columns) {
		int m = m();
		int l = columns.length;
		
		Matrix dest = create(m, l);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < l; j++) {
				dest.set(i, j, get(i, columns[j]));
			}
		}
		
		return dest;
	}
	
	default public Matrix removeColumn(int col) {
		
		int m = m();
		int n = n() - 1;
		Matrix dest = create(m, n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				double val;
				if (j < col) {
					val = get(i, j);
				} else if (j > col) {
					val = get(i, j + 1);
				} else {
					continue;
				}
				dest.set(i, j, val);
			}
		}
		
		return dest;
	}
	
	default public Matrix selectColumns(int start, int end) {
		int m = m();
		int l = end - start;
		
		Matrix dest = create(m, l);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < l; j++) {
				dest.set(i, j, get(i, start + j));
			}
		}
		
		return dest;
	}
	
	default public void set(double[] values) {
		int m = m();
		int n = n();
		
		if (size() != values.length) {
			throw new IllegalArgumentException("Invalid number of values. Expected: " + size()
					+ ", but instead got: " + values.length + ".");
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, values[i * n + j]);
			}
		}
	}
	
	default public void rand() {
		rand(-1, 1);
	}
	
	default public void rand(double min, double max) {
		int m = m();
		int n = n();
		double diff = max - min;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, min + diff * random());
			}
		}
	}
	
	default public Matrix add(Matrix b) {
		int m = m();
		int n = n();
		int om = b.m();
		int on = b.n();
		if (m != om || n != on) {
			throw new LinearAlgebraException("Matrix dimensions must be the same. This " + m + "x"
					+ n + ", other " + om + "x" + on);
		}
		
		Matrix c = create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) + b.get(i, j));
			}
		}
		
		return c;
	}
	
	default public Matrix subtract(Matrix b) {
		int m = m();
		int n = n();
		int om = b.m();
		int on = b.n();
		if (m != om || n != on) {
			throw new LinearAlgebraException("Matrix dimensions must be the same. This " + m + "x"
					+ n + ", other " + om + "x" + on);
		}
		
		Matrix c = create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c.set(i, j, get(i, j) - b.get(i, j));
			}
		}
		
		return c;
	}
	
	default public Matrix mult(Matrix b) {
		int m = m();
		int n = n();
		int om = b.m();
		int on = b.n();
		
		if (n != om) {
			throw new LinearAlgebraException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", other " + om + "x" + on + ".");
		}
		
		Matrix c = create(m, on);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < on; j++) {
				double sum = 0.0;
				for (int k = 0; k < n; k++) {
					sum += get(i, k) * b.get(k, j);
				}
				c.set(i, j, sum);
			}
		}
		
		return c;
	}
	
	default Vector mult(Vector x) {
		int m = m();
		int n = n();
		int l = x.size();
		
		if (n != l) {
			throw new LinearAlgebraException("Invalid matrix dimension for multiplication. This "
					+ m + "x" + n + ", vector length " + l + ".");
		}
		
		Vector b = Vector.create(m);
		for (int i = 0; i < m; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++) {
				sum += get(i, j) * x.get(j);
			}
			b.set(i, sum);
		}
		
		return b;
	}
	
	default public Matrix trans() {
		int m = m();
		int n = n();
		Matrix mtr = create(n, m);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(j, i, get(i, j));
			}
		}
		return mtr;
	}
	
	default public Matrix elemult(Matrix b) {
		int m = m();
		int n = n();
		int om = b.m();
		int on = b.n();
		
		if (n != on || m != om) {
			throw new LinearAlgebraException(
					"Invalid matrix dimension for element-wise multiplication. Expected the same dimension for both matrices, but instead go: this "
							+ m + "x" + n + ", other " + om + "x" + on + ".");
		}
		
		Matrix c = create(m, on);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < on; j++) {
				c.set(i, j, get(i, j) * b.get(i, j));
			}
		}
		
		return c;
	}
	
	default public Matrix scale(double scalar) {
		int m = m();
		int n = n();
		Matrix mtr = create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, get(i, j) * scalar);
			}
		}
		return mtr;
	}
	
	default public Matrix elepow(int exp) {
		int m = m();
		int n = n();
		Matrix mtr = create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, Math.pow(get(i, j), exp));
			}
		}
		return mtr;
	}
	
	default public double sum() {
		int m = m();
		int n = n();
		
		double sum = 0.0;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += get(i, j);
			}
		}
		
		return sum;
	}
	
	default public Matrix insertCol(int colIdx, double value) {
		int m = m();
		int n = n();
		
		Matrix mtr = Matrix.create(m, n + 1);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (j >= colIdx) {
					mtr.set(i, j + 1, get(i, j));
				} else {
					mtr.set(i, j, get(i, j));
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			mtr.set(i, colIdx, value);
		}
		
		return mtr;
	}
	
	default public Matrix insertCol(int colIdx, Vector newColumn) {
		int m = m();
		int n = n();
		
		Matrix mtr = Matrix.create(m, n + 1);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (j >= colIdx) {
					mtr.set(i, j + 1, get(i, j));
				} else {
					mtr.set(i, j, get(i, j));
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			mtr.set(i, colIdx, newColumn.get(i));
		}
		
		return mtr;
	}
	
	default public Matrix echelon() {
		
		int m = m();
		int n = n();
		Matrix b = clone();
		
		for (int j = 0; j < n; j++) {
			double scalar = b.get(j, j);
			if (scalar == 0) {
				continue;
			}
			Vector r = b.getRow(j).scale(1.0 / scalar);
			// this is our intention, and to prevent catastrophic cancellation
			r.set(j, 1);
			b.setRow(j, r);
			
			for (int i = j + 1; i < m; i++) {
				scalar = b.get(i, j);
				Vector r2 = b.getRow(i).subtract(r.scale(scalar));
				r2.set(j, 0);
				b.setRow(i, r2);
			}
		}
		
		return b;
	}
	
	default public Vector solve() {
		
		Matrix e = echelon();
		int d = e.n();
		Vector v = Vector.create(d);
		
		double xn = e.get(d - 1, d - 1);
		v.set(d - 1, xn);
		for (int i = d - 2; i >= 0; i--) {
			xn = 0.0;
			for (int j = i + 1; j < d; j++) {
				xn -= e.get(i, j) * v.get(j);
			}
			
			v.set(i, xn);
		}
		
		return v;
	}
	
	default public Matrix qr() {
		
		Matrix q = ortho();
		int m = q.m();
		int n = q.n();
		
		for (int j = 0; j < n; j++) {
			double norm = q.colNorm(j);
			for (int i = 0; i < m; i++) {
				q.set(i, j, q.get(i, j) / norm);
			}
		}
		
		return q;
	}
	
	default public Matrix ortho() {
		
		// Gram-Schmidt
		// https://en.wikipedia.org/wiki/Gram-Schmidt_process
		int m = m();
		int n = n();
		Matrix u = Matrix.create(m, n);
		
		for (int k = 0; k < n; k++) {
			Vector vk = getColumn(k);
			
			Vector sum = Vector.create(m);
			for (int j = 0; j < k; j++) {
				Vector uj = u.getColumn(j);
				sum = sum.add(uj.proj(vk));
			}
			
			Vector uk = vk.subtract(sum);
			u.setColumn(k, uk);
		}
		
		return u;
	}
	
	default public Matrix eigVal() {
		Matrix a = this;
		Matrix q = a.qr();
		int iter = 100;
		for (int i = 0; i < iter; i++) {
			a = q.trans().mult(a).mult(q);
			if (i + 1 < iter) {
				q = a.qr();
			}
		}
		return a;
	}
	
	default public Tuple<Matrix, Vector> eig() {
		Vector eigVals = eigVal().diag();
		int s = eigVals.size();
		
		int n = n();
		Matrix I = Matrix.I(n);
		
		Matrix eigVectors = Matrix.create(n, s);
		for (int i = 0; i < s; i++) {
			I.setDiag(1);
			
			double eigVal = eigVals.get(i);
			Matrix diff = subtract(I.scale(eigVal));
			
			Vector eigVec = diff.solve().unit();
			eigVectors.setColumn(i, eigVec);
		}
		
		return new Tuple<>(eigVectors, eigVals);
	}
	
	default public Matrix clone() {
		int m = m();
		int n = n();
		Matrix mtr = create(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				mtr.set(i, j, get(i, j));
			}
		}
		return mtr;
	}
	
	default public void print() {
		print(m());
	}
	
	default public void print(int maxRows) {
		int m = m();
		int n = n();
		maxRows = (maxRows < m) ? maxRows : m;
		
		if (maxRows < m) {
			System.out.println("Matrix " + m + "x" + n + "  (truncated)");
		} else {
			System.out.println("Matrix " + m + "x" + n);
		}
		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < n; j++) {
				if (j > 0) {
					System.out.print("  ");
				}
				System.out.printf("%12.4f", get(i, j));
			}
			System.out.println();
			
		}
	}
}
