package com.yaison.cerebro.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class StructUtils {
	
	private static final Random RANDOM = new Random();
	
	private StructUtils(){
		super();
	}
	
	static int[] randomIndices(int length){
		int[] arr = new int[length];
		for(int i =0; i < arr.length; i++){
			arr[i] = i;
		}
		
		shuffle(arr);
		
		return arr;
	}
	
	static void verify(int[] shift){
		for (int i = 0; i < shift.length; i++) {
			int crt = shift[i];
			if (crt < 0 || crt >= shift.length) {
				throw new IndexOutOfBoundsException("The idx " + crt
						+ ", at shift position " + i
						+ " is out of bounds. The bounds are [0, "
						+ shift.length + "].");
			}
		}
		
		// let's make sure all the indices are correct.
		int[] temp = new int[shift.length];
		System.arraycopy(shift, 0, temp, 0, shift.length);
		
		Arrays.sort(temp);
		for (int i = 0; i < temp.length; i++) {
			int crt = temp[i];
			if (crt != i) {
				int idx = Arrays.binarySearch(temp, crt);
				
				throw new IllegalArgumentException("The entry " + crt
						+ " is repeated. Idx: " + idx + ".");
			}
		}
	}
	
	static void shuffle(int[] arr){
		//from Collections.shuffle
		
		final int size = arr.length;
		for (int i=size; i>1; i--){
            swap(arr, i-1, RANDOM.nextInt(i));
		}
	}
	
	 static void swap(int[] arr, int i, int j) {
		 int old = arr[j];
		 arr[j] = arr[i];
		 arr[i] = old;
	 }
	
	static int[] packSelection(int[] indices){
		if(indices == null || indices.length == 0){
			throw new IllegalArgumentException("The columns parameter can not be empty or null.");
		}
		
		List<Integer> list = new ArrayList<>();
		
		for(int i =0; i < indices.length; i++){
			int idx = indices[i];
			if(!list.contains(idx)){
				list.add(Integer.valueOf(idx));
			}
		}
		
		
		
		int[] arr = new int[list.size()];
		
		for(int i  =0; i < arr.length; i++){
			arr[i] = list.get(i);
		}
		Arrays.sort(arr);
		
		return arr;
	}
}
