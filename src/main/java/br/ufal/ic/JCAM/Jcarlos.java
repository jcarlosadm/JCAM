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
		// TODO Auto-generated method stub

	}

	private void OPU() {
		// TODO Auto-generated method stub

	}

	private void NUMEROTEXTO() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	private void OPB6() {
		// TODO Auto-generated method stub

	}

	private void OPB5() {
		// TODO Auto-generated method stub

	}

	private void TR17() {
		// TODO Auto-generated method stub

	}

	private void TR11() {
		
		switch (this.currentToken.getCategory()) {
		case ABRE_PAR:
			System.out.println("TR11 = TR12");
			TR12();
			break;

		case ID:
			System.out.println("TR11 = \"id\"(" +this.currentToken.getLexicalValue() + ") TR2 OPB2");
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
		// TODO Auto-generated method stub
		
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
			System.out.println("TR13 = \"id\"(" +this.currentToken.getLexicalValue()+") TR2 OPB2 \")\" OPB2");
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
		// TODO Auto-generated method stub

	}

	private void OPB2() {
		// TODO Auto-generated method stub

	}

	private void OPB() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	private void OPB4() {
		// TODO Auto-generated method stub

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

}
