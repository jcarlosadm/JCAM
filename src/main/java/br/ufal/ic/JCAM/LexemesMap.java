package br.ufal.ic.JCAM;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LexemesMap {

	private static Map<String, TokenCategory> lexemes;
	
	static {
		lexemes = new HashMap<String, TokenCategory>();
		
		// ID
		// CONST_INT
		// CONST_REAL
		
		// CONST_BOOL
		lexemes.put("verdadeiro", TokenCategory.CONST_BOOL);
		lexemes.put("falso", TokenCategory.CONST_BOOL);		
		
		// CONST_TEXTO
		// CONST_CARACTERE
		
		lexemes.put("var", TokenCategory.PR_CMD_DECL_VAR);
		lexemes.put("const", TokenCategory.PR_CMD_DECL_CONST);
		
		lexemes.put("=", TokenCategory.OP_ATRIBUICAO);
		
		lexemes.put("+", TokenCategory.OP_ARIT_ADD);
		lexemes.put("-", TokenCategory.OP_ARIT_ADD);
		lexemes.put("*", TokenCategory.OP_ARIT_MUL);
		lexemes.put("/", TokenCategory.OP_ARIT_MUL);
		lexemes.put("^", TokenCategory.OP_ARIT_EXP);
		
		lexemes.put("<", TokenCategory.OP_RELACIONAL_1);
		lexemes.put(">", TokenCategory.OP_RELACIONAL_1);
		lexemes.put("<=", TokenCategory.OP_RELACIONAL_1);
		lexemes.put(">=", TokenCategory.OP_RELACIONAL_1);

		lexemes.put("!=", TokenCategory.OP_RELACIONAL_2);
		lexemes.put("==", TokenCategory.OP_RELACIONAL_2);

		lexemes.put("e", TokenCategory.OP_BOOL_E);
		lexemes.put("nao", TokenCategory.OP_BOOL_NAO);
		lexemes.put("ou", TokenCategory.OP_BOOL_OU);
		
		lexemes.put("inicio", TokenCategory.PR_INICIO);
		lexemes.put("se", TokenCategory.PR_CMD_SE);
		lexemes.put("senao", TokenCategory.PR_CMD_SENAO);
		lexemes.put("enquanto", TokenCategory.PR_CMD_ENQUANTO);
		lexemes.put("para", TokenCategory.PR_CMD_PARA);
		lexemes.put("ate", TokenCategory.PR_CMD_PARA_ATE);
		lexemes.put("passo", TokenCategory.PR_CMD_PARA_PASSO);
		lexemes.put("proc", TokenCategory.PR_CMD_PROC);
		lexemes.put("func", TokenCategory.PR_CMD_FUNC);
		lexemes.put("retorne", TokenCategory.PR_CMD_RETORNE);
		
		lexemes.put("inteiro", TokenCategory.PR_TIPO_INTEIRO);
		lexemes.put("real", TokenCategory.PR_TIPO_REAL);
		lexemes.put("booleano", TokenCategory.PR_TIPO_BOOLEANO);
		lexemes.put("caractere", TokenCategory.PR_TIPO_CARACTERE);
		lexemes.put("texto", TokenCategory.PR_TIPO_TEXTO);
		
		lexemes.put(",", TokenCategory.SE_VIRGULA);
		lexemes.put(";", TokenCategory.SE_PONTOVIRGULA);
		lexemes.put(":", TokenCategory.SE_DOISPONTOS);
		
		lexemes.put("(", TokenCategory.ABRE_PAR);
		lexemes.put(")", TokenCategory.FECHA_PAR);
		lexemes.put("[", TokenCategory.ABRE_COL);
		lexemes.put("]", TokenCategory.FECHA_COL);
		lexemes.put("{", TokenCategory.ABRE_CH);
		lexemes.put("}", TokenCategory.FECHA_CH);
	}
	
	public LexemesMap() {

	}
	
	public static TokenCategory getTokenCategory(String charValue) {
		if (lexemes.containsKey(charValue)) {
			return lexemes.get(charValue);
		}
		
		return null;
	}
	
	public static Set<String> getKeys() {
		return lexemes.keySet();
	}
	
}
