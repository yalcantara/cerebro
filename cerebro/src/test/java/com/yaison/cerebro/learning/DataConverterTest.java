package com.yaison.cerebro.learning;

import static java.lang.Math.pow;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;
import com.yaison.cerebro.math.Matrix;
import com.yaison.cerebro.structs.ColumnHeader;
import com.yaison.cerebro.structs.ComputedFeatureFunc;
import com.yaison.cerebro.structs.Data;

public class DataConverterTest {
	
	
	private Data data;
	
	@Before
	public void setup(){
		data = DataSets.iris();
	}
	
	@Test
	public void testConvert(){
		List<ColumnHeader> headers = data.header().list();
		DataConverter c = new DataConverter(headers);
		
		double[] dest = new double[c.originalMatrixColumns()];
		
		Matrix m = data.matrix();
		for(int i =0; i < data.rows(); i++){
			c.convert(data.row(i), dest);
			
			for(int j =0; j < dest.length; j++){
				assertEquals(m.get(i, j), dest[j], 0.00009);
			}
		}
	}
	
	@Test
	public void testComputedConvert(){
		List<ColumnHeader> headers = data.header().list();
		
		ComputedFeatureFunc f1 = (r)->pow(r[0], 3);
		ComputedFeatureFunc f2 = (r)->pow(r[0], 2);
		ComputedFeatureFunc f3 = (r)->pow(r[1], 3);
		ComputedFeatureFunc f4 = (r)->pow(r[1], 2);
		ComputedFeatureFunc f5 = (r)->r[0] + r[1];
		
		
		DataConverter c = new DataConverter(headers, f1, f2, f3, f4, f5);
		double[] dest = new double[c.originalMatrixColumns()];
		
		
		Matrix m = data.matrix();
		dest = new double[c.totalColumns()];
		
		for(int i =0; i < data.rows(); i++){
			c.convert(data.row(i), dest);
			
			for(int j =0; j < c.originalMatrixColumns(); j++){
				assertEquals(m.get(i, j), dest[j], 0.00009);
			}
			
			int cidx = c.originalMatrixColumns();
			assertEquals(pow(m.get(i, 0), 3), dest[cidx], 0.00009);
			assertEquals(pow(m.get(i, 0), 2), dest[cidx + 1], 0.00009);
			assertEquals(pow(m.get(i, 1), 3), dest[cidx + 2], 0.00009);
			assertEquals(pow(m.get(i, 1), 2), dest[cidx + 3], 0.00009);
			assertEquals(m.get(i, 0) + m.get(i, 1), dest[cidx + 4], 0.00009);
		}
	}
}
