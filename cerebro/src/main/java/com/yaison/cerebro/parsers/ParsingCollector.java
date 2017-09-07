package com.yaison.cerebro.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;

import com.yaison.cerebro.CerebroException;

public class ParsingCollector {
	
	
	
	private static final String[] DELIMITERS = { ",", ";", " ", " {1,},",
			" {1,};", " {1,}" };
	
	private String lines;
	private boolean collected;
	private Pattern delimiter;
	private Integer columns;
	
	public ParsingCollector() {
		super();
	}
	
	public void lines(String lines) {
		this.lines = lines;
	}
	
	public Pattern getDelimiter() {
		if (delimiter == null) {
			collect();
		}
		
		if (delimiter == null) {
			throw new CerebroException(
					"Could not determine the delimiter expression.");
		}
		
		return delimiter;
	}
	
	public int getColumns() {
		if (columns == null) {
			collect();
		}
		
		if (columns == null) {
			throw new CerebroException(
					"Could not determine the number of columns.");
		}
		
		return columns;
	}
	
	private int testDelimiter(String regex) {
		Pattern d = Pattern.compile(regex);
		
		Mean lineLength = new Mean();
		
		Max maxSplit = new Max();
		Min minSplit = new Min();
		
		try (BufferedReader reader = new BufferedReader(new StringReader(lines))) {
			
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				lineLength.increment(line.length());
				
				int splitLength = splitLength(line, d);
				
				maxSplit.increment(splitLength);
				minSplit.increment(splitLength);
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		if (maxSplit.getResult() > 1) {
			if (maxSplit.getResult() < lineLength.getResult()) {
				int max = ((int) maxSplit.getResult());
				int min = ((int) minSplit.getResult());
				
				if (max == min) {
					return (int) max;
				}
			}
		}
		
		return -1;
	}
	
	private int splitLength(String line, Pattern p) {
		Matcher m = p.matcher(line);
		int count = 0;
		while (m.find()) {
			count++;
		}
		
		// the result length of successful splitting is n = s + 1,
		// where s is the number of found splits.
		// e,g. "1,3,2" -> ',' was found twice, but the total split is 3.
		if (count > 0) {
			count++;
		}
		
		return count;
	}
	
	private void collect() {
		if (collected) {
			return;
		}
		
		collectDelimiter();
	}
	
	private void collectDelimiter() {
		if (delimiter != null) {
			return;
		}
		
		for (int i = 0; i < DELIMITERS.length; i++) {
			String regex = DELIMITERS[i];
			int cols = testDelimiter(regex);
			if (cols >= 1) {
				delimiter = Pattern.compile(regex);
				columns = cols;
				return;
			}
		}
	}
	
}
