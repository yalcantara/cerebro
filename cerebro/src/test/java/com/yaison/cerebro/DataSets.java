package com.yaison.cerebro;

import com.yaison.cerebro.structs.Data;

public class DataSets {
	
	private static Data iris;
	private static Data housing;
	private static Data ex1data1;
	private static Data ex2data2;
	
	public synchronized static Data iris(){
		if(iris == null){
			iris = Loader.fromFile("test-files/iris.data");
		}
		
		return iris;
	}
	
	public synchronized static Data housing(){
		if(housing == null){
			housing = Loader.fromFile("test-files/housing.data");
		}
		
		return housing;
	}
	
	public synchronized static Data ex1data1(){
		if(ex1data1 == null){
			ex1data1 = Loader.fromFile("test-files/linear-regression/ex1data1.txt");
		}
		
		return ex1data1;
	}
	
	public synchronized static Data ex2data2(){
		if(ex2data2 == null){
			String path = "test-files/regularization/ex2data2.txt";
			ex2data2 = Loader.fromFile(path);
		}
		
		return ex2data2;
	}
}
