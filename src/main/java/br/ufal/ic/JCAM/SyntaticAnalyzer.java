package br.ufal.ic.JCAM;

import java.util.Arrays;
import java.util.List;

public class SyntaticAnalyzer {

	private LexicalAnalyzer lexicalAnalyzer;
	private Token currentToken;

	// se houver um ou mais erros, mudar para false
	private boolean success = true;

	private static List<TokenCategory> typesCategory;
	// private static List<TokenCategory> commandsWithScope;
	// private static List<TokenCategory> commandsWithoutScope;

	static {
		typesCategory = Arrays.asList(TokenCategory.PR_TIPO_BOOLEANO, TokenCategory.PR_TIPO_CARACTERE,
				TokenCategory.PR_TIPO_INTEIRO, TokenCategory.PR_TIPO_REAL, TokenCategory.PR_TIPO_TEXTO);

	}

	public SyntaticAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
		this.lexicalAnalyzer = lexicalAnalyzer;
	}

	private void updateToken() {
		this.currentToken = this.lexicalAnalyzer.nextToken();

		if (this.currentToken == null) {
			this.errorMsg("erro inesperado");
		}

		else {
			while (this.currentToken.getCategory() == null) {
				this.errorMsg("token não esperado");

				this.currentToken = this.lexicalAnalyzer.nextToken();
				if (this.currentToken == null) {
					this.errorMsg("erro inesperado");
					break;
				}
			}
		}

	}

	public boolean isSuccess() {
		return this.success;
	}

	public Boolean haveToken() {
		if (this.currentToken.getCategory() != TokenCategory.EOF) {
			return true;
		}

		return false;
	}

	private void errorMsg(String msg) {
		if (this.currentToken == null)
			System.out.println("erro inesperado");
		else
			System.out.println("token: " + this.currentToken.getLexicalValue() + "." + msg + ". linha "
					+ this.currentToken.getPosition().getLine() + ", coluna "
					+ this.currentToken.getPosition().getColumn() + ".");
		this.success = false;
	}

	public void Escopo() {
		if (currentToken.getCategory() == TokenCategory.ABRE_CH) {
			updateToken();

			if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
				updateToken();

				// LCmd();

				System.out.println("escopo válido!");
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_FECHA_CH, this.currentToken);
			}
		} else {
			printErrorMsg(ErrorMsg.NOTFOUND_ABRE_CH, this.currentToken);
		}

	}

	public void LCmd() {

	}

	public void Cmd() {
		CmdSemEscopo();

		if (this.currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
			updateToken();

			//

		}
	}

	// TODO: Remover ambiguidade todas elas começam com TokenCategory.ID, qual
	// produção escolher ???
	public void CmdSemEscopo() {
		Atrib(); // = após ID
		Decl(); // sem problemas, começa com var ou const
		ChVarConst(); // acesso a matriz, [ após ID (em AcMatriz)
		ChFunc(); // começa com ID, igual a ChProc
		ChProc(); // coma com ID, igual a ChFunc
	}

	public void CmdComEscopo() {
		// Se();
		// Enquanto();
		// Para();
	}

	public void LDeclGlob() {
		DeclGlob();

		if (currentToken.getCategory() == TokenCategory.PR_CMD_DECL_GLOBAL) {
			updateToken();
			LDeclGlob();
		}
	}

	public void DeclGlob() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_DECL_GLOBAL) {
			updateToken();

			Decl();

			if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
				updateToken();

				System.out.println("def global válida!");

			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA, currentToken);
			}
		} else {
			System.out.println("Error");
		}
	}

	public void Decl() {
		ModDecl();
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				updateToken();
				System.out.println(currentToken);

				DeclTipoAtrib();
				System.out.println("def atrib tipo valida!");
			}
		} else {
			printErrorMsg(ErrorMsg.INVALID_ID, this.currentToken);
		}
	}

	public void ModDecl() {
		TokenCategory category = currentToken.getCategory();
		if (category == TokenCategory.PR_CMD_DECL_CONST || category == TokenCategory.PR_CMD_DECL_VAR) {
			updateToken();
		}
	}

	public void DeclTipoAtrib() {
		TokenCategory category = currentToken.getCategory();

		if (typesCategory.contains(category)) {
			updateToken();

			DeclAtribTipo();
		} else {
			System.out.println("matriz " + currentToken);
			Matriz();
			DeclAtribMatriz();
		}

	}

	public void DeclAtribTipo() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			updateToken();

			VAtrib();
		}
	}

	public void DeclAtribMatriz() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_CH) {
				updateToken();

				LArg();

				if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
					updateToken();
					System.out.println("def matriz com atribuição válida!");
				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_FECHA_CH, this.currentToken);
				}
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_ABRE_CH, this.currentToken);
			}
		}
	}

	public void Matriz() {
		// this.updateToken();
		if (this.currentToken.getCategory() == TokenCategory.ABRE_COL) {
			if (this.updateToken()) {

				TokenCategory category = this.currentToken.getCategory();

				if (typesCategory.contains(category)) {

					this.updateToken();

					if (this.currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
						// System.out.println(currentToken);

						this.updateToken();
						if (this.currentToken.getCategory() == TokenCategory.CONST_INT) {
							this.updateToken();
							if (this.currentToken.getCategory() == TokenCategory.FECHA_COL) {
								this.updateToken();
								// if (this.currentToken.getCategory() ==
								// TokenCategory.SE_PONTOVIRGULA) {
								System.out.println("def matriz válida");
								// this.updateToken();
								// } else {
								// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
								// this.currentToken);
								// }
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
		} else
			printErrorMsg(ErrorMsg.NOTFOUND_ABRE_COL, this.currentToken);
	}

	public void ChVarConst() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();
			AcMatriz();
		}
	}

	public void AcMatriz() {
		if (currentToken.getCategory() == TokenCategory.ABRE_COL) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.CONST_INT) {
				updateToken();
				if (currentToken.getCategory() == TokenCategory.FECHA_COL) {
					updateToken();
					// if(this.currentToken.getCategory() ==
					// TokenCategory.SE_PONTOVIRGULA) {
					// this.updateToken();
					System.out.println("def acesso matriz válido");
					// } else {
					// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
					// this.currentToken);
					// }
				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_FECHA_COL, this.currentToken);
				}
			} else {
				printErrorMsg(ErrorMsg.CONST_INT, this.currentToken);
			}
		}
	}

	public void Retorno() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_RETORNE) {
			updateToken();
			VAtrib();

			// if (this.currentToken.getCategory() ==
			// TokenCategory.SE_PONTOVIRGULA) {
			// this.updateToken();
			System.out.println("def retorne válida!");
			// } else {
			// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
			// this.currentToken);
			// }
		} else {
			// Erro?
		}
	}

	public void Atrib() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();
			AcMatriz();
			if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
				updateToken();
				// System.out.println(this.currentToken);
				VAtrib();

				// if (this.currentToken.getCategory() ==
				// TokenCategory.SE_PONTOVIRGULA) {
				// this.updateToken();
				System.out.println("def atribuição válida");

				// } else {
				// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
				// this.currentToken);
				// }
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_OP_ATRIB, this.currentToken);
			}
		} else {
			printErrorMsg(ErrorMsg.INVALID_ID, this.currentToken);
		}
	}

	public void VAtrib() {
		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("valido");
			this.updateToken();
			AcMatriz();
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_BOOL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			this.updateToken();
			// if (this.currentToken.getCategory() ==
			// TokenCategory.SE_PONTOVIRGULA) {
			System.out.println("VAtrib tipo válido");
			// this.updateToken();
			// } else {
			// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
			// this.currentToken);
			// }
			break;

		default:
			// Concat();
			// Expr();
			// ChFunc();
			System.out.println("atribuição inválida!");
			break;
		}
	}

	public void LParam() {
		Param();
		// if (this.currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
		LParamNr();
		// }
	}

	public void LParamNr() {
		if (currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
			updateToken();

			Param();

			// System.out.println(this.currentToken);

			LParamNr();
		}
	}

	// TODO: Como tratar o erro e prosseguir a análise? *
	public void Param() {
		// this.updateToken();
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				if (updateToken()) {
					TokenCategory category = currentToken.getCategory();

					if (typesCategory.contains(category)) {
						updateToken();
						// if (this.currentToken.getCategory() ==
						// TokenCategory.SE_PONTOVIRGULA) {
						System.out.println("def param válido");
						// this.updateToken();
						// } else {
						// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
						// this.currentToken);
						// }

					} else {
						printErrorMsg(ErrorMsg.INVALID_TYPE, currentToken);
						// Situação de erro: Tipo não suportado
					}
				}
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_SE_DOISPONTOS, currentToken);
				// Situação de erro: Separador ":" esperado
			}
		} // else {
			// printErrorMsg(ErrorMsg.INVALID_ID, this.currentToken);
			// Situação de erro: ID esperado
			// }
	}

	public void LFuncProc() {

	}

	// TODO: Falta função CorpoFunc()
	public void Func() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_FUNC) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.ID) {
				updateToken();
				if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
					updateToken();
					LParam();

					if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
						updateToken();

						if (currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
							updateToken();

							if (typesCategory.contains(currentToken.getCategory())) {
								updateToken();

								if (currentToken.getCategory() == TokenCategory.ABRE_CH) {
									updateToken();

									// CorpoFunc();

									if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
										updateToken();

										if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
											updateToken();

											System.out.println("def func válida!");
										} else {
											printErrorMsg(ErrorMsg.NOTFOUND_FECHA_CH, currentToken);
										}
									} else {
										printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA, currentToken);
									}
								} else {
									printErrorMsg(ErrorMsg.NOTFOUND_ABRE_CH, currentToken);
								}
							} else {
								printErrorMsg(ErrorMsg.INVALID_TYPE, currentToken);
							}
						} else {
							printErrorMsg(ErrorMsg.NOTFOUND_SE_DOISPONTOS, currentToken);
						}

					} else {
						printErrorMsg(ErrorMsg.NOTFOUND_FECHA_PAR, currentToken);
					}

				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_ABRE_PAR, currentToken);
				}
			} else {
				printErrorMsg(ErrorMsg.INVALID_ID, currentToken);
			}
		}
	}

	public void LProc() {
		Proc();
		if (currentToken.getCategory() == TokenCategory.PR_CMD_PROC) {
			LProc();
		}
	}

	public void Proc() {
		// System.out.println(this.currentToken);
		if (currentToken.getCategory() == TokenCategory.PR_CMD_PROC) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.ID) {
				updateToken();
				if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
					updateToken();
					LParam();

					if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
						updateToken();
						Escopo();

						// if (this.currentToken.getCategory() ==
						// TokenCategory.SE_PONTOVIRGULA) {
						// this.updateToken();
						System.out.println("def proc válido");

						// } else {
						// printErrorMsg(ErrorMsg.NOTFOUND_SE_PONTOVIRGULA,
						// this.currentToken);
						// }
					} else {
						printErrorMsg(ErrorMsg.NOTFOUND_FECHA_PAR, currentToken);
					}
				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_ABRE_PAR, currentToken);
				}
			} else if (currentToken.getCategory() == TokenCategory.PR_INICIO) {
				//
			} else {
				printErrorMsg(ErrorMsg.INVALID_ID, currentToken);
			}
		}

	}

	public void ChFunc() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				LArg();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();
					System.out.println("def ch func valida!");
				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_FECHA_PAR, currentToken);
				}
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_ABRE_PAR, currentToken);
			}
		}
	}

	public void Inicio() {
		if (currentToken.getCategory() == TokenCategory.PR_INICIO) {
			updateToken(); // token agr passa a ser (

		}
	}

	public void ChProc() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				LArg();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();
					System.out.println("def ch proc válida!");
				} else {
					printErrorMsg(ErrorMsg.NOTFOUND_FECHA_PAR, currentToken);
				}
			} else {
				printErrorMsg(ErrorMsg.NOTFOUND_ABRE_PAR, currentToken);
			}
		}
	}

	public void LArg() {
		VAtrib();

		// if(this.currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
		LArgNr();
		// }
	}

	public void LArgNr() {
		if (this.currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
			this.updateToken();
			VAtrib();

			LArgNr();
		} else {

		}
	}

	public void Expr() {
		TokenCategory tkCateg = this.currentToken.getCategory();

		if (tkCateg == TokenCategory.ABRE_PAR) {
			System.out.println("Expr = TR");
			TR();
		}

		else if (tkCateg == TokenCategory.ID) {
			System.out.println("Expr = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB");
			this.updateToken();
			TR2();
			OPB();
		}

		else if (tkCateg == TokenCategory.CONST_INT || tkCateg == TokenCategory.CONST_REAL
				|| tkCateg == TokenCategory.CONST_CARACTERE || tkCateg == TokenCategory.CONST_TEXTO) {
			System.out.println("Expr = NUMEROTEXTO OPB2");
			NUMEROTEXTO();
			OPB2();
		}

		else if (tkCateg == TokenCategory.CONST_BOOL) {
			System.out.println("Expr = \"constBool\"(" + this.currentToken.getLexicalValue() + ") OPB3");
			this.updateToken();
			OPB3();
		}

		else if (tkCateg == TokenCategory.OP_ARIT_ADD) {
			System.out.println("Expr = OPU");
			OPU();
		}

		else if (tkCateg == TokenCategory.OP_BOOL_NAO) {
			System.out.println("Expr = OPU2");
			OPU2();
		}

		else
			errorMsg("token não esperado");
	}

	private void TR() {
		// TODO Auto-generated method stub

	}

	private void OPU2() {
		// TODO Auto-generated method stub

	}

	private void OPU() {
		// TODO Auto-generated method stub

	}

	private void NUMEROTEXTO() {
		// TODO Auto-generated method stub

	}

	private void TR6() {
		// TODO Auto-generated method stub

	}

	private void TR11() {
		// TODO Auto-generated method stub

	}

	private void OPB3() {
		// TODO Auto-generated method stub

	}

	private void OPB2() {
		// TODO Auto-generated method stub

	}

	private void OPB() {
		// TODO Auto-generated method stub

	}

	private void TR2() {
		// TODO Auto-generated method stub

	}

	private void TR3() {
		// TODO Auto-generated method stub

	}

	public void run() {

		// if(this.updateToken()) {
		// System.out.println(this.currentToken.getLexicalValue());
		// }

		// TODO: remover após testes ~> arquivo testes.jcam
		if (this.updateToken()) {
			Param();
			Param();
			Matriz();
			LProc();
			System.out.println(this.currentToken);
			AcMatriz();
			System.out.println(this.currentToken);
			VAtrib();
			System.out.println(this.currentToken);
			VAtrib();
			System.out.println(this.currentToken);
			Atrib();
			System.out.println(this.currentToken);
			Atrib();
			System.out.println(this.currentToken);
			Retorno();
			System.out.println(this.currentToken);
			LParam();
			System.out.println(this.currentToken);
			LArg();
			System.out.println(this.currentToken);
			ChProc();
			System.out.println(this.currentToken);
			Decl();
			System.out.println(this.currentToken);
			Decl();
			System.out.println(this.currentToken);
			Func();
			System.out.println(this.currentToken);
			LDeclGlob();
			System.out.println(this.currentToken);
		}
	}

}
