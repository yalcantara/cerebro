package com.yaison.cerebro.structs.primitives;

public interface BigArray {
	
	
	public static void checkInt(double val) {
		if (Double.isNaN(val)) {
			throw new IllegalArgumentException("The val parameter is NaN.");
		}
		
		if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Out of bounds of an int. Got: "
					+ val + ".");
		}
		
		if (((int) val) != val) {
			throw new IllegalArgumentException(
					"The val parameter is not a discrete value. Got: " + val
							+ ".");
		}
	}
	
	public int length();
	
	default public void swap(int fromIdx, int toIdx) {
		final int l = length();
		if (fromIdx < 0 || toIdx < 0 || fromIdx >= l || toIdx >= l) {
			throw new IndexOutOfBoundsException(
					"Out of bound index for array length: " + l
							+ ". Got fromIdx: " + fromIdx + ", toIdx: " + toIdx
							+ ".");
		}
		
		double fromVal = get(fromIdx);
		double toVal = get(toIdx);
		setDouble(toIdx, fromVal);
		setDouble(fromIdx, toVal);
	}
	
	
	public double get(int idx);
	public void setDouble(int idx, double val);
	public void setInt(int idx, int val);
	public BigArrayType type();
	
}
