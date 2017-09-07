package com.yaison.cerebro.parsers.smart;

import com.yaison.cerebro.descriptive.NumericMetaDataBuilder;
import com.yaison.cerebro.structs.NumericMetaData;

public class TextColumnInfo {
	
	private int colIdx;
	private int numberCount;
	private int wordCount;
	private WordCounter counter;
	private NumericMetaDataBuilder metaBuilder;
	private NumericMetaData meta;
	
	
	
	public TextColumnInfo(){
		super();
	}
	
	public void setColumnIdx(int colIdx){
		this.colIdx = colIdx;
	}
	
	public int getColumnIdx(){
		return colIdx;
	}
	
	public boolean isMixed(){
		if(isEmpty()){
			return false;
		}
		
		return wordCount > 0 && numberCount > 0;
	}
	
	public double numPerc(){
		return getNumberCount() / (double)length();
	}
	
	public double wordPerc(){
		return getWordCount() / (double)length();
	}
	
	public boolean isAllNumbers(){
		if(length() == 0){
			return false;
		}
		
		return wordCount == 0;
	}
	
	public boolean isAllWords(){
		if(length() == 0){
			return false;
		}
		
		return numberCount == 0;
	}
	
	public void addWord(String field){
		if(counter == null){
			counter = new WordCounter();
		}
		
		counter.add(field);
		wordCount++;
	}
	
	public int getNumberCount() {
		return numberCount;
	}

	public int getWordCount() {
		return wordCount;
	}

	public int diffWords(){
		if(counter == null){
			return 0;
		}
		
		return counter.size();
	}
	
	
	public boolean isEmpty(){
		return length() == 0;
	}
	
	public int length(){
		return wordCount + numberCount;
	}
	
	public int count(String word){
		if(counter == null){
			return 0;
		}
		
		return counter.count(word);
	}
	
	public void addNumber(double val){
		if(metaBuilder == null){
			metaBuilder = new NumericMetaDataBuilder();
		}
		metaBuilder.add(val);
		numberCount++;
	}
	
	public String[] words(){
		if(counter == null){
			return WordCounter.EMPTY_STRING_ARRAY;
		}
		
		return counter.words();
	}
	
	
	
	public NumericMetaData meta(){
		if(meta == null){
			meta = metaBuilder.build();
		}
		
		return meta;
	}

	@Override
	public String toString() {
		return "TextColumnInfo [colIdx=" + colIdx + ", numberCount="
				+ numberCount + ", wordCount=" + wordCount + ", diffWords="
				+ diffWords() + "]";
	}
	
	
}
