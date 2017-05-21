package br.ufal.ic.JCAM;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Random;

import org.junit.Test;

public class LexicalAnalyzerRandomTest {

	private static final int MAX_NUMBER_OF_TOKENS = 500;

	private static final int MAX_ID_SIZE = 100;

	private StringBuilder input = new StringBuilder();
	private StringBuilder output = new StringBuilder();

	private int line = 0, column = 0;

	private boolean lineCommented = false;

	private StringBuilder cummulativeID = new StringBuilder();
	private int cummulativeIdColumn = 0;

	@Test
	public void test() throws Exception {

		Random rnd = new Random();
		int numberOfTokens = rnd.nextInt(MAX_NUMBER_OF_TOKENS);
		int numberOfMethods = 4;

		for (int i = 0; i < numberOfTokens; i++) {
			int choice = rnd.nextInt(numberOfMethods);

			switch (choice) {
			case 0:
				this.insertSpace();
				break;
			case 1:
				this.insertComment();
				break;
			case 2:
				this.insertNewLine();
				break;
			case 3:
				this.insertID();
				break;
			}
		}

		BufferedReader bReader = new BufferedReader(new StringReader(this.input.toString()));
		LexicalAnalyzer lAnalyzer = new LexicalAnalyzer(bReader);
		StringBuilder output = new StringBuilder();

		Token token = null;
		while ((token = lAnalyzer.nextToken()).getCategory() != TokenCategory.EOF) {
			output.append(token.toString() + "\n");
		}

		// System.out.println(this.input);
		// System.out.println(this.output);
		// System.out.println(output);

		assertEquals(this.output.toString(), output.toString());

	}

	private void insertSpace() {
		this.insertCummulativeID();

		this.input.append(" ");
		++this.column;
	}

	private void insertNewLine() {
		this.insertCummulativeID();

		this.input.append("\n");
		++this.line;
		this.column = 0;
		this.lineCommented = false;
	}

	private void insertComment() {
		this.insertCummulativeID();

		this.input.append("//");
		this.column += 2;
		this.lineCommented = true;
	}

	private void insertID() {

		if (this.cummulativeID.toString().isEmpty())
			this.cummulativeIdColumn = this.column;

		Random rnd = new Random();
		int numberOfChars = 1 + rnd.nextInt(MAX_ID_SIZE);
		StringBuilder input = new StringBuilder();

		boolean first = true;
		int choice = 0;
		for (int i = 0; i < numberOfChars; i++) {
			++this.column;

			if (first == true) {
				choice = 2;
				first = false;
			} else
				choice = rnd.nextInt(3);

			if (choice == 0)
				input.append("_");
			else if (choice == 1)
				input.append(rnd.nextInt(10));
			else {
				choice = rnd.nextInt(2);

				if (choice == 0) {
					choice = 65 + rnd.nextInt((90 - 65) + 1);
					input.append(((char) choice));
				} else {
					choice = 97 + rnd.nextInt((122 - 97) + 1);
					input.append(((char) choice));
				}
			}
		}

		this.input.append(input);
		if (this.lineCommented == false)
			this.cummulativeID.append(input);
	}

	private void insertCummulativeID() {
		if (!this.cummulativeID.toString().isEmpty()) {
			this.output.append((new Token(this.cummulativeID.toString(),
					new Position(this.line, this.cummulativeIdColumn), TokenCategory.ID)).toString() + "\n");
			this.cummulativeID = new StringBuilder();
		}
	}

}
