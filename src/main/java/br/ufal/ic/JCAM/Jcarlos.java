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
		// TODO Auto-generated method stub
		TokenCategory categ = this.currentToken.getCategory();
		
		if (categ == TokenCategory.ABRE_PAR) {
			
		}

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

		if (this.currentToken.getCategory() == TokenCategory.ABRE_COL) {
			System.out.println("TR2 = AcMatriz");
			AcMatriz();
		}

		else if (this.currentToken.getCategory() == TokenCategory.ABRE_PAR) {
			System.out.println("TR2 = ChFuncProcR");
			ChFuncProcR();
		}

		else
			this.errorMsg("token inesperado");
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
		
		else this.errorMsg("token não esperado");
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
		
		else this.errorMsg("token não esperado");
	}

	private void OPU3() {
		// TODO Auto-generated method stub
		
	}

	private void OPB4() {
		// TODO Auto-generated method stub
		
	}

	private void TR9() {
		// TODO Auto-generated method stub
		
	}

	private void TR7() {
		// TODO Auto-generated method stub

	}

}
