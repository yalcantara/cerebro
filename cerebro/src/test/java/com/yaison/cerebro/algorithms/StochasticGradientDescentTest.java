package com.yaison.cerebro.algorithms;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.Data;

public class StochasticGradientDescentTest  {
	
	
	
	public StochasticGradientDescentTest() {
	}
	

	
	@Test
	public void test1() {
		
		Data data = DataSets.ex1data1();
		data = data.shuffle();
		Matrix x = data.matrix().selectColumn(0);
		Matrix y = data.matrix().selectColumn(1);
		
		SupervisedAlgorithm algo = new MinSquareRegression(x, y);
		algo.setTheta(new double[] { -3, 0.8 });
		
		double j = algo.j();
		assertEquals(4.6481, j, 0.00009);
		
		
		StochasticGradientDescent grad = new StochasticGradientDescent(algo);
		grad.alpha(0.01);
		grad.descend(1000000);
		j = algo.j();
		assertEquals(0.1474, j, 0.009);
		
		
	}
	
	@Test
	public void test2(){
		Data data = DataSets.housing();
		Matrix x = data.matrix().selectColumns(
			new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
		Matrix y = data.matrix().selectColumn(13);
		
		MinSquareRegression algo = new MinSquareRegression(x, y);
		double[] start = { 0.1, 0.6, -0.1, -0.0, -0.7, 0.2, -0.5, -0.8, 0.8,
				-0.6, 0.7, 0.3, -0.8, 0.0 };
		algo.setTheta(start);
		
		int m = algo.m();
		StochasticGradientDescent grad = new StochasticGradientDescent(algo);
		grad.alpha(0.01);
		
		algo.setTheta(start);
		grad.descend(m);
		assertEquals(0.3394, algo.j(), 0.0009); // <- less precision here
		
		algo.setTheta(start);
		grad.descend(m*10);
		assertEquals(0.1662, algo.j(), 0.0009); // <- less precision here
		
		grad.alpha(grad.alpha() / 10);
		grad.descend(m*10);
		assertEquals(0.1317, algo.j(), 0.0009);
		
		grad.alpha(grad.alpha() / 10);
		grad.descend(m*10);
		assertEquals(0.1310, algo.j(), 0.0009);
	}
	
}
