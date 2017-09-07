package com.yaison.cerebro.structs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.yaison.cerebro.DataSets;

public class DataImplTest {
	
	private DataImpl data;
	private List<String[]> raw;
	
	public DataImplTest() {
		super();
	}
	
	@Before
	public void setUp() throws Exception {
		String path = "test-files/iris.data";
		Data data = DataSets.iris();
		assertTrue(data instanceof DataImpl);
		this.data = (DataImpl)data;
		
		raw = new ArrayList<String[]>();
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line;
			while((line = br.readLine()) != null){
				if(line.trim().isEmpty()){
					continue;
				}
				raw.add(line.split(","));
			}
		}
	}
	
	@Test
	public void testLength(){
		assertEquals(raw.size(), data.rows());
	}
	
	
	
	@Test
	public void testGetInt() {
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);
		nf.setMinimumFractionDigits(4);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		
		for(int i =0; i < raw.size(); i++){
			Object[] row = data.get(i);
			String[] src = raw.get(i);

			checkRow(nf, row, src);
		}
	}
	
	private void checkRow(NumberFormat nf, Object[] row, String[] raw){
		for(int i =0; i < raw.length; i++){
			Object val  = row[i];
			String src = raw[i];
			
			
			String valStr;
			String srcStr;
			
			if(val instanceof Double || val instanceof Float){
				valStr = nf.format(((Number)val).doubleValue());
				srcStr = nf.format(Double.parseDouble(src));
			}else if(val instanceof Number){
				valStr = nf.format(((Number)val).longValue());
				srcStr = nf.format(Long.parseLong(src));
			}else{
				valStr = String.valueOf(val);
				srcStr = src;
			}
			
			assertEquals(srcStr, valStr);
		}
	}
	
}
