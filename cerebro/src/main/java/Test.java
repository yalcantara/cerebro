import static com.yaison.cerebro.graph.Charts.scatter;

import java.util.Arrays;

import com.yaison.cerebro.Loader;
import com.yaison.cerebro.learning.Classification;
import com.yaison.cerebro.learning.Learning;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.Data;

public class Test {

	public static void main(String[] args) {
		Data data = Loader.fromFile("test-files/regularization/ex2data2.txt");
		double[][] raw = data.matrix().toArray();

		Matrix m = Matrix.from(raw);

		scatter(m, 0, 1, 2);

		Classification c = Learning.classify(data, 2);
		System.out.println(Arrays.toString(c.tetha()));
		System.out.println(c.error());

	}

}
