package com.yaison.cerebro.algorithms;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.Data;

public class KMeansTest {
	
	public KMeansTest() {
	}
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test() {
		Data data = DataSets.iris();
		
		double[][] start = { {  -0.01, 0, 0.5, 0.1 }, {  -1, 1, -1, -1 },
				{  1.1, 0.15, 2, 1 } };
		Matrix X = data.matrix().selectColumns(new int[]{0, 1, 2, 3});
		
		KMeans k = new KMeans(X, start);
		for (int i = 0; i <= 5; i++) {
			k.iterate(1);
		}
		
		assertEquals(56, k.count(0));
		assertEquals(50, k.count(1));
		assertEquals(44, k.count(2));
		assertEquals(0.9347, k.j(), 0.00009);
	}
	
}
