package br.ufal.ic.JCAM;

public class Token {
	
	private String lexicalValue;
	private Position position;
	private TokenCategory category;

	public Token(String lexicalValue, Position position, TokenCategory category) {
		this.lexicalValue = lexicalValue;
		this.position = position;
		this.category = category;
	}
	
	public String getLexicalValue() {
		return lexicalValue;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public TokenCategory getCategory() {
		return category;
	}
	
}
