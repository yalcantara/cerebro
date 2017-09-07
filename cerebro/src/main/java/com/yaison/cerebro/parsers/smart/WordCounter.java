package com.yaison.cerebro.parsers.smart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;



public class WordCounter implements Iterable<WordCounter.WordCount>{
	
	public static final String[] EMPTY_STRING_ARRAY = new String[]{};

	
	public static final int DEFAULT_MAX_WORDS = 1000;
	
	private ArrayList<WordCount> entries;
	private final int maxWords;
	
	public WordCounter(){
		this(DEFAULT_MAX_WORDS);
	}
	
	public WordCounter(int maxWords) {
		super();
		if(maxWords < 1){
			throw new IllegalArgumentException("The allowed minimun for "
					+ "maxWords is 1. Got: " + maxWords +".");
		}
		this.maxWords = maxWords;
	}
	
	public String[] words(){
		if(size() == 0){
			return EMPTY_STRING_ARRAY;
		}
		
		int l = size();
		String[] arr = new String[l];
		for(int i=0; i < arr.length; i++){
			arr[i] = entries.get(i).word;
		}
		
		return arr;
	}
	
	
	
	public int add(String word) {
		if(contains(word) == false){
			if(size() >= maxWords){
				return 0;
			}
		}
		
		if (entries == null) {
			entries = new ArrayList<>();
			entries.add(new WordCount(word));
			return 1;
		}
		
		if (entries.isEmpty()) {
			entries.add(new WordCount(word));
			return 1;
		}
		
		WordCount e = new WordCount(word);
		int idx = find(e);
		if (idx >= 0) {
			return entries.get(idx).increment();
		}
		
		int insertion = -(idx + 1);
		entries.add(insertion, e);
		return e.count();
	}
	
	public boolean isEmpty(){
		return entries == null || entries.isEmpty();
	}
	
	public int size(){
		if(entries == null){
			return 0;
		}
		return entries.size();
	}
	
	public boolean contains(String word){
		return find(word) >= 0;
	}
	
	public int count(String word) {
		if (isEmpty()) {
			return 0;
		}
		
		int idx = find(word);
		if (idx >= 0) {
			return entries.get(idx).count;
		}
		
		return 0;
	}
	
	private int find(String word) {
		return find(new WordCount(word));
	}
	
	private int find(WordCount entry) {
		if (isEmpty()) {
			return -1;
		}
		
		int idx = Collections.binarySearch(entries, entry);
		return idx;
	}
	
	public static final class WordCount implements Comparable<WordCount> {
		private final String word;
		private int count;
		
		private WordCount(String word) {
			this.word = word;
			count = 1;
		}
		
		public String word(){
			return word;
		}
		
		public int count(){
			return count;
		}
		
		private int increment() {
			return ++count;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((word == null) ? 0 : word.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WordCount other = (WordCount) obj;
			if (word == null) {
				if (other.word != null)
					return false;
			} else if (!word.equals(other.word))
				return false;
			return true;
		}
		
		@Override
		public int compareTo(WordCount o) {
			return word.compareTo(o.word);
		}

		@Override
		public String toString() {
			return "WordCount [word=" + word + ", count=" + count + "]";
		}
		
		
	}

	@Override
	public Iterator<WordCount> iterator() {
		if(isEmpty()){
			return Collections.emptyIterator();
		}
		return entries.iterator();
	}
}
