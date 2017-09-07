package com.yaison.cerebro;

public interface DataInputStream extends AutoCloseable {

	public int columns();
	public boolean next();
	public double[] get();
	public double get(int col);
	
	public void close();
}
