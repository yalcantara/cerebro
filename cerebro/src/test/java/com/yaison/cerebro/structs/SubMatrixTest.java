package com.yaison.cerebro.structs;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;

public class SubMatrixTest {
	
	public SubMatrixTest() {
	}
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test() {
		Data data = DataSets.iris();
		
		Matrix m = data.matrix();
		
		Matrix setosa = m.selectRows(0, 50);
		test(setosa, 4);
		
		Matrix versicolor = m.selectRows(50, 100);
		test(versicolor, 5);
		
		Matrix virginica = m.selectRows(100, 150);
		test(virginica, 6);
	}
	
	void test(Matrix iris, int col){
		assertEquals(7, iris.columns());
		assertEquals(50, iris.rows());
		for(int i =0; i < 50; i++){
			assertEquals(1, iris.get(i, col), 0);
		}
	}
	
}
