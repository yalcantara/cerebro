package com.yaison.cerebro;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.yaison.cerebro.parsers.LineStreamSplitter;
import com.yaison.cerebro.parsers.ParsingCollector;
import com.yaison.cerebro.parsers.TextFileReader;
import com.yaison.cerebro.parsers.smart.OnlineTextStreamInfoCollector;
import com.yaison.cerebro.parsers.smart.TextColumnInfo;
import com.yaison.cerebro.structs.ColumnGroup;
import com.yaison.cerebro.structs.Data;
import com.yaison.cerebro.structs.DataImpl;
import com.yaison.cerebro.structs.Header;

public class Loader {
	
	public static Data fromFile(String path) {
		return fromFile(Paths.get(path));
	}
	
	private static TextColumnInfo[] info(Path path) {
		
		final TextColumnInfo[] infos;
		
		try (LineStreamSplitter sp = split(path)) {
			
			OnlineTextStreamInfoCollector oc = new OnlineTextStreamInfoCollector();
			oc.stream(sp);
			
			infos = oc.collect();
		}
		
		return infos;
	}
	
	private static LineStreamSplitter split(Path path) {
		TextFileReader r = null;
		
		try {
			r = new TextFileReader(path);
			ParsingCollector col = new ParsingCollector();
			col.lines(r.peek(10));
			
			LineStreamSplitter sp = new LineStreamSplitter();
			sp.delimiter(col.getDelimiter());
			sp.stream(r);
			
			return sp;
		} catch (Exception ex) {
			if (r != null) {
				r.close();
			}
			throw ex;
		}
	}
	
	public static Data fromFile(Path path) {
		TextColumnInfo[] infos = info(path);
		
		ColumnGroup[] cluster = new ColumnGroup[infos.length];
		for(int i =0; i < infos.length; i++){
			TextColumnInfo info = infos[i];
			cluster[i] = new ColumnGroup(info);
		}
		
		Header h = new Header(infos, cluster);
		
		
		try (LineStreamSplitter sp = split(path)) {
			
			int row=0;
			while(sp.next()){
				String[] fields = sp.split();
				
				for(int i =0; i < fields.length; i++){
					String field = fields[i];
					
					if(h.isNumeric(i)){
						cluster[i].setOriginal(row, Double.parseDouble(field));
					}else{
						int localIdx = h.localIdx(i, field);
						
						cluster[i].column(localIdx).setOriginal(row, 1);
					}
				}
				
				row++;
			}
		}
		
		
		return new DataImpl(h, cluster);
	}
}
