package com.yaison.cerebro.learning;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.algorithms.SupervisedAlgorithm;
import com.yaison.cerebro.algorithms.LogisticRegression;
import com.yaison.cerebro.algorithms.MinSquareRegression;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.math.MultiVarFunc;
import com.yaison.cerebro.math.Optimizer;
import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedColumn;
import com.yaison.cerebro.structs.ComputedColumnFunc;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.Data;
import com.yaison.cerebro.structs.DataMatrixMapper;
import com.yaison.cerebro.structs.Header;
import com.yaison.cerebro.structs.NumericMetaData;


public class Learning {
	
	public static Classification classify(Data data, int predictColumn) {
		return classify(data, predictColumn, null);
	}
	
	public static Classification classify(Data data, int predictColumn,
			String target) {
		
		if (data.isNumeric(predictColumn)) {
			
			if (target == null) {
				if (data.header().meta(predictColumn).binary()) {
					return (Classification) solve(data, predictColumn, target,
						LogisticRegression.class);
				}
			}
			
			throw new IllegalArgumentException(
					"The column at idx "
							+ predictColumn
							+ " is a numeric type, so it cann't be used as the prediction column for classification.");
		}
		
		return (Classification) solve(data, predictColumn, target,
			LogisticRegression.class);
	}
	
	public static Regression regress(Data data, int predictColumn, String word) {
		if (data.isNumeric(predictColumn)) {
			throw new IllegalArgumentException(
					"The column at idx "
							+ predictColumn
							+ " is a text type, so it cann't be used as the prediction column for regression.");
		}
		
		
		return (Regression) solve(data, predictColumn, word,
			MinSquareRegression.class);
	}
	
	public static Regression regress(Data data, int predictColumn) {
		if (data.isText(predictColumn)) {
			throw new IllegalArgumentException(
					"The column at idx "
							+ predictColumn
							+ " is a text type, so it cann't be used as the prediction column for regression.");
		}
		
		return (Regression) solve(data, predictColumn, null,
			MinSquareRegression.class);
	}
	
	private static MLProblem solve(Data data, int predictColumn, String word,
			Class<? extends SupervisedAlgorithm> algoClass) {
		Header h = data.header();
		DataMatrixMapper mapper = h.mapper();
		Matrix main = data.matrix();
		
		int[] dataFeatures = features(h, predictColumn);
		
		List<ColumnHeader> featureHeaders = h.select(dataFeatures);
		//ComputedInfo computed = extraColumns(data, featureHeaders);
		
		ColumnHeader hipotesisHeader = h.select(predictColumn);
		
		//Matrix X = main.selectColumns(mapper.toMatrixColumns(dataFeatures))
		//		.extra(computed.cols);
		Matrix X = main.selectColumns(mapper.toMatrixColumns(dataFeatures));
		
		Matrix Y;
		
		if (word == null) {
			Y = main.selectColumn(mapper.toMatrixColumn(predictColumn));
		} else {
			Y = main.selectColumn(mapper.toMatrixColumn(predictColumn, word));
		}
		
		SupervisedAlgorithm algo;
		if (algoClass == MinSquareRegression.class) {
			algo = new MinSquareRegression(X, Y);
		} else if (algoClass == LogisticRegression.class) {
			algo = new LogisticRegression(X, Y);
		} else {
			throw unsuportedAlgo(algoClass);
		}
		
		algo.setRandomTheta();
		double[] ans = Optimizer.optimize((MultiVarFunc) algo,
			algo.cloneTheta());
		algo.setTheta(ans);
		double error = algo.j();
		
		if (algoClass == MinSquareRegression.class) {
			return new RegressionImpl(algo, error, featureHeaders,
					null, hipotesisHeader);
		}
		
		if (algoClass == LogisticRegression.class) {
			return new ClassificationImpl(algo, error, featureHeaders,
					null, hipotesisHeader, word);
		}
		
		throw unsuportedAlgo(algoClass);
	}
	
	private static CerebroException unsuportedAlgo(
			Class<? extends SupervisedAlgorithm> algoClass) {
		return new CerebroException("Unsupported algorithm "
				+ algoClass.getName() + ".");
	}
	
	@SuppressWarnings("unused")
	private static ComputedInfo extraColumns(Data data,
			List<ColumnHeader> features) {
		
		Matrix original = data.matrix();
		
		DataMatrixMapper mapper = data.header().mapper();
		
		List<ComputedColumn> computedCols = new ArrayList<ComputedColumn>();
		List<ComputedFeatureFunc> computedFeatures = new ArrayList<ComputedFeatureFunc>();
		
		for (int i = 0; i < features.size(); i++) {
			ColumnHeader h = features.get(i);
			if (h.isText()) {
				continue;
			}
			
			// We are going to ignore 2 types of columns
			//
			// a) Columns that have 0 variance: they don't have different
			// values (all values are the same) thus, useless.
			//
			// b) Binary valued columns: those columns only have 2 different
			// values, so are their applied func.
			NumericMetaData meta = h.meta();
			if (meta.var() == 0.0 || meta.binary()) {
				continue;
			}
			
			final int col = mapper.toMatrixColumn(i);
			
			ComputedColumnFunc c3 = (m, r) -> pow(m.get(r, col), 3);
			ComputedColumnFunc c2 = (m, r) -> pow(m.get(r, col), 2);
			ComputedColumnFunc cexp = (m, r) -> exp(m.get(r, col));
			ComputedColumnFunc csqrt = (Matrix m, int r) -> {
				double v = m.get(r, col);
				return signum(v) * sqrt(abs(v));
			};
			
			ComputedFeatureFunc f3 = (double[] row) -> {
				return pow(row[col], 3);
			};
			ComputedFeatureFunc f2 = (row) -> pow(row[col], 2);
			ComputedFeatureFunc fexp = (row) -> exp(row[col]);
			ComputedFeatureFunc fsqrt = (row) -> {
				double v = row[col];
				return signum(v) * sqrt(abs(v));
			};
			
			computedCols.add(new ComputedColumn(original, c3));
			computedCols.add(new ComputedColumn(original, c2));
			computedCols.add(new ComputedColumn(original, cexp));
			computedCols.add(new ComputedColumn(original, csqrt));
			
			computedFeatures.add(f3);
			computedFeatures.add(f2);
			computedFeatures.add(fexp);
			computedFeatures.add(fsqrt);
		}
		
		return new ComputedInfo(computedCols, computedFeatures);
	}
	
	private static int[] features(Header h, int predictColumn) {
		
		int[] selection = new int[h.columns() - 1];
		int idx = 0;
		for (int i = 0; i < h.columns(); i++) {
			if (i == predictColumn) {
				continue;
			}
			selection[idx] = i;
			idx++;
		}
		
		return selection;
	}
	
	@SuppressWarnings("unused")
	private static class ComputedInfo {
		final List<ComputedColumn> cols;
		final List<ComputedFeatureFunc> features;
		
		private ComputedInfo(List<ComputedColumn> cols,
				List<ComputedFeatureFunc> features) {
			this.cols = Collections.unmodifiableList(cols);
			this.features = Collections.unmodifiableList(features);
		}
	}
}
