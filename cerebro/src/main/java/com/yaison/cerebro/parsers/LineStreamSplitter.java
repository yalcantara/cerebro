package com.yaison.cerebro.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.LineInputStream;

public class LineStreamSplitter implements AutoCloseable {
	
	private Pattern delimiter;
	private LineInputStream stream;
	private boolean started = false;
	
	private String crtLine;
	
	public LineStreamSplitter() {
		super();
	}
	
	public void stream(LineInputStream stream) {
		this.stream = stream;
	}
	
	public void delimiter(String regex) {
		delimiter(Pattern.compile(regex));
	}
	
	public void delimiter(Pattern regex) {
		this.delimiter = regex;
	}
	
	public boolean next(){
		if(started){
			stream.start();
		}
		
		String line;
		do{
			
			
			if(!stream.next()){
				crtLine = null;
				close();
				return false;
			}
			line = stream.line().trim();
		}while(line.isEmpty());
		
		crtLine = line;
		return true;
	}
	
	public int lineLength() {
		if (crtLine == null) {
			return 0;
		}
		return crtLine.length();
	}
	
	public boolean isLineEmpty() {
		return lineLength() == 0;
	}
	
	public String line() {
		return crtLine;
	}
	
	public int columns() {
		String line = stream.peek(1);
		
		if (line == null) {
			throw new CerebroException(
					"Could not determine the number of columns.");
		}
		
		return fields(line.trim());
	}
	
	public String[] split() {
		return split(crtLine);
	}
	
	public String[] split(String line) {
		String[] arr = delimiter.split(line);
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				arr[i] = arr[i].trim();
			}
		}
		
		return arr;
	}
	
	public void close() {
		if (stream == null) {
			return;
		}
		
		try {
			stream.close();
		} catch (Exception ex) {
			// ignored
		}
	}
	
	private int fields(String line){
		if(line == null || line == "" || line.equals("")){
			return 0;
		}
		
		int count = 1;
		Matcher m = delimiter.matcher(line);
		while(m.find()){
			count++;
		}
		
		return count;
	}
}
