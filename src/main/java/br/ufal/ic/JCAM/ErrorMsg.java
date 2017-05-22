package br.ufal.ic.JCAM;

public enum ErrorMsg {
	INVALID_ID("digite um identificador válido!"),
	INVALID_TYPE("digite um tipo válido! (inteiro | booleano | texto | caractere | real)"),
	CONST_INT("constante inteira esperada!"),
	INVALID_DECL_GLOBAL("palavra global esperada!"),
	NOTFOUND_ABRE_COL("[ esperado"),
	NOTFOUND_FECHA_COL("] esperado"),
	NOTFOUND_ABRE_PAR("( esperado"),
	NOTFOUND_FECHA_PAR(") esperado"),
	NOTFOUND_ABRE_CH("{ esperado"),
	NOTFOUND_FECHA_CH("} esperado"),
	NOTFOUND_SE_DOISPONTOS("separador : esperado!"),
	NOTFOUND_SE_PONTOVIRGULA("separador ; esperado!"),
	NOTFOUND_OP_ATRIB("operador de atribuição = esperado!"),
	NOTFOUND_SE_VIRGULA("separador , esperado!");
	
	
	
	private String msg;
	
	ErrorMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
