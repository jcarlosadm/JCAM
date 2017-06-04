package br.ufal.ic.JCAM;

public class Jcarlos {

	private LexicalAnalyzer lexicalAnalyzer;
	private Token currentToken;

	// se houver um ou mais erros, mudar para false
	private boolean success = true;

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
			System.out.println("NUMEROTEXTO = \"constInt\"("+this.currentToken.getLexicalValue()+")");
			this.updateToken();
			break;
			
		case CONST_REAL:
			System.out.println("NUMEROTEXTO = \"constReal\"("+this.currentToken.getLexicalValue()+")");
			this.updateToken();
			break;
			
		case CONST_CARACTERE:
			System.out.println("NUMEROTEXTO = \"constChar\"("+this.currentToken.getLexicalValue()+")");
			this.updateToken();
			break;
			
		case CONST_TEXTO:
			System.out.println("NUMEROTEXTO = \"constTexto\"("+this.currentToken.getLexicalValue()+")");
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
		case OP_ARIT_ADD:
		case OP_ARIT_MUL:
		case OP_ARIT_EXP:
			System.out.println("OPB5 = OPB6 \"opr\" TR5");
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
					|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
				this.updateToken();
				TR5();
			} else
				this.errorMsg("\"opr\" esperado");
			break;

		case OP_BOOL_E:
		case OP_BOOL_OU:
			System.out.println("OPB5 = \"opbb\"(" + this.currentToken.getLexicalValue() + ") TR6");
			this.updateToken();
			TR6();
			break;

		default:
			System.out.println("OPB5 = epsilon");
			break;
		}
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
			System.out.println("TR18 = \"id\"(" + this.currentToken.getLexicalValue() + ") TR2 OPB5 \")\" OPB3");
			this.updateToken();
			TR2();
			OPB5();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
			} else
				this.errorMsg("\")\" esperado");
			break;

		case CONST_INT:
		case CONST_REAL:
		case CONST_CARACTERE:
		case CONST_TEXTO:
			System.out.println("TR18 = NUMEROTEXTO OPB6 \"opr\" TR5 \")\" OPB3");
			NUMEROTEXTO();
			OPB6();
			if (this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_1
					|| this.currentToken.getCategory() == TokenCategory.OP_RELACIONAL_2) {
				this.updateToken();
				TR5();
				if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
					this.updateToken();
					OPB3();
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
			System.out.println("TR18 = OPU4 \")\" OPB3");
			OPU4();
			if (this.currentToken.getCategory() == TokenCategory.FECHA_PAR) {
				this.updateToken();
				OPB3();
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

	private void ChFuncProcR() {
		// TODO remove later

	}

	private void AcMatriz() {
		// TODO remove later

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

}
