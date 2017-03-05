package br.ufal.ic.JCAM;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

	private Token currentToken;
	
	// lines
	private List<String> lines = new ArrayList<String>();

	// current line
	private String currentLineString = null;

	// line and column of cursor
	private int currentColumnNumber = 0, currentLineNumber = 0;

	public LexicalAnalyzer(BufferedReader file) throws IOException {
		String currentLine = null;
		
		while((currentLine = file.readLine()) != null)
			this.lines.add(currentLine);
			
	}

	public Token nextToken() {

		// store beginning of sequence. use this for print any errors
		int currentColumnBeginning = this.currentColumnNumber;
		
		int charValue = this.getNextChar();
		
		
		return currentToken;
	}

	/**
	 * get next char value (0 to 65535)
	 * 
	 * @return next char value, or -1 if reaches end of file
	 */
	private int getNextChar() {
		
		if (this.lines.isEmpty())
			return -1;
		
		// initialization for the first time which you call this
		if (this.currentLineString == null)
			this.currentLineString = this.lines.get(this.currentLineNumber);
		else
			++this.currentColumnNumber;
		
		// while end of line, get next line
		while (this.currentColumnNumber >= this.currentLineString.length()) {
			if (this.currentLineNumber + 1 < this.lines.size()){
				this.currentColumnNumber = 0;
				++this.currentLineNumber;
				this.currentLineString = this.lines.get(this.currentLineNumber);
			}
			else
				return -1;
		}
		
		return this.currentLineString.charAt(this.currentColumnNumber);
	}

	private boolean isSpace(String value) {
		return value.matches("\\s+");
	}

	private boolean isLineBreak(String value) {
		return value.matches("\\r?\\n");
	}
	
	private boolean isCommentBeginning(String seq) {
		// TODO implement
		return false;
	}
}
