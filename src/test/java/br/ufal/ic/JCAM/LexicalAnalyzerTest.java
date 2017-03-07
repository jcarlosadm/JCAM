package br.ufal.ic.JCAM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class LexicalAnalyzerTest {
	
	public BufferedReader getFileReader(String fileName) {
		try {
			return new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	@Test
	public void test_001() {
		Assert.assertNotEquals(null, getFileReader("files/test001.jcam"));
		
		LexicalAnalyzer lexicalAnalyzer;
		try {
			lexicalAnalyzer = new LexicalAnalyzer(getFileReader("files/test001.jcam"));
			
			Token currentToken = lexicalAnalyzer.nextToken();
			while(currentToken.getCategory() != TokenCategory.EOF) {
				System.out.println(currentToken);
				currentToken = lexicalAnalyzer.nextToken();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
