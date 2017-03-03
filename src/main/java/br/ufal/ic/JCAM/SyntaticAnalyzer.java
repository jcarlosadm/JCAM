package br.ufal.ic.JCAM;

public class SyntaticAnalyzer {
	
	private LexicalAnalyzer lexicalAnalyzer;
	
	public SyntaticAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
		this.lexicalAnalyzer = lexicalAnalyzer;
	}
	
	public void run() {
		Token token = lexicalAnalyzer.nextToken();
		
		token.getLexicalValue();
	}
	
}
