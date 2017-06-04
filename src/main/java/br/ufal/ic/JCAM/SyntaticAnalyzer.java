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

	private void errorMsg(String msg) {
		if (this.currentToken == null)
			System.out.println("erro inesperado");
		else
			System.out.println("ERRO. token: " + this.currentToken.getLexicalValue() + "." + msg + ". linha "
					+ this.currentToken.getPosition().getLine() + ", coluna "
					+ this.currentToken.getPosition().getColumn() + ".");
		this.success = false;
	}

	private void Programa() {
		System.out.println("Programa = LOptDecl Inicio");

		LOptDecl();
		Inicio();
	}

	private void LOptDecl() {
		System.out.println("LOptDecl = LDeclGlob LFuncProc");

		LDeclGlob();
		LFuncProc();
	}

	private void Escopo() {
		if (currentToken.getCategory() == TokenCategory.ABRE_CH) {
			System.out.println("Escopo = \"{\" LCmd \"}\"");
			updateToken();

			LCmd();

			if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
				updateToken();
			} else {
				errorMsg("\"}\" esperado");
			}
		} else {
			errorMsg("\"{\" esperado");
		}

	}

	private void LCmd() {
		switch (currentToken.getCategory()) {
		case ID:
		case PR_CMD_DECL_VAR:
		case PR_CMD_DECL_CONST:
		case PR_CMD_SE:
		case PR_CMD_ENQUANTO:
		case PR_CMD_PARA:
		case PR_ESCREVA:
		case PR_LEIA:
			System.out.println("LCmd = Cmd LCmd");
			Cmd();
			LCmd();
			break;

		default:
			System.out.println("LCmd = epsilon");
			break;
		}
	}

	private void Cmd() {
		switch (currentToken.getCategory()) {
		case ID:
		case PR_CMD_DECL_VAR:
		case PR_CMD_DECL_CONST:
		case PR_ESCREVA:
		case PR_LEIA:
			CmdSemEscopo();

			if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
				updateToken();
			} else {
				errorMsg("\";\" esperado");
			}

			break;

		case PR_CMD_SE:
		case PR_CMD_ENQUANTO:
		case PR_CMD_PARA:
			CmdComEscopo();
			break;

		default:
			errorMsg("token não esperado");
			break;
		}
	}

	private void CmdSemEscopo() {
		switch (currentToken.getCategory()) {
		case ID:
			System.out.println("CmdSemEscopo = \"id\"(" + currentToken.getLexicalValue() + ") CmdSemEscopoR");
			updateToken();
			CmdSemEscopoR();
			break;

		case PR_CMD_DECL_VAR:
		case PR_CMD_DECL_CONST:
			System.out.println("CmdSemEscopo = Decl");
			Decl();
			break;

		case PR_ESCREVA:
			System.out.println("CmdSemEscopo = Escreva");
			Escreva();
			break;

		case PR_LEIA:
			System.out.println("CmdSemEscopo = Leia");
			Leia();
			break;

		default:
			errorMsg("token não esperado");
			break;
		}
	}

	private void Escreva() {
		if (currentToken.getCategory() == TokenCategory.PR_ESCREVA) {
			System.out.println("Escreva = \"escreva\" \"(\" Expr \")\"");
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				Expr();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();
				} else {
					errorMsg("\")\" esperado");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"escreva\" esperado");
		}
	}

	private void Leia() {
		if (currentToken.getCategory() == TokenCategory.PR_LEIA) {
			System.out.println("Leia = \"leia\" \"(\" \"id\" AcMatriz \")\"");
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				if (currentToken.getCategory() == TokenCategory.ID) {
					updateToken();

					AcMatriz();

					if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
						updateToken();
					} else {
						errorMsg("\")\" esperado");
					}
				} else {
					errorMsg("\"id\" invalido");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"leia\" esperado");
		}
	}

	private void CmdSemEscopoR() {
		switch (currentToken.getCategory()) {

		case ABRE_PAR:
			System.out.println("CmdSemEscopoR = ChFuncProcR");
			ChFuncProcR();
			break;

		default:
			System.out.println("CmdSemEscopoR = AcMatriz CmdSemEscopoR2");
			AcMatriz();
			CmdSemEscopoR2();
			break;
		}
	}

	private void CmdSemEscopoR2() {
		switch (currentToken.getCategory()) {
		case OP_ATRIBUICAO:
			System.out.println("CmdSemEscopoR2 = AtribR");
			AtribR();
			break;

		default:
			System.out.println("CmdSemEscopoR2 = epsilon");
			break;
		}
	}

	private void CmdComEscopo() {
		switch (currentToken.getCategory()) {
		case PR_CMD_SE:
			System.out.println("CmdComEscopo = Se");
			Se();
			break;

		case PR_CMD_ENQUANTO:
			System.out.println("CmdComEscopo = Enquanto");
			Enquanto();
			break;

		case PR_CMD_PARA:
			System.out.println("CmdComEscopo = Para");
			Para();
			break;

		default:
			errorMsg("token não esperado");
			break;
		}
	}

	private void Se() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_SE) {
			System.out.println("Se = \"se\" \"(\" TR6 \")\" Escopo Senao");
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				TR6();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();

					Escopo();
					Senao();
				} else {
					errorMsg("\")\" esperado");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"se\" esperado");
		}
	}

	private void Senao() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_SENAO) {
			System.out.println("Senao = \"senao\" Escopo");
			updateToken();

			Escopo();
		} else {
			System.out.println("Senao = epsilon");
		}
	}

	private void Enquanto() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_ENQUANTO) {
			System.out.println("Enquanto = \"enquanto\" \"(\" TR6 \")\" Escopo");
			this.updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				TR6();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();

					Escopo();
				} else {
					errorMsg("\")\" esperado");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"enquanto\" esperado");
		}
	}

	private void Para() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_PARA) {
			System.out.println(
					"Para = \"para\" \"(\" ParaExpr \")\" \"ate\" \"(\" TR6 \")\" \"passo\" \"(\" ParaExpr \")\" Escopo");
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				ParaExpr();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();

					if (currentToken.getCategory() == TokenCategory.PR_CMD_PARA_ATE) {
						updateToken();

						if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
							updateToken();

							TR6();

							if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
								updateToken();

								if (currentToken.getCategory() == TokenCategory.PR_CMD_PARA_PASSO) {
									updateToken();

									if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
										updateToken();

										ParaExpr();

										if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
											updateToken();

											Escopo();
										} else {
											errorMsg("\")\" esperado");
										}
									} else {
										errorMsg("\"(\" esperado");
									}
								} else {
									errorMsg("\"passo\" esperado");
								}
							} else {
								errorMsg("\")\" esperado");
							}
						} else {
							errorMsg("\"(\" esperado");
						}
					} else {
						errorMsg("\"ate\" esperado");
					}
				} else {
					errorMsg("\")\" esperado");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"para\" esperado");
		}
	}

	private void ParaExpr() {
		switch (currentToken.getCategory()) {
		case ID:
			System.out.println("ParaExpr = \"id\"(" + currentToken.getLexicalValue() + ") AcMatriz ParaExprR");
			updateToken();
			AcMatriz();
			ParaExprR();
			break;

		case PR_CMD_DECL_VAR:
		case PR_CMD_DECL_CONST:
			System.out.println("ParaExpr = Decl");
			Decl();

		default:
			errorMsg("token não esperado");
			break;
		}
	}

	private void ParaExprR() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			System.out.println("ParaExprR = AtribR");

			AtribR();
		} else {
			System.out.println("ParaExprR = epsilon");
		}
	}

	private void LDeclGlob() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_DECL_GLOBAL) {
			System.out.println("LDeclGlob = DeclGlob \";\" LDeclGlob");
			DeclGlob();

			if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
				updateToken();

				LDeclGlob();
			} else {
				errorMsg("\";\" esperado");
			}
		} else {
			System.out.println("LDeclGlob = epsilon");
		}
	}

	private void DeclGlob() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_DECL_GLOBAL) {
			System.out.println("DeclGlob = \"global\" Decl");

			updateToken();
			Decl();
		}
	}

	private void Decl() {
		System.out.println("Decl = ModDecl \"id\" \":\" DeclTipoAtrib");
		ModDecl();
		if (currentToken.getCategory() == TokenCategory.ID) {
			updateToken();
			if (currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				updateToken();
				DeclTipoAtrib();
			}
		} else {
			errorMsg("\"id\" inválido");
		}
	}

	private void ModDecl() {
		TokenCategory category = currentToken.getCategory();
		if (category == TokenCategory.PR_CMD_DECL_CONST) {
			System.out.println("ModDecl = \"const\"");
			updateToken();
		} else if (category == TokenCategory.PR_CMD_DECL_VAR) {
			System.out.println("ModDecl = \"var\"");
			updateToken();
		} else {
			errorMsg("token não esperado");
		}
	}

	private void DeclTipoAtrib() {
		TokenCategory category = currentToken.getCategory();

		if (typesCategory.contains(category)) {
			System.out.println("DeclTipoAtrib = \"tipo\"(" + currentToken.getLexicalValue() + ") DeclAtribTipo");
			updateToken();

			DeclAtribTipo();
		} else if (currentToken.getCategory() == TokenCategory.ABRE_COL) {
			System.out.println("DeclTipoAtrib = Matriz DeclAtribMatriz");

			Matriz();
			DeclAtribMatriz();
		} else {
			errorMsg("token não esperado");
		}

	}

	private void DeclAtribTipo() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			System.out.println("DeclAtribTipo = \"opatrib\"(" + currentToken.getLexicalValue() + ") VAtrib");
			updateToken();

			VAtrib();
		} else {
			System.out.println("DeclAtribTipo = epsilon");
		}
	}

	private void DeclAtribMatriz() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			System.out
					.println("DeclAtribMatriz = \"opatrib\"(" + currentToken.getLexicalValue() + ") \"{\" LArg \"}\"");
			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_CH) {
				updateToken();

				LArg();

				if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
					updateToken();
				} else {
					errorMsg("\"}\" esperado");
				}
			} else {
				errorMsg("\"{\" esperado");
			}
		} else {
			System.out.println("DeclAtribMatriz = epsilon");
		}
	}

	private void Matriz() {
		if (currentToken.getCategory() == TokenCategory.ABRE_COL) {
			System.out.println("Matriz = \"[\" \"tipo\" \";\" MatrizTamanho \"]\"");
			updateToken();

			TokenCategory category = currentToken.getCategory();
			if (typesCategory.contains(category)) {
				updateToken();

				if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
					updateToken();

					MatrizTamanho();

					if (currentToken.getCategory() == TokenCategory.FECHA_COL) {
						updateToken();
					} else {
						errorMsg("\"]\" esperado");
					}
				} else {
					errorMsg("\";\" esperado");
				}
			} else {
				errorMsg("tipo inválido");
			}
		} else {
			errorMsg("\"[\" esperado");
		}
	}

	private void MatrizTamanho() {
		if (this.currentToken.getCategory() == TokenCategory.ID) {
			System.out.println("MatrizTamanho = \"id\"(" + this.currentToken.getLexicalValue() + ") AcMatriz");
			this.updateToken();
			AcMatriz();
		}

		else if (this.currentToken.getCategory() == TokenCategory.CONST_INT) {
			System.out.println("MatrizTamanho = \"constInt\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
		}

		else
			this.errorMsg("token não esperado");
	}

	private void ChVarConst() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			System.out.println("ChVarConst = \"id\"(" + currentToken.getLexicalValue() + ") AcMatriz");
			updateToken();
			AcMatriz();
		}
	}

	private void AcMatriz() {
		if (currentToken.getCategory() == TokenCategory.ABRE_COL) {
			System.out.println("AcMatriz = \"[\" MatrizTamanho \"]\"");
			updateToken();

			MatrizTamanho();
			if (currentToken.getCategory() == TokenCategory.FECHA_COL) {
				updateToken();
			} else {
				errorMsg("\"]\" esperado");
			}
		} else {
			System.out.println("AcMatriz = epsilon");
		}
	}

	private void Retorno() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_RETORNE) {
			System.out.println("Retorno = \"retorne\"(" + this.currentToken.getLexicalValue() + ") VAtrib");
			updateToken();
			VAtrib();
		} else {
			errorMsg("\"retorne\" esperado");
		}
	}

	private void Atrib() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			System.out.println("Atrib = \"id\"(" + currentToken.getLexicalValue() + ") AcMatriz AtribR");
			updateToken();

			AcMatriz();
			if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
				updateToken();

				AtribR();
			} else {
				errorMsg("\"=\" esperado");
			}
		} else {
			errorMsg("\"id\" inválido");
		}
	}

	private void AtribR() {
		if (currentToken.getCategory() == TokenCategory.OP_ATRIBUICAO) {
			System.out.println("AtribR = \"opatrib\"(" + currentToken.getLexicalValue() + ") VAtrib");
			updateToken();

			VAtrib();
		} else {
			errorMsg("\"=\" esperado");
		}
	}

	private void VAtrib() {
		System.out.println("VAtrib = Expr");
		Expr();
	}

	private void LParam() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			System.out.println("LParam = Param LParamNr");

			Param();
			LParamNr();
		} else {
			System.out.println("LParam = epsilon");
		}

	}

	private void LParamNr() {
		if (currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
			System.out.println("LParamNr = \",\" Param LParamNr");
			updateToken();

			Param();
			LParamNr();
		} else {
			System.out.println("LParamNr = epsilon");
		}
	}

	private void Param() {
		if (currentToken.getCategory() == TokenCategory.ID) {
			System.out.println("Param = \"id\"(" + currentToken.getLexicalValue() + ") \":\" \"tipo\"");
			updateToken();
			if (currentToken.getCategory() == TokenCategory.SE_DOISPONTOS) {
				updateToken();

				TokenCategory category = currentToken.getCategory();

				if (typesCategory.contains(category)) {
					updateToken();
				} else {
					errorMsg("tipo inválido");
				}

			} else {
				errorMsg("\":\" esperado");
			}
		} else {
			errorMsg("\"id\" inválido");
		}
	}

	private void LFunc() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_FUNC) {
			System.out.println("LFunc = Func LFunc");
			Func();
			LFunc();
		} else {
			System.out.println("LFunc = epsilon");
		}
	}

	private void LFuncProc() {
		switch (currentToken.getCategory()) {
		case PR_CMD_FUNC:
			System.out.println("LFuncProc = LFunc LFuncProc");
			LFunc();
			LFuncProc();
			break;

		case PR_CMD_PROC:
			LProc();
			LFuncProc();
			break;

		default:
			System.out.println("LFuncProc = epsilon");
			break;
		}

	}

	private void CorpoFunc() {
		System.out.println("CorpoFunc = LCmd Retorno");
		LCmd();
		Retorno();
	}

	private void Func() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_FUNC) {
			System.out
					.println("Func = \"funcao\" \"id\" \"(\" LParam \")\" \":\" \"tipo\" \"{\" CorpoFunc \";\" \"}\"");

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

									CorpoFunc();

									if (currentToken.getCategory() == TokenCategory.SE_PONTOVIRGULA) {
										updateToken();

										if (currentToken.getCategory() == TokenCategory.FECHA_CH) {
											updateToken();
										} else {
											errorMsg("\"}\" esperado");
										}
									} else {
										errorMsg("\";\" esperado");
									}
								} else {
									errorMsg("\"{\" esperado");
								}
							} else {
								errorMsg("tipo inválido");
							}
						} else {
							errorMsg("\":\" esperado");
						}
					} else {
						errorMsg("\")\" esperado");
					}
				} else {
					errorMsg("\"(\" esperado");
				}
			} else {
				errorMsg("\"id\" invalido");
			}
		} else {
			errorMsg("\"funcao\" esperado");
		}
	}

	private void LProc() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_PROC) {
			System.out.println("LProc = Proc LProc");
			Proc();
			LProc();
		} else {
			System.out.println("LProc = epsilon");
		}
	}

	private void Proc() {
		if (currentToken.getCategory() == TokenCategory.PR_CMD_PROC) {
			System.out.println("Proc = \"procedimento\" \"id\" \"(\" LParam \")\" Escopo\"");
			updateToken();
			if (currentToken.getCategory() == TokenCategory.ID) {
				updateToken();
				if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
					updateToken();
					LParam();

					if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
						updateToken();
						Escopo();
					} else {
						errorMsg("\")\" esperado");
					}
				} else {
					errorMsg("\"(\" esperado");
				}
			} else {
				errorMsg("\"id\" inválido");
			}
		} else {
			errorMsg("\"procedimento\" esperado");
		}
	}

	private void ChFuncProcR() {
		if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("ChFuncProcR = \"(\" LArg \")\"");
			updateToken();
			LArg();

			if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				updateToken();
			} else {
				errorMsg("\")\" esperado");
			}
		} else {
			errorMsg("\"(\" esperado");
		}
	}

	private void Inicio() {
		if (currentToken.getCategory() == TokenCategory.PR_INICIO) {
			System.out.println("Inicio = \"inicio\" \"(\" \")\" Escopo");

			updateToken();

			if (currentToken.getCategory() == TokenCategory.ABRE_PAR) {
				updateToken();

				if (currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					updateToken();

					Escopo();
				} else {
					errorMsg("\")\" esperado");
				}
			} else {
				errorMsg("\"(\" esperado");
			}
		} else {
			errorMsg("\"inicio\" esperado");
		}
	}

	private void LArg() {
		switch (currentToken.getCategory()) {
		case ABRE_PAR:
		case ID:
		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
		case CONST_BOOL:
		case OP_ARIT_ADD:
		case OP_BOOL_NAO:
			System.out.println("LArg = VAtrib LArgNr");
			VAtrib();
			LArgNr();
			break;

		default:
			System.out.println("LArg = epsilon");
			break;
		}
	}

	private void LArgNr() {
		if (currentToken.getCategory() == TokenCategory.SE_VIRGULA) {
			System.out.println("LArgNr = \",\" VAtrib LArgNr");
			updateToken();

			VAtrib();
			LArgNr();
		} else {
			System.out.println("LArgNr = epsilon");
		}
	}

	private void Expr() {
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
		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR = \"(\" TR3");
			this.updateToken();
			TR3();
		}

		else
			this.errorMsg("\"(\" esperado");
	}

	private void OPU2() {
		if (this.currentToken.getCategory() == TokenCategory.OP_BOOL_NAO) {
			System.out.println("OPU2 = \"opbu\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
		} else
			this.errorMsg("token não esperado");
	}

	private void OPU() {
		if (this.currentToken.getCategory() == TokenCategory.OP_ARIT_ADD) {
			System.out.println("OPU = \"oparu\"(" + this.currentToken.getLexicalValue() + ") TR11");
			this.updateToken();
			TR11();
		} else
			this.errorMsg("token não esperado");

	}

	private void NUMEROTEXTO() {

		switch (this.currentToken.getCategory()) {
		case CONST_INT:
			System.out.println("NUMEROTEXTO = \"constInt\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		case CONST_REAL:
			System.out.println("NUMEROTEXTO = \"constReal\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		case CONST_CARACTERE:
			System.out.println("NUMEROTEXTO = \"constChar\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		case CONST_TEXTO:
			System.out.println("NUMEROTEXTO = \"constTexto\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}

	}

	private void TR6() {

		switch (this.currentToken.getCategory()) {
		case ABRE_PAR:
			System.out.println("TR6 = TR17");
			TR17();
			break;

		case ID:
			System.out.println("TR6 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB5");
			this.updateToken();
			TR2();
			OPB5();
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR6 = NUMEROTEXTO OPB6 \"opr\" TR5");
			NUMEROTEXTO();
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
					|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
				this.updateToken();
				TR5();
			} else {
				this.errorMsg("token não esperado");
			}
			break;

		case CONST_BOOL:
			System.out.println("TR6 = \"constBool\"(" + this.currentToken.getLexicalValue() + ") OPB3");
			this.updateToken();
			OPB3();
			break;

		case OP_BOOL_NAO:
			System.out.println("TR6 = OPU2");
			OPU2();
			break;

		case OP_ARIT_ADD:
			System.out.println("TR6 = OPU4");
			OPU4();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}

	}

	private void OPU4() {

		if (this.currentToken.getCategory() == TokenCategory.OP_ARIT_ADD) {
			System.out.println("OPU4 = OPU5 \"opr\" TR19");
			OPU5();
			if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
					|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
				this.updateToken();
				TR19();
			}
		} else
			this.errorMsg("token não esperado");
	}

	private void OPB6() {

		if (this.currentToken.getCategory() == TokenCategory.OP_ARIT_ADD
				|| this.currentToken.getCategory() == TokenCategory.OP_ARIT_MUL
				|| this.currentToken.getCategory() == TokenCategory.OP_ARIT_EXP) {
			System.out.println("OPB6 = \"oparb\"(" + this.currentToken.getLexicalValue() + ") TR19");
			this.updateToken();
			TR19();
		} else
			System.out.println("OPB6 = epsilon");
	}

	private void OPB5() {

		switch (this.currentToken.getCategory()) {

		case OP_BOOL_E:
		case OP_BOOL_OU:
			System.out.println("OPB5 = \"opbb\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
			break;

		default:
			System.out.println("OPB5 = OPB6 OPB5R");
			OPB6();
			OPB5R();

			break;
		}
	}

	private void OPB5R() {
		if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
				|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
			System.out.println("OPB5R = \"opr\"(" + this.currentToken.getLexicalValue() + ") TR5");
			this.updateToken();
			TR5();
		} else
			System.out.println("OPB5R = epsilon");
	}

	private void TR17() {

		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR17 = \"(\" TR18");
			this.updateToken();
			TR18();
		} else
			this.errorMsg("\"(\" esperado");
	}

	private void TR18() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR18 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB5 \")\" OPB5");
			this.updateToken();
			TR2();
			OPB5();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB5();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR18 = NUMEROTEXTO OPB6 \"opr\" TR5 \")\" OPB5");
			NUMEROTEXTO();
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
					|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
				this.updateToken();
				TR5();
				if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					this.updateToken();
					OPB5();
				} else
					this.errorMsg("\")\" esperado");
			} else
				this.errorMsg("\"opr\" esperado");
			break;

		case CONST_BOOL:
			System.out.println("TR18 = \"constBool\"(" + this.currentToken.getLexicalValue() + ") OPB3 \")\" OPB3");
			this.updateToken();
			OPB3();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_BOOL_NAO:
			System.out.println("TR18 = OPU2 \")\" OPB3");
			OPU2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR18 = OPU4 \")\" OPB5");
			OPU4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB5();
			} else
				this.errorMsg("\")\" esperado");
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void TR11() {

		switch (this.currentToken.getCategory()) {
		case ABRE_PAR:
			System.out.println("TR11 = TR12");
			TR12();
			break;

		case ID:
			System.out.println("TR11 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB2");
			this.updateToken();
			TR2();
			OPB2();
			break;

		case CONST_INT:
		case CONST_REAL:
			System.out.println("TR11 = NUMERO OPB2");
			NUMERO();
			OPB2();
			break;

		case OP_ARIT_ADD:
			System.out.println("TR11 = OPU");
			OPU();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}

	}

	private void NUMERO() {

		switch (this.currentToken.getCategory()) {
		case CONST_INT:
			System.out.println("NUMERO = \"constInt\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		case CONST_REAL:
			System.out.println("NUMERO = \"constReal\"(" + this.currentToken.getLexicalValue() + ")");
			this.updateToken();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void TR12() {
		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR12 = \"(\" TR13");
			this.updateToken();
			TR13();
		} else
			this.errorMsg("\"(\" esperado");
	}

	private void TR13() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR13 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB2 \")\" OPB2");
			this.updateToken();
			TR2();
			OPB2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
			System.out.println("TR13 = NUMERO OPB2 \")\" OPB2");
			NUMERO();
			OPB2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR13 = OPU \")\" OPB2");
			OPU();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}

	}

	private void OPB3() {

		if (this.currentToken.getCategory() == TokenCategory.OP_BOOL_E
				|| this.currentToken.getCategory() == TokenCategory.OP_BOOL_OU) {
			System.out.println("OPB3 = \"opbb\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
		} else
			System.out.println("OPB3 = epsilon");
	}

	private void OPB2() {

		switch (this.currentToken.getCategory()) {
		case OP_ARIT_ADD:
		case OP_ARIT_MUL:
		case OP_ARIT_EXP:
			System.out.println("OPB2 = \"oparb\"(" + this.currentToken.getLexicalValue() + ") TR4");
			this.updateToken();
			TR4();
			break;

		case OP_RELACIONAL_1:
		case OP_RELACIONAL_2:
			System.out.println("OPB2 = \"opr\"(" + this.currentToken.getLexicalValue() + ") TR5");
			this.updateToken();
			TR5();
			break;

		default:
			System.out.println("OPB2 = epsilon");
			break;
		}

	}

	private void OPB() {

		switch (this.currentToken.getCategory()) {
		case OP_ARIT_ADD:
		case OP_ARIT_MUL:
		case OP_ARIT_EXP:
			System.out.println("OPB = \"oparb\"(" + this.currentToken.getLexicalValue() + ") TR4");
			this.updateToken();
			TR4();
			break;

		case OP_RELACIONAL_1:
		case OP_RELACIONAL_2:
			System.out.println("OPB = \"opr\"(" + this.currentToken.getLexicalValue() + ") TR5");
			this.updateToken();
			TR5();
			break;

		case OP_BOOL_E:
		case OP_BOOL_OU:
			System.out.println("OPB = \"opbb\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
			break;

		default:
			System.out.println("OPB = epsilon");
			break;
		}
	}

	private void TR2() {

		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR2 = ChFuncProcR");
			ChFuncProcR();
		}

		else {
			System.out.println("TR2 = AcMatriz");
			AcMatriz();
		}
	}

	private void TR3() {

		TokenCategory categ = this.currentToken.getCategory();

		if (categ == TokenCategory.ID) {
			System.out.println("TR3 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB \")\" OPB");
			this.updateToken();
			TR2();
			OPB();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB();
			} else
				this.errorMsg("\")\" esperado");
		}

		else if (categ == TokenCategory.CONST_INT || categ == TokenCategory.CONST_REAL
				|| categ == TokenCategory.CONST_CARACTERE || categ == TokenCategory.CONST_TEXTO) {
			System.out.println("TR3 = NUMEROTEXTO OPB2 \")\" OPB2");
			NUMEROTEXTO();
			OPB2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
		}

		else if (categ == TokenCategory.CONST_BOOL) {
			System.out.println("TR3 = \"constBool\"(" + this.currentToken.getLexicalValue() + ") OPB3 \")\" OPB3");
			this.updateToken();
			OPB3();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
			} else
				this.errorMsg("\")\" esperado");
		}

		else if (categ == TokenCategory.OP_ARIT_ADD) {
			System.out.println("TR3 = OPU \")\" OPB2");
			OPU();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
		}

		else if (categ == TokenCategory.OP_BOOL_NAO) {
			System.out.println("TR3 = OPU2 \")\" OPB3");
			OPU2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
			} else
				this.errorMsg("\")\" esperado");
		}

		else
			this.errorMsg("token não esperado");

	}

	private void TR4() {

		TokenCategory categ = this.currentToken.getCategory();

		if (categ == TokenCategory.ABRE_PAR) {
			System.out.println("TR4 = TR7");
			TR7();
		}

		else if (categ == TokenCategory.ID) {
			System.out.println("TR4 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB2");
			this.updateToken();
			TR2();
			OPB2();
		}

		else if (categ == TokenCategory.CONST_INT || categ == TokenCategory.CONST_REAL
				|| categ == TokenCategory.CONST_CARACTERE || categ == TokenCategory.CONST_TEXTO) {
			System.out.println("TR4 = NUMEROTEXTO OPB2");
			NUMEROTEXTO();
			OPB2();
		}

		else if (categ == TokenCategory.OP_ARIT_ADD) {
			System.out.println("TR4 = OPU");
			OPU();
		}

		else
			this.errorMsg("token não esperado");
	}

	private void TR5() {

		TokenCategory categ = this.currentToken.getCategory();

		if (categ == TokenCategory.ABRE_PAR) {
			System.out.println("TR5 = TR9");
			TR9();
		}

		else if (categ == TokenCategory.ID) {
			System.out.println("TR5 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB4");
			this.updateToken();
			TR2();
			OPB4();
		}

		else if (categ == TokenCategory.CONST_INT || categ == TokenCategory.CONST_REAL
				|| categ == TokenCategory.CONST_CARACTERE || categ == TokenCategory.CONST_TEXTO) {
			System.out.println("TR5 = NUMEROTEXTO OPB4");
			NUMEROTEXTO();
			OPB4();
		}

		else if (categ == TokenCategory.OP_ARIT_ADD) {
			System.out.println("TR5 = OPU3");
			OPU3();
		}

		else
			this.errorMsg("token não esperado");
	}

	private void OPU3() {

		if (this.currentToken.getCategory() == TokenCategory.OP_ARIT_ADD) {
			System.out.println("OPU3 = \"oparu\"(" + this.currentToken.getLexicalValue() + ") TR14");
			this.updateToken();
			TR14();
		} else
			this.errorMsg("token não esperado");
	}

	private void OPB4() {

		switch (this.currentToken.getCategory()) {
		case OP_ARIT_ADD:
		case OP_ARIT_MUL:
		case OP_ARIT_EXP:
			System.out.println("OPB4 = \"oparb\"(" + this.currentToken.getLexicalValue() + ") TR5");
			this.updateToken();
			TR5();
			break;

		case OP_BOOL_E:
		case OP_BOOL_OU:
			System.out.println("OPB4 = \"opbb\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
			break;

		default:
			System.out.println("OPB4 = epsilon");
			break;
		}
	}

	private void TR9() {
		if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
			System.out.println("TR9 = \"(\" TR10");
			this.updateToken();
			TR10();
		} else
			this.errorMsg("\"(\" esperado");
	}

	private void TR10() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR10 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB4 \")\" OPB4");
			this.updateToken();
			TR2();
			OPB4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");

			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR10 = NUMEROTEXTO OPB4 \")\" OPB4");
			NUMEROTEXTO();
			OPB4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR10 = OPU3 \")\" OPB4");
			OPU3();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");

			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}

	}

	private void TR7() {

		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR7 = \"(\" TR8");
			this.updateToken();
			TR8();
		}

		else
			this.errorMsg("\"(\" esperado");

	}

	private void TR8() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR8 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB2 \")\" OPB2");
			this.updateToken();
			TR2();
			OPB2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR8 = NUMEROTEXTO OPB2 \")\" OPB2");
			NUMEROTEXTO();
			OPB2();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR8 = OPU \")\" OPB2");
			OPU();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB2();
			} else
				this.errorMsg("\")\" esperado");
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void TR14() {

		switch (this.currentToken.getCategory()) {
		case ABRE_PAR:
			System.out.println("TR14 = TR15");
			TR15();
			break;

		case ID:
			System.out.println("TR14 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB4");
			this.updateToken();
			TR2();
			OPB4();
			break;

		case CONST_INT:
		case CONST_REAL:
			System.out.println("TR14 = NUMERO OPB4");
			NUMERO();
			OPB4();
			break;

		case OP_ARIT_ADD:
			System.out.println("TR14 = OPU3");
			OPU3();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void TR15() {
		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR15 = \"(\" TR16");
			this.updateToken();
			TR16();
		} else
			this.errorMsg("\"(\" esperado");
	}

	private void TR16() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR16 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB4 \")\" OPB4");
			this.updateToken();
			TR2();
			OPB4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
			System.out.println("TR16 = NUMERO OPB4 \")\" OPB4");
			NUMERO();
			OPB4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR16 = OPU3 \")\" OPB4");
			OPU3();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB4();
			} else
				this.errorMsg("\")\" esperado");
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void TR19() {

		switch (this.currentToken.getCategory()) {
		case ABRE_PAR:
			System.out.println("TR19 = TR20");
			TR20();
			break;

		case ID:
			System.out.println("TR19 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB6");
			this.updateToken();
			TR2();
			OPB6();
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR19 = NUMEROTEXTO OPB6");
			NUMEROTEXTO();
			OPB6();
			break;

		case OP_ARIT_ADD:
			System.out.println("TR19 = OPU5");
			OPU5();
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	private void OPU5() {

		if (this.currentToken.getCategory() == TokenCategory.OP_ARIT_ADD) {
			System.out.println("OPU5 = \"oparu\"(" + this.currentToken.getLexicalValue() + ") TR19");
			this.updateToken();
			TR19();
		} else
			this.errorMsg("token não esperado");

	}

	private void TR20() {

		if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR20 = \"(\" TR21");
			this.updateToken();
			TR21();
		} else
			this.errorMsg("\"(\" esperado");
	}

	private void TR21() {

		switch (this.currentToken.getCategory()) {
		case ID:
			System.out.println("TR21 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB6 \")\" OPB6");
			this.updateToken();
			TR2();
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB6();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR21 = NUMEROTEXTO OPB6 \")\" OPB6");
			NUMEROTEXTO();
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB6();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case OP_ARIT_ADD:
			System.out.println("TR21 = OPU5 \")\" OPB6");
			OPU5();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB6();
			} else
				this.errorMsg("\")\" esperado");
			break;

		default:
			this.errorMsg("token não esperado");
			break;
		}
	}

	public void run() {
		updateToken();
		System.out.println(currentToken);
		Programa();
	}

}
