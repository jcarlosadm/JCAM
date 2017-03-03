package br.ufal.ic.JCAM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Analyzer {

	private String fileName;
	
	public Analyzer(String fileName) {
		this.fileName = fileName;
	}
	
	public void run() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(this.fileName));
			
			SyntaticAnalyzer syntaticAnalyzer = new SyntaticAnalyzer(new LexicalAnalyzer(file));
			syntaticAnalyzer.run();
			
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado!");
		}
	}
}
