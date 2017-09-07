package com.yaison.cerebro.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yaison.cerebro.CerebroException;
import com.yaison.cerebro.LineInputStream;

public class TextFileReader implements LineInputStream {
	
	
	private static final Pattern NEW_LINE = Pattern.compile("\r\n|\r|\n");
	
	private final Path path;
	private boolean first = true;
	private BufferedReader reader;
	private String peekBuffer;
	private BufferedReader peekReader;
	private boolean end = false;
	private String crtLine = null;
	
	public TextFileReader(String path) {
		this(Paths.get(path));
	}
	
	public TextFileReader(Path path) {
		if (path.isAbsolute() == false) {
			path = path.toAbsolutePath().normalize();
		}
		
		String str = path.toUri().toString();
		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS) == false) {
			throw new CerebroException("The file " + str + " does not exist.");
		}
		
		if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) == false) {
			throw new CerebroException("The file " + str
					+ " isn't a regular file.");
		}
		
		if (Files.isReadable(path) == false) {
			throw new CerebroException("The file " + str + " is not readable.");
		}
		
		System.out.println("path: " + str + ", seems ok.");
		this.path = path;
	}
	
	public void start() {
		end = false;
		first = true;
		reader = null;
		crtLine = null;
	}
	
	public boolean next(){
		String line = read();
		if(line == null){
			crtLine = null;
			return false;
		}
		
		crtLine = line;
		
		return true;
	}
	
	public String line(){
		return crtLine;
	}
	
	protected String read() {
		if (peekBuffer == null) {
			return doNext();
		}
		
		ensurePeek();
		String line;
		try {
			line = peekReader.readLine();
		} catch (IOException e) {
			close();
			throw new RuntimeException(e);
		}
		
		if (line == null) {
			releasePeek();
			return doNext();
		}
		
		return line;
	}
	
	private void ensurePeek() {
		if (peekReader == null) {
			peekReader = new BufferedReader(new StringReader(peekBuffer));
		}
	}
	
	private void ensureStart() {
		if (first) {
			first = false;
			try {
				reader = new BufferedReader(new FileReader(path.toFile()));
			} catch (FileNotFoundException e) {
				close();
				throw new RuntimeException(e);
			}
		}
	}
	
	private String readLine() {
		ensureStart();
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			close();
			throw new RuntimeException(e);
		}
		
		return line;
	}
	
	private String doNext() {
		if (end) {
			return null;
		}
		
		String line = readLine();
		
		if (line == null) {
			end = true;
			close();
		}
		
		return line;
	}
	
	public boolean ended() {
		return end;
	}
	
	public Path path() {
		return path;
	}
	
	@Override
	public void close() {
		end = true;
		crtLine = null;
		if (reader == null) {
			return;
		}
		
		try {
			reader.close();
		} catch (Exception ex) {
			// ignored
		}
		reader = null;
		
		releasePeek();
	}
	
	private void releasePeek() {
		peekBuffer = null;
		try {
			peekReader.close();
		} catch (Exception ex) {
			// ignored
		}
		peekReader = null;
	}
	
	@Override
	public String peek(int lines) {
		if (end) {
			return null;
		}
		
		final StringBuilder sb;
		
		if (peekBuffer != null) {
			int crtLines = countLines(peekBuffer); 
			if(crtLines >= lines){
				return readLines(peekBuffer, lines);
			}
			
			sb = new StringBuilder();
			sb.append(peekBuffer);
			lines = lines - crtLines; //only read the lines that are necessary
		}else{
			sb = new StringBuilder();
		}
		
		int counter = 0;
		String line;
		while (counter < lines && (line = readLine()) != null) {
			sb.append(line);
			sb.append('\n');
			
			counter++;
		}
		
		peekBuffer = sb.toString();
		
		return peekBuffer;
	}
	
	protected static String readLines(String str, int lines){
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		Matcher m = NEW_LINE.matcher(str);
		if( m.find()){
			count++;
			int s = 0;
			int end = m.start();
			sb.append(str.substring(s, end));
			s = m.end();
			while( count < lines && m.find()){
				count++;
				sb.append('\n');
				end = m.start();
				sb.append(str.substring(s, end));
				s = m.end();
			}
			
			if(count < lines && s < str.length()){
				count++;
				sb.append('\n');
				sb.append(str.substring(s));
			}
		}else{
			return str;
		}
		
		return sb.toString();
	}
	
	
	private static int countLines(String str) {
		
		if (str == null || str == ""
				|| (str.length() < 100 && str.trim().isEmpty())) {
			//100 is the trim threshold
			return 0;
		}
		int sum = 1;
		
		Matcher m = NEW_LINE.matcher(str);
		while (m.find()) {
			sum++;
		}
		
		return sum;
	}
}
