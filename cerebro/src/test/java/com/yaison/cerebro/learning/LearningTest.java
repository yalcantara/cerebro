package com.yaison.cerebro.learning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.structs.Data;

public class LearningTest  {
	
	
	private Data ex1data1;
	
	
	public LearningTest() {
		super();
	}
	
	
	@Before
	public void setup(){
		ex1data1 = DataSets.ex1data1();
	}
	
	@Test
	public void testRegress() {
		Regression reg = Learning.regress(ex1data1, 1);
		assertTrue(reg.error() <= 0.15);
		
		for(int i =0; i < ex1data1.rows(); i++){
			double x = (Double)ex1data1.get(i, 0);
			double h = ex1data1Hipotesis(x);
			double p = reg.predict(x);
			assertEquals(h, p, 0.9);
		}
	}
	
	static double ex1data1Hipotesis(double x){
		//for file linear-regression/ext1data1.txt
		return -3.8957808783118786 + 1.1930336441895961 * x;
	}
	
}
