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

		while ((currentLine = file.readLine()) != null)
			this.lines.add(currentLine);
	}

	public Token nextToken() {

		int fixedColumn = this.currentColumnNumber;
		int fixedLine = this.currentLineNumber;

		String charValue = "";
		
		// skip spaces
		do {
			int charInt = this.getNextChar();
			if (charInt == -1)
				return this.getEOFToken();

			charValue = String.valueOf((char) charInt);

			// skip line comments
			if (charValue.equals("/") && this.nextCharIsSlash()) {
				this.goToLineEnd();

				// go to beginning of next line
				charInt = this.getNextChar();
				if (charInt == -1)
					return this.getEOFToken();

				charValue = String.valueOf((char) charInt);
			}

		} while (this.isSpace(charValue) || this.isLineBreak(charValue));

		fixedColumn = this.currentColumnNumber;
		fixedLine = this.currentLineNumber;

		// try identify this char as Token
		while (this.isValidToken(charValue) || this.canGrown(charValue)) {
			if (!this.canGrown(charValue))
				break;

			int charInt = this.getNextChar();

			if (charInt == -1 || this.isSpace(String.valueOf((char) charInt))
					|| this.isLineBreak(String.valueOf((char) charInt)) || fixedLine != this.currentLineNumber)
				break;

			if (this.canGrown(charValue) && !this.isValidToken(charValue + String.valueOf((char) charInt)))
				break;

			charValue += String.valueOf((char) charInt);
		}

		TokenCategory tokenCategory = this.getTokenCategory(charValue);

		// unknown token
		if (tokenCategory == null)
			return this.getUnknownToken(charValue, fixedColumn, fixedLine);
		// token found!
		else
			this.currentToken = new Token(charValue, new Position(fixedLine, fixedColumn), tokenCategory);

		return this.currentToken;
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
		else {
			// check end of file
			if (this.currentColumnNumber >= this.currentLineString.length()
					&& (currentLineNumber + 1) >= this.lines.size())
				return -1;

			++this.currentColumnNumber;
		}

		// while end of line, get next line
		while (this.currentColumnNumber >= this.currentLineString.length()) {
			if (this.currentLineNumber + 1 < this.lines.size()) {
				this.currentColumnNumber = 0;
				++this.currentLineNumber;
				this.currentLineString = this.lines.get(this.currentLineNumber);
			} else
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

	private Token getEOFToken() {
		this.currentToken = new Token(null, new Position(this.currentLineNumber, this.currentColumnNumber),
				TokenCategory.EOF);
		return this.currentToken;
	}

	private boolean nextCharIsSlash() {
		if (this.currentColumnNumber + 1 >= this.currentLineString.length())
			return false;

		return this.currentLineString.substring(currentColumnNumber + 1, currentColumnNumber + 2).equals("/");
	}

	private void goToLineEnd() {
		this.currentColumnNumber = this.currentLineString.length() - 1;
	}

	private Token getUnknownToken(String charValue, int column, int line) {
		this.currentToken = new Token(charValue, new Position(line, column), null);
		return this.currentToken;
	}

	/**
	 * check if this token is a valid token
	 * 
	 * @param charValue
	 * @return true if this is a valid token
	 */
	private boolean isValidToken(String charValue) {
		// TODO implement
		return false;
	}

	/**
	 * check if this token can grown. example: a ID can grown, and a letter 'i'
	 * can be 'if' later
	 * 
	 * @param charValue
	 * @return true if this can grown
	 */
	private boolean canGrown(String charValue) {
		// TODO implement
		return false;
	}

	/**
	 * get a token category
	 * 
	 * @param charValue
	 * @return a token category, or null
	 */
	private TokenCategory getTokenCategory(String charValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
