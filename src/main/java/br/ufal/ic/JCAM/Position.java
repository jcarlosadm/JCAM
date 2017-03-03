package br.ufal.ic.JCAM;

public class Position {
	
	private Integer line;
	private Integer column;
	
	public Position(Integer line, Integer column) {
		this.line = line;
		this.column = column;
	}
	
	public Integer getLine() {
		return line;
	}
	
	public Integer getColumn() {
		return column;
	}
	
}
