package com.yaison.cerebro.structs;

import java.io.Serializable;
import java.util.List;

import com.yaison.cerebro.CerebroException;

public interface ColumnHeader extends Serializable  {
	
	public int rows();
	public String getLabel();
	public void setLabel(String label);
	public int dataIdx();
	
	public boolean isNumeric();
	public NumericMetaData meta();
	
	public List<String> texts();
	default public String text(){
		if(isNumeric()){
			throw new CerebroException("The column at idx " + dataIdx() +", is numeric. Can not call text() on a numeric column.");
		}
		
		return text(0);
	}
	
	public int localIdx(String key);
	public String text(int idx);
	public int totalText();
	
	default public boolean isText(){
		return !isNumeric();
	}
	
	default public boolean isDiscrete(){
		return isNumeric() && meta().discrete();
	}
	default public boolean isContinuous(){
		return !isDiscrete();
	}
	
	public GroupHeader groupHeader();
}
