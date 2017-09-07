package com.yaison.cerebro.structs.primitives;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class BigBitArrayTest {
	
	public BigBitArrayTest() {
	}
	
	@Before
	public void setUp() throws Exception {
	}
	
	private void doubleFailtTest(int idx, double val){
		BigBitArray arr = new BigBitArray(10);
		try{
			arr.setDouble(idx, val);
			throw new AssertionError();
		}catch(IllegalArgumentException ex){
			//ok
		}
	}
	
	private void intFailtTest(int idx, int val){
		BigBitArray arr = new BigBitArray(10);
		try{
			arr.setInt(idx, val);
			throw new AssertionError();
		}catch(IllegalArgumentException ex){
			//ok
		}
	}
	
	@Test
	public void failTest(){
		
		doubleFailtTest(0, 3);
		doubleFailtTest(2, -1);
		doubleFailtTest(0, 10);
		intFailtTest(0, 3);
		intFailtTest(0, -4);
		intFailtTest(0, 7);
	}
	
	
	@Test
	public void test() {
		BigBitArray arr = new BigBitArray(100_000_000);
		
		boolean[] p = new boolean[arr.length()];
		
		Random r = new Random();
		for (int i = 0; i < arr.length(); i++) {
			p[i] = r.nextBoolean();
			arr.set(i, p[i]);
		}
		
		for (int i = 0; i < p.length; i++) {
			double val = arr.get(i);
			boolean ref = p[i];
			
			if(ref){
				assertTrue(val == 1.0);
			}else{
				assertTrue(val == 0.0);
			}
		}
	}
	
}
