package br.ufal.ic.JCAM;

import java.io.BufferedReader;

public class LexicalAnalyzer {

	private BufferedReader file;
	private Token current;
	
	public LexicalAnalyzer(BufferedReader file) {
		this.file = file;
	}
	
	public Token nextToken() {
		
		return current;
	}

}
