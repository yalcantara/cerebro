package com.yaison.cerebro;

public interface LineInputStream extends AutoCloseable {

	
	public void start();
	public String peek(int lines);
	public boolean next();
	public String line();
	
	public void close();
}
