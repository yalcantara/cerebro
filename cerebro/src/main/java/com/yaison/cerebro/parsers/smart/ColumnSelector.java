package com.yaison.cerebro.parsers.smart;

public class ColumnSelector {
	
	private static final double DEFAULT_PASS_THRESHOLD = 0.1; // 10%
	
	public static void main(String[] args) {
		TextColumnInfo info = new TextColumnInfo();
		for (int i = 0; i < 10; i++) {
			info.addNumber(i);
		}
		info.addWord("klk");
		
		ColumnSelector s = new ColumnSelector();
		
		System.out.println(s.passThreshold(info));
	}
	
	public boolean passThreshold(TextColumnInfo info) {
		if (isPartialCandidate(info) == false) {
			return false;
		}
		
		
		if (info.numPerc() < DEFAULT_PASS_THRESHOLD
				|| info.wordPerc() < DEFAULT_PASS_THRESHOLD) {
			return true;
		}
		
		return false;
	}
	
	public boolean isFullCandidate(TextColumnInfo info){
		if(isPartialCandidate(info) && (info.isAllNumbers() || info.isAllWords())){
			return true;
		}
		
		return false;
	}
	
	public boolean isPartialCandidate(TextColumnInfo info) {
		if (info.isEmpty()) {
			return false;
		}
		
		if (info.isAllNumbers()) {
			if (info.meta().zeroed()) {
				return false;
			}
		}
		
		if (info.isAllWords()) {
			if (info.diffWords() == 1) {
				return false;
			}
		}
		
		return true;
	}
}
