package br.ufal.ic.JCAM;

import java.util.Arrays;
import java.util.List;

public class SyntaticAnalyzer {
	
	private LexicalAnalyzer lexicalAnalyzer;
	private Token currentToken;	
	
	private static List<TokenCategory> typesCategory;
	
	static {
		typesCategory = Arrays.asList(
			TokenCategory.PR_TIPO_BOOLEANO,
			TokenCategory.PR_TIPO_CARACTERE,
			TokenCategory.PR_TIPO_INTEIRO,
			TokenCategory.PR_TIPO_REAL,
			TokenCategory.PR_TIPO_TEXTO
		);
	}
	
	public SyntaticAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
		this.lexicalAnalyzer = lexicalAnalyzer;
	}
	
	private Boolean updateToken() {
		Token current = this.lexicalAnalyzer.nextToken();
		
		if(current != null) {
			this.currentToken = current;
			
			return true;
		} 

		// TODO: Lançar erro: não existem mais tokens para serem analisados
		
		return false;	
	}
	
	public void printErrorMsg(ErrorMsg error, Token token) { 
		Position pos = token.getPosition();
		System.out.println("Token " + token.getLexicalValue() + 
				" inválido na linha: " + pos.getLine().intValue() + 
				" coluna: " + pos.getColumn().intValue() + 
				", " + error.getMsg());	
	
	}	
		
	// TODO: continuar implementação...
	public void Decl() {
		ModDecl();
		if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.ID) {
			if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				// DeclTipoAtrib();
			} else {
				// Erro: dois pontos esperado
			}
		} else {
			// Erro: ID esperado
		}
	}
	
	// TODO: continuar implementação...
	public void ModDecl() {
		if(this.updateToken()) {
			TokenCategory category = this.currentToken.getCategory();
			
			if(category == TokenCategory.PR_CMD_DECL_VAR) {
			
			} else if(category == TokenCategory.PR_CMD_DECL_CONST) {
				
			} else {
				// Erro declaração inválida (var | const) esperado
			}
		}

	}
	
	// TODO: continuar implementação...	
	public void DeclTipoAtrib() {
		if(this.updateToken()) {
			TokenCategory category = this.currentToken.getCategory();
			
			if(typesCategory.contains(category)) {
				
				// DeclAtribTipo();
				
			} else {
				System.out.println("Erro: tipo não esperado " + this.currentToken);
				// Erro: tipo não esperado
			}
		}
	}
	
	public void Matriz() {
		if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.ABRE_COL) {
			if(this.updateToken()) {
				TokenCategory category = this.currentToken.getCategory();
				
				if(typesCategory.contains(category)) {
					if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {						
						if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.CONST_INT) {
							if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.FECHA_COL) {
								if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
									System.out.println("def matriz válida");
								} else {
									printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA, this.currentToken);
								}
							} else {
								printErrorMsg(ErrorMsg.NOTFOUND_FECHA_COL, this.currentToken);
							}
						} else {
							printErrorMsg(ErrorMsg.CONST_INT, this.currentToken);
						}
					} else {
						printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA, this.currentToken);
					}
				} else {
					printErrorMsg(ErrorMsg.INVALID_TYPE, this.currentToken);
				}
			}
		} else {
			printErrorMsg(ErrorMsg.NOTFOUND_ABRE_COL, this.currentToken);
		}
	}

	
	// TODO: Como tratar o erro e prosseguir a análise? 	 * 	
	public void Param() {
		if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.ID) {
			if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				if(this.updateToken()) {
					TokenCategory category = this.currentToken.getCategory();
					
					if(typesCategory.contains(category)) {
						
						if(this.updateToken() && this.currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
							System.out.println("def param válido");
						} else {
							printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA, this.currentToken);
						}
						
					} else {
						printErrorMsg(ErrorMsg.INVALID_TYPE, this.currentToken);
						// Situação de erro: Tipo não suportado
					}					
				}
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_SE_DOISPONTOS, this.currentToken);
				// Situação de erro: Separador ":" esperado
			}
		} else {
			printErrorMsg(ErrorMsg.INVALID_ID, this.currentToken);
			// Situação de erro: ID esperado
		}
	}
	
	public void run() {
		
		//if(this.updateToken()) {
		//	System.out.println(this.currentToken.getLexicalValue());
		//}
		
		// TODO: remover após testes ~> arquivo testes.jcam
		Param();
		Param();
		Matriz();
	}
	
}
