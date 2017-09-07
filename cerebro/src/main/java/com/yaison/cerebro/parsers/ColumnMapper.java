package com.yaison.cerebro.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yaison.cerebro.CerebroException;

public class ColumnMapper {
	
	private final Map<Integer, List<Key>> map;
	
	public ColumnMapper() {
		map = new HashMap<>();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ColumnMapper clone() {
		ColumnMapper m = new ColumnMapper();
		((HashMap) m.map).putAll(this.map);
		return m;
	}
	
	public Key map(int src, String key, String other, int target, double val) {
		if (Double.isNaN(val)) {
			throw new IllegalArgumentException(
					"The value NaN is not acceptable.");
		}
		
		List<Key> list = map.get(src);
		if (list == null) {
			list = new ArrayList<>();
			map.put(src, list);
		}
		
		if (find(list, key) >= 0) {
			throw new IllegalArgumentException("The key '" + key
					+ "' already exist for src " + src + ".");
		}
		
		Key k = new Key(key, other, target, val);
		list.add(k);
		return k;
	}
	
	public boolean contains(int col) {
		return map.containsKey(col);
	}
	
	public boolean contains(int col, String key) {
		return doget(col, key) != null;
	}
	
	public List<Key> get(int col) {
		List<Key> list = map.get(col);
		if (list == null) {
			throw new CerebroException("No mapping for col " + col
					+ ". Make sure constains(int) was called first.");
		}
		
		return list;
	}
	
	public Key get(int col, String key) {
		Key ans = doget(col, key);
		if (key == null) {
			throw new CerebroException("No mapping for col " + col
					+ " and word '" + key
					+ "'. Make sure constains(int, String) was called first.");
		}
		
		return ans;
	}
	
	private Key doget(int col, String key) {
		List<Key> list = map.get(col);
		if (list == null) {
			return null;
		}
		
		int idx = find(list, key);
		if (idx == -1) {
			return null;
		}
		
		return list.get(idx);
	}
	
	private int find(List<Key> list, String key) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).same(key)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static final class Key {
		final String key;
		final String other;
		final int target;
		final double val;
		
		private Key(String key, String other, int target, double val) {
			this.key = key;
			this.other = other;
			this.target = target;
			this.val = val;
		}
		
		public boolean isBinary(){
			return other != null;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getOther(){
			return other;
		}
		
		public int getTarget() {
			return target;
		}
		
		public double getVal() {
			return val;
		}
		
		public boolean same(String key) {
			return this.key.equals(key);
		}
		
		@Override
		public String toString() {
			return "Key [key=" + key + ", target=" + target + ", val=" + val
					+ "]";
		}
		
	}
}
