package com.yaison.cerebro.descriptive;

import com.yaison.cerebro.DataInputStream;
import com.yaison.cerebro.structs.NumericMetaData;

public class DataInfoExtractor {


	private DataInputStream stream;

	public DataInfoExtractor() {
		super();
	}

	public void stream(DataInputStream stream) {
		this.stream = stream;
	}

	protected String getColumnName(int idx) {
		return null;
	}

	public DataInfo extract() {

		final NumericMetaData[] meta;
		try (DataInputStream s = stream) {
			final int cols = s.columns();
			final NumericMetaDataBuilder[] builders = new NumericMetaDataBuilder[cols];
			for(int i =0; i < cols; i++){
				builders[i] = new NumericMetaDataBuilder();
			}

			while (s.next()) {
				
				for (int i = 0; i < cols; i++) {
					double x = s.get(i);
					builders[i].add(x);
				}
			}

			meta = new NumericMetaData[cols];

			for (int i = 0; i < cols; i++) {
				builders[i].name(getColumnName(i));
				meta[i] = builders[i].build();
			}
		}

		return new DataInfo(meta);
	}


}
