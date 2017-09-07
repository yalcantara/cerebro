package com.yaison.cerebro.structs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.parsers.smart.TextColumnInfo;

public class Header implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6503242679102811443L;
	
	private final List<ColumnHeader> headers;
	private final DataMatrixMapper mapper;
	
	public Header(TextColumnInfo[] infos, ColumnGroup[] cluster) {
		ColumnHeader[] headers = new ColumnHeader[infos.length];
		
		for(int i =0; i < infos.length; i++){
			headers[i] = new ColumnHeaderImpl(infos[i], cluster[i].header(), i);
		}
		
		this.headers = Collections.unmodifiableList(Arrays.asList(headers));
		this.mapper = new DataMatrixMapperImpl(headers);
	}
	
	
	public List<ColumnHeader> list(){
		return headers;
	}
	
	public ColumnHeader last(){
		return headers.get(headers.size()-1);
	}
	
	public ColumnHeader select(int colIdx){
		return select(new int[]{colIdx}).get(0);
	}
	
	public List<ColumnHeader> select(int... colIndices){
		colIndices = StructUtils.packSelection(colIndices);
		
		List<ColumnHeader> selection = new ArrayList<ColumnHeader>(colIndices.length);
		
		for(int i =0; i < colIndices.length; i++){
			selection.add(headers.get(colIndices[i]));
		}
		
		return Collections.unmodifiableList(selection);
	}
	
	public NumericMetaData meta(int colIdx){
		return headers.get(colIdx).meta();
	}
	
	public ColumnHeader get(int colIdx){
		return headers.get(colIdx);
	}
	
	public boolean isNumeric(int colIdx){
		return headers.get(colIdx).isNumeric();
	}
	
	public boolean isText(int colIdx){
		return headers.get(colIdx).isText();
	}
	
	
	public int columns(){
		return headers.size();
	}
	
	public int rows(){
		return headers.get(0).rows();
	}
	
	public int absIdx(int colIdx){
		int idx = 0;
		
		for(int i =0; i < colIdx; i++){
			idx += headers.get(i).groupHeader().columns();
		}
		
		return idx;
	}
	
	public int localIdx(int colIdx, String text){
		return headers.get(colIdx).localIdx(text);
	}
	
	public DataMatrixMapper mapper(){
		return mapper;
	}
	
	static class ColumnHeaderImpl implements ColumnHeader {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7353585849732287060L;
		
		private String label;
		private final int idx;
		private final NumericMetaData meta;
		private final List<String> texts;
		private final int rows;
		private final GroupHeader groupHeader;
		
		ColumnHeaderImpl(TextColumnInfo info, GroupHeader groupHeader, int idx) {
			this.idx = idx;
			this.rows = info.length();
			this.groupHeader = groupHeader;
			if (info.isAllNumbers()) {
				meta = info.meta();
				texts = null;
			}else if(info.isAllWords()){
				texts = Collections.unmodifiableList(Arrays.asList(info.words()));
				meta = null;
			}else{
				throw new IllegalArgumentException(
					"Invalid column at idx "
							+ info.getColumnIdx()
							+ ". The column must be either full numeric "
							+ "or full text. Got: "
							+ info + ".");
			}
		}
		
		@Override
		public List<String> texts(){
			return texts;
		}
		
		@Override
		public String getLabel() {
			return label;
		}
		
		@Override
		public void setLabel(String label) {
			this.label = label;
		}
		
		@Override
		public int rows(){
			return rows;
		}
		
		@Override
		public int dataIdx() {
			return idx;
		}
		
		@Override
		public boolean isNumeric() {
			return meta != null;
		}
		
		@Override
		public NumericMetaData meta() {
			if (meta == null) {
				throw new CerebroException(
						"The column at idx "
								+ idx
								+ " is not numeric. Please call isNumeric() "
								+ "first before calling this method.");
			}
			
			return meta;
		}
		
		@Override
		public String text(int idx) {
			return texts.get(idx);
		}
		
		@Override
		public int totalText() {
			return texts.size();
		}

		public int localIdx(String text){
			
			for(int i =0; i < texts.size(); i++){
				if(text.equals(texts.get(i))){
					return i;
				}
			}
			
			throw new IllegalArgumentException("Could not find the text '" + text +"' for column at original idx: " + idx +".");
		}

		@Override
		public GroupHeader groupHeader() {
			return groupHeader;
		}
		
	}
}
