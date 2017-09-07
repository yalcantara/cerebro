package com.yaison.cerebro.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.yaison.cerebro.parsers.ColumnMapper.Key;

public class DataMapper implements Iterable<DataMapper.DestColumn> {
	
	private final int originalColums;
	private final ArrayList<DestColumn> columns;
	private final ColumnMapper mapper;
	private final ColumnMapper others;
	
	public DataMapper(int originalColums) {
		columns = new ArrayList<DestColumn>(originalColums);
		
		if (originalColums < 1) {
			throw new IllegalArgumentException(
					"The initialColumns can not be less than 1. Got: "
							+ originalColums + ".");
		}
		
		for (int i = 0; i < originalColums; i++) {
			appendColumn();
		}
		
		this.originalColums = originalColums;
		mapper = new ColumnMapper();
		others = new ColumnMapper();
	}
	
	public int getOriginalColums() {
		return originalColums;
	}
	
	public void map(int src, String... words) {
		if (src < 0) {
			throw new IllegalArgumentException(
					"The src parameter can not be less " + "than 0. Got: "
							+ src + ".");
		}
		
		if (src >= originalColums) {
			throw new IllegalArgumentException("The src can not be higher "
					+ "or equals than the originalColumns. Got: " + src
					+ ", expected < " + originalColums + ".");
		}
		
		if (contains(src)) {
			throw new IllegalArgumentException(
					"The column was already mapped. Column: " + src + ".");
		}
		
		if(words.length == 2){
			mapBinary(src, words);
		}else{
			mapAll(src, words);
		}
	}
	
	private void mapBinary(int src, String[] words){
		String word1 = words[0];
		String word2 = words[1];
		Key k = map(src, word1, word2, src);
		
		DestColumn dest = columns.get(src);
		dest.name = name(src, word1, word2);
		dest.mapping = k;
	}
	
	private void mapAll(int src, String[] words){
		String word = words[0];
		Key k = map(src, word, null, src);
		
		DestColumn dest = columns.get(src);
		dest.name = name(src, k);
		dest.mapping = k;
		
		// if the mapping only has 2 words, then we can just map the first one
		// as 0 or 1.
		
		for (int i = 1; i < words.length; i++) {
			word = words[i];
			k = map(src, word, null, columns.size());
			appendColumn(src, k);
		}
	}
	
	public boolean isOriginal(int col) {
		return columns.get(col).isOriginal();
	}
	
	public boolean contains(int col) {
		return mapper.contains(col);
	}
	
	public List<Key> getOriginalMapping(int col) {
		return mapper.get(col);
	}
	
	public String getMapping(int col) {
		Key k = columns.get(col).mapping;
		return k == null ? null : k.key;
	}
	
	public String getName(int col) {
		return columns.get(col).name;
	}
	
	public List<Key> get(int col, String word) {
		if (mapper.contains(col, word)) {
			return Arrays.asList(mapper.get(col, word));
		}
		
		if (others.contains(col)) {
			return others.get(col);
		}
		
		return Collections.emptyList();
	}
	
	public int totalColumns() {
		return columns.size();
	}
	
	
	private Key map(int src, String word, String other, int target) {
		Key k = mapper.map(src, word, other, target, 1);
		others.map(src, word, null, target, 0);
		return k;
	}
	
	private DestColumn appendColumn() {
		int idx = columns.size();
		DestColumn dest = new DestColumn(idx, idx, name(idx));
		columns.add(dest);
		return dest;
	}
	
	private String name(int src) {
		return "col" + src;
	}
	
	private String name(int src, Key mapping) {
		return "col" + src + ":" + mapping.key;
	}
	
	private String name(int src, String word1, String word2) {
		return "col" + src + ":" + word1 +"," +word2;
	}
	
	private DestColumn appendColumn(int src, Key mapping) {
		int idx = columns.size();
		DestColumn dest = new DestColumn(src, idx, name(src, mapping), mapping);
		columns.add(dest);
		return dest;
	}
	
	public static final class DestColumn {
		
		private int src;
		private final int idx;
		private String name;
		private Key mapping;
		
		private DestColumn(int src, int idx, String name) {
			this(src, idx, name, null);
		}
		
		private DestColumn(int src, int idx, String name, Key mapping) {
			this.src = src;
			this.idx = idx;
			this.name = name;
			this.mapping = mapping;
		}
		
		public int getSrc() {
			return src;
		}
		
		public int getIdx() {
			return idx;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isOriginal() {
			return mapping == null;
		}
		
		@Override
		public String toString() {
			return "DestColumn [src=" + src + ", idx=" + idx + ", name=" + name
					+ ", original=" + isOriginal() + "]";
		}
		
	}
	
	public Iterator<DestColumn> iterator() {
		return columns.iterator();
	}
}
