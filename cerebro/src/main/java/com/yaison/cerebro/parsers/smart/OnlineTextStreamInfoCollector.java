package com.yaison.cerebro.parsers.smart;

import com.yaison.cerebro.CerebroUserException;
import com.yaison.cerebro.parsers.LineStreamSplitter;

public class OnlineTextStreamInfoCollector {
	
	private LineStreamSplitter splitter;
	
	public OnlineTextStreamInfoCollector() {
		super();
	}
	
	public void stream(LineStreamSplitter splitter) {
		this.splitter = splitter;
	}
	
	public TextColumnInfo[] collect() {
		return collect(true);
	}
	
	public TextColumnInfo[] collect(boolean strict) {
		
		final TextColumnInfo[] infos;
		try (LineStreamSplitter s = splitter) {
			infos = createInfo();
			
			int row = 0;
			while (s.next()) {
				String[] fields = s.split();	
				processRow(row, fields, infos);
				row++;
			}
		}
		
		return infos;
	}
	
	private void processRow(int row, String[] fields, TextColumnInfo[] infos){
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			TextColumnInfo info = infos[i];
			
			
			double val = silentParse(field);
			
			
			check(info, splitter.line(), field, val, row, i);
			
			if (Double.isNaN(val)) {
				info.addWord(field);
			} else {
				info.addNumber(val);
			}
		}
	}
	
	private TextColumnInfo[] createInfo(){
		final int columns = splitter.columns();
		TextColumnInfo[] infos = new TextColumnInfo[columns];
		for (int i = 0; i < infos.length; i++) {
			infos[i] = new TextColumnInfo();
			infos[i].setColumnIdx(i);
		}
		
		return infos;
	}
	
	private void check(TextColumnInfo info, String line, String field, double val, int row, int col){
		
			if (info.isAllNumbers() && Double.isNaN(val) ||
					info.isAllWords() && !Double.isNaN(val)) {
				throw new CerebroUserException(
						"In strict mode, columns can not be mixed (have words and numbers).\nRow: "
								+ (row + 1) + ", column: " + (col + 1) 
								+ ", val: " + field +"\nLine: '" +line +"'.");
			}
		
	}
	
	private double silentParse(String field) {
		if ("".equals(field)) {
			return Double.NaN;
		}
		
		try {
			double d = Double.parseDouble(field);
			if (Double.isInfinite(d)) {
				// What can we do with infinite? Let's consider
				// infinite as non-parseable.
				return Double.NaN;
			}
			
			return d;
		} catch (NumberFormatException ex) {
			return Double.NaN;
		}
	}
}
