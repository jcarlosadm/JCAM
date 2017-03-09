package br.ufal.ic.JCAM;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class LexicalAnalyzerCaseTests {

	private static final String INPUT_01 = "procedimento inicio() {\n" + " escreva(\"Hello World!\");\n"
			+ "var _teste : _texto = \" \";" + "}";
	private static final String EXPECTED_OUTPUT_01 = "[ procedimento, 0 0, PR_CMD_PROC ]\n"
			+ "[ inicio, 0 13, PR_INICIO ]\n" + "[ (, 0 19, ABRE_PAR ]\n" + "[ ), 0 20, FECHA_PAR ]\n"
			+ "[ {, 0 22, ABRE_CH ]\n" + "[ escreva, 1 1, PR_ESCREVA ]\n" + "[ (, 1 8, ABRE_PAR ]\n"
			+ "[ \"Hello World!\", 1 9, CONST_TEXTO ]\n" + "[ ), 1 23, FECHA_PAR ]\n" + "[ ;, 1 24, SE_PONTOVIRGULA ]\n"
			+ "[ var, 2 0, PR_CMD_DECL_VAR ]\n" + "[ _, 2 4,  ]\n" + "[ teste, 2 5, ID ]\n"
			+ "[ :, 2 11, SE_DOISPONTOS ]\n" + "[ _, 2 13,  ]\n" + "[ texto, 2 14, PR_TIPO_TEXTO ]\n"
			+ "[ =, 2 20, OP_ATRIBUICAO ]\n" + "[ \" \", 2 22, CONST_TEXTO ]\n" + "[ ;, 2 25, SE_PONTOVIRGULA ]\n"
			+ "[ }, 2 26, FECHA_CH ]";

	@Test
	public void test() throws Exception {
		String test = INPUT_01;
		String output = "";

		BufferedReader bReader = new BufferedReader(new StringReader(test));
		LexicalAnalyzer lAnalyzer = new LexicalAnalyzer(bReader);

		Token token = lAnalyzer.nextToken();
		while (token.getCategory() != TokenCategory.EOF) {
			output += token.toString();

			if ((token = lAnalyzer.nextToken()).getCategory() != TokenCategory.EOF)
				output += "\n";
		}

		Assert.assertEquals(EXPECTED_OUTPUT_01, output);
	}

}
