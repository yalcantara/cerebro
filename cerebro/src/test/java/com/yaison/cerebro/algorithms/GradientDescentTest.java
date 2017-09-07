package com.yaison.cerebro.algorithms;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.Data;

public class GradientDescentTest{
	
	private MinSquareRegression reg;
	private GradientDescent gradient;
	
	public GradientDescentTest() {
	}
	
	@Before
	public void setUp() throws Exception {
		
		Data data = DataSets.ex1data1();
		Matrix x = data.matrix().selectColumn(0);
		Matrix y = data.matrix().selectColumn(1);
		
		reg = new MinSquareRegression(x, y);
		reg.setTheta(new double[]{-1, 1});
		gradient = new GradientDescent(reg);
		gradient.alpha(0.001);
	}
	
	@Test
	public void test() {
		
		double old = reg.j();
		for(int i =0; i < 100; i++){
			gradient.descend();
			double j = reg.j();
			
			
			
			assertTrue(j < old);
			old = j;
		}
		
	}
	
}
