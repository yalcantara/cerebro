package com.yaison.cerebro.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TextFileReaderTest  {
	
	private TextFileReader reader;
	
	public TextFileReaderTest() {
	}
	
	@Before
	public void setUp() throws Exception {
		reader = new TextFileReader("test-files/iris.data");
	}
	
	@Test
	public void test() {
		reader.start();
		int lines = 0;
		StringBuilder sb = new StringBuilder();
		if(reader.next()){
			sb.append(reader.line());
			lines++;
			while(reader.next()){
				sb.append("\n");
				sb.append(reader.line());
				lines++;
			}
		}
		String data = sb.toString();
		
		
		assertEquals(50, count(data, "Iris-setosa"));
		assertEquals(50, count(data, "Iris-versicolor"));
		assertEquals(50, count(data, "Iris-virginica"));
		assertEquals(600, count(data, ","));
		assertEquals(150, lines);
	}
	
	private int count(String src, String key) {
		int sum = 0;
		int s = -1;
		
		do {
			s = src.indexOf(key, s+1);
			if(s >= 0){
				sum++;
			}
		} while (s >= 0);
		
		return sum;
	}
}
