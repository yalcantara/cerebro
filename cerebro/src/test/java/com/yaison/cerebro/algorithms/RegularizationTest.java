package com.yaison.cerebro.algorithms;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.math.Optimizer;
import com.yaison.cerebro.structs.Column;
import com.yaison.cerebro.structs.ComputedColumn;
import com.yaison.cerebro.structs.Data;
import com.yaison.cerebro.structs.DataImpl;


public class RegularizationTest {
	
	private static final double[] start = new double[] { 0.94, 0.36, -0.81,
			0.24, -0.29, -0.14, -0.14 };
	
	private Data data;
	private Matrix m;
	private Matrix y;
	
	
	@Before
	public void setUp() throws Exception {
		
		
		data = DataSets.ex2data2();
		Matrix x = data.matrix().selectColumns(new int[] { 0, 1 });
		y = data.matrix().selectColumn(2);
		
		Column c1 = new ComputedColumn(x, 0, (x1) -> x1);
		Column c2 = new ComputedColumn(x, 1, (x2) -> x2);
		
		Column c3 = new ComputedColumn(x, 0, (x1) -> pow(x1, 3));
		Column c4 = new ComputedColumn(x, 0, (x1) -> pow(x1, 2));
		
		Column c5 = new ComputedColumn(x, 1, (x2) -> pow(x2, 3));
		Column c6 = new ComputedColumn(x, 1, (x2) -> pow(x2, 2));
		
		m = DataImpl.createMatrix(new Column[]{c1, c2, c3, c4, c5, c6});
	}
	
	@Test
	public void logisticL2Test() {
		
		LogisticRegression reg = new LogisticRegression(m, y);
		reg.setTheta(start);
		
		reg.regularized(Regularization.NONE);
		double[] ans = Optimizer.optimize(reg, reg.cloneTheta());
		
		// The strategy here is that the L2 min prefers simpler solutions
		// (sparce vector) so smaller norms.
		
		double norm = norm(ans);
		
		reg.setTheta(start);
		reg.regularized(Regularization.L2);
		reg.regParameter(100);
		
		ans = Optimizer.optimize(reg, reg.cloneTheta());
		
		double l2norm = norm(ans);
		
		assertEquals(5.1802, norm, 0.00009);
		assertEquals(0.3730, l2norm, 0.00009);
	}
	
	public double norm(double[] t){
		double sum = 0.0;
		
		for(int i =0; i < t.length; i++){
			sum += pow(t[i], 2);
		}
		
		return sqrt(sum);
	}
	
	
	@Test
	public void logisticL1Test() {
		
		LogisticRegression reg = new LogisticRegression(m, y);
		reg.setTheta(start);
		reg.regularized(Regularization.L1);
		reg.regParameter(20);
		double[] ans = Optimizer.optimize(reg, reg.cloneTheta());
		reg.setTheta(ans);
		
		int sparce = 0;
		for (int i = 0; i < ans.length; i++) {
			if (abs(ans[i]) < 0.0001) {
				sparce++;
			}
		}
		
		double j = reg.j();
		assertEquals(4, sparce);
		assertEquals(0.6455, j, 0.00009);
	}
	
	@Test
	public void minSquareL2Test(){
		
		MinSquareRegression reg = new MinSquareRegression(m, y);
		reg.setTheta(start);
		
		reg.regularized(Regularization.NONE);
		double[] ans = Optimizer.optimize(reg, reg.cloneTheta());
		
		// The strategy here is that the L2 min prefers simpler solutions
		// (sparce vector) so smaller norms.
		
		double norm = norm(ans);
		
		reg.setTheta(start);
		reg.regularized(Regularization.L2);
		reg.regParameter(100);
		
		ans = Optimizer.optimize(reg, reg.cloneTheta());
		
		double l2norm = norm(ans);
		
		assertEquals(1.2501, norm, 0.00009);
		assertEquals(0.7449, l2norm, 0.0009);
	}
	
	
	@Test
	public void minSquareL1Test() {
		MinSquareRegression log = new MinSquareRegression(m, y);
		log.setTheta(start);
		log.regularized(Regularization.L1);
		log.regParameter(20);
		
		double[] ans = Optimizer.optimize(log, log.cloneTheta());
		
		int sparce = 0;
		for (int i = 0; i < ans.length; i++) {
			if (abs(ans[i]) < 0.0001) {
				sparce++;
			}
		}
		
		double j = log.j();
		assertEquals(sparce, 3);
		assertEquals(0.1170, j, 0.00009);
	}
	
}
