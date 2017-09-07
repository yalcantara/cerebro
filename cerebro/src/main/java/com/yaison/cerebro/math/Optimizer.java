package com.yaison.cerebro.math;

import java.util.Comparator;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;


public class Optimizer {

	public static final int DEFAULT_ITER = 5000;
	
	
	public static final Comparator<PointValuePair> PV_COMPARATOR; 
	
	static{
		PV_COMPARATOR = new Comparator<PointValuePair>() {

			@Override
			public int compare(PointValuePair a, PointValuePair b) {
				return Double.compare(a.getValue().doubleValue(), b.getValue()
						.doubleValue());
			}

		};
	}
	

	public static MultivariateFunction wrap(BiVarFunc f) {
		return new MultivariateFunction() {

			@Override
			public double value(double[] point) {
				return f.apply(point[0], point[1]);
			}
		};
	}

	public static MultivariateFunction wrap(MultiVarFunc f) {
		return new MultivariateFunction() {

			@Override
			public double value(double[] point) {
				return f.apply(point);
			}
		};
	}
	
	
	public static double[] optimize(MultiVarFunc f, Matrix start){
		if(start.columns() > 1 && start.rows() > 1){
			throw new RuntimeException("The start Matrix must be (m x 1) or (1 x m) dimension.");
		}
		
		double[] vals;
		if(start.columns() == 1){
			vals = start.getColumn(0).toVectorArray();
		}else{
			vals = start.getRow(0).toVectorArray();
		}
		
		return optimize(f, vals);
	}
	
	public static double[] masiveOptimize(MultiVarFunc f, int dimensions){
		double[] tries = {0, 1, -1, 2, -2};
		
		double[] x = new double[dimensions];
		double[] ans = new double[dimensions];
		double cost = Double.MAX_VALUE;
		for(int i =0; i < tries.length; i++){
			
			for(int j =0; j < dimensions; j++){
				x[j] = tries[i];
			}
			
			double[] crtAns = optimize(f, x, DEFAULT_ITER * 10);
			double crtCost = f.apply(crtAns);
			if(crtCost < cost){
				ans = crtAns;
				cost = crtCost;
			}
		}
		
		return ans;
	}
	
	
	public static double[] optimize(MultiVarFunc f, double[] start){
		
		
		return optimize(wrap(f), start, DEFAULT_ITER);
	}
	
public static double[] optimize(MultiVarFunc f, double[] start, int iter){
		
		
		return optimize(wrap(f), start, iter);
	}
	
	public static double[] optimize(BiVarFunc f, double xstart, double ystart){
		return optimize(wrap(f), new double[]{xstart, ystart}, DEFAULT_ITER);
	}
	
	private static double[] optimize(MultivariateFunction mf, double[] start, int iter){
		
		NelderMeadSimplex s = new NelderMeadSimplex(start.length);
		s.build(start);
		
		
		
		
		for(int i =0; i < iter; i++){
			s.iterate(mf, PV_COMPARATOR);
		}
		
		
		PointValuePair min = s.getPoint(0);
		int size = s.getSize();
		for(int i = 1; i < size; i++){
			PointValuePair other = s.getPoint(i);
			if(PV_COMPARATOR.compare(other, min) < 0){
				min = other;
			}
		}
		
		return min.getPoint();
	}
}
