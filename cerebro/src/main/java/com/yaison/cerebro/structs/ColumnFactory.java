package com.yaison.cerebro.structs;

import com.yaison.cerebro.structs.primitives.BigArray;
import com.yaison.cerebro.structs.primitives.BigBitArray;
import com.yaison.cerebro.structs.primitives.BigByteArray;
import com.yaison.cerebro.structs.primitives.BigCharArray;
import com.yaison.cerebro.structs.primitives.BigFloatArray;
import com.yaison.cerebro.structs.primitives.BigIntArray;

public class ColumnFactory {
	
	
	
	private ColumnFactory() {
		super();
	}
	
	
	public static Column binary(int length){
		return new BinaryColumn(length);
	}
	
	public static Column create(NumericMetaData meta) {
		double max = meta.max();
		double min = meta.min();
		int length = meta.length();
		boolean discrete = meta.discrete();
		
		if(discrete){
			if (min == 0 && max == 1) {
				return binary(length);
			}
		}
		return new ColumnImpl(meta, bigArray(meta));
	}
	
	private static BigArray bigArray(NumericMetaData meta) {
		double max = meta.max();
		double min = meta.min();
		int length = meta.length();
		boolean discrete = meta.discrete();
		
		if (discrete) {
			if (min == 0 && max == 1) {
				return new BigBitArray(length);
			}
			
			double rangeDouble = max - min;
			long rangeLong = (long) rangeDouble;
			if (rangeLong == rangeDouble) {
				if (rangeLong == 0 || rangeLong == 1) {
					return new BigBitArray(length);
				}
				
				if (rangeLong <= 255) {
					return new BigByteArray(length);
				}
				
				if (rangeLong <= 65535) {
					return new BigCharArray(length);
				}
				
				if (rangeLong <= 4294967295L) {
					return new BigIntArray(length);
				}
			}
			
		}
		
		return new BigFloatArray(length);
	}
}
