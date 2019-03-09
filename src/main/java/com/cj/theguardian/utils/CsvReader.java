package com.cj.theguardian.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

public class CsvReader {

	private BufferedReader reader;

	public CsvReader(File file, boolean hasHeaders) throws FileNotFoundException {
		if (file.exists() && !file.isDirectory()) {
			createReader(new FileInputStream(file), hasHeaders);
		} else {
			throw new IllegalArgumentException("File does not exist or is a direcotry " + file.getAbsolutePath());
		}
	}

	public CsvReader(String classpathResource, boolean hasHeaders) {
		InputStream stream = CsvReader.class.getClassLoader().getResourceAsStream(classpathResource);
		if(stream == null) {
			throw new IllegalArgumentException("classpath resource does not exist: " + classpathResource);
		}
		createReader(stream,hasHeaders);
	}

	private void createReader(InputStream stream, boolean hasHeaders) {
		reader = new BufferedReader(new InputStreamReader(stream));
		if (hasHeaders) {
			try {
				reader.readLine();
			} catch (IOException e) {
				// TODO log warning
			}
		}
	}

	public String[] nextRow() {
		String nextLine = "";
		try {
			while (nextLine != null && StringUtils.isBlank(nextLine)) {
				nextLine = reader.readLine();
			}
		} catch (IOException e) {
			// TODO throw exception or just log?
			return null;
		}
		String[] commaDelimitedLine = null;
		if(nextLine != null) {
			commaDelimitedLine = nextLine.split(",");
		}
		return commaDelimitedLine;

	}

}
