package com.cj.theguardian.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CsvReader {
	private BufferedReader reader;

	private String[] headers;

	public static Map<String,String> nextRowAsMap(CsvReader reader, String[] headers) {
		Map<String,String> mappedByColumnNum = reader.nextRowAsMap();
		Map<String,String> mappedByHeaderName = new HashMap<>();
		for (Map.Entry<String, String> mappedByColumn : mappedByColumnNum.entrySet()) {
			String headerName = headers[Integer.parseInt(mappedByColumn.getKey()) - 1];
			mappedByHeaderName.put(headerName, mappedByColumn.getValue());
		}
		return mappedByHeaderName;
	}

	public CsvReader(File file, boolean hasHeaders) throws FileNotFoundException {
		if (file.exists() && !file.isDirectory()) {
			this.createReader(new FileInputStream(file), hasHeaders);
		} else {
			throw new IllegalArgumentException("File does not exist or is a direcotry " + file.getAbsolutePath());
		}
	}

	public CsvReader(String classpathResource, boolean hasHeaders) {
		InputStream stream = com.cj.theguardian.utils.CsvReader.class.getClassLoader().getResourceAsStream(classpathResource);
		if (stream == null) {
			throw new IllegalArgumentException("classpath resource does not exist: " + classpathResource);
		} else {
			this.createReader(stream, hasHeaders);
		}
	}

	private void createReader(InputStream stream, boolean hasHeaders) {
		this.reader = new BufferedReader(new InputStreamReader(stream));
		if (hasHeaders) {
			try {
				String headers = this.reader.readLine().replace("\uFEFF","");
				this.headers = headers.split(",");
			} catch (IOException var4) {
			}
		}

	}

	/**
	 * keys will be the headers, unless the file has none, then it the keys will be simply the column number, ie "1", "2","3" etc
	 *
	 * @return
	 */
	public Map<String,String> nextRowAsMap() {
		String[] nextRow = nextRow();
		if(nextRow == null) {
			return null;
		}
		Map<String,String> rowMap = new HashMap<>();
		for(int i = 0; i < nextRow.length;i++) {
			String header = headers != null ? headers[i] : "" + (i+1);
			rowMap.put(header, nextRow[i].replace("\uFEFF",""));
		}
		return rowMap;
	}

	public Map<String,String> nextRowAsMap(String[] headers) {
		return nextRowAsMap(this, headers);
	}

	public String[] nextRow() {
		String nextLine = "";

		try {
			while(nextLine != null && StringUtils.isBlank(nextLine)) {
				nextLine = this.reader.readLine();
			}
		} catch (IOException var3) {
			return null;
		}

		String[] commaDelimitedLine = null;
		if (nextLine != null) {
			commaDelimitedLine = nextLine.split(",");
		}

		return commaDelimitedLine;
	}

	public void close() {
		try{
			reader.close();
		} catch(Exception e) {
			// well we tried
		}
	}
}

