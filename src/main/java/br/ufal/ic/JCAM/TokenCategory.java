package br.ufal.ic.JCAM;

public enum TokenCategory {
	ID,
	CONST_INT,
	CONST_REAL,
	CONST_CARACTERE,
	CONST_TEXTO,
	CONST_BOOL,
	
	// Separadores
	SE_VIRGULA,
	SE_PONTOVIRGULA,
	SE_DOISPONTOS,
	
	// Delimitadores
	ABRE_PAR,
	FECHA_PAR,
	ABRE_COL,
	FECHA_COL,
	ABRE_CH,
	FECHA_CH,

	// Operadores
	OP_ARIT_ADD,
	OP_ARIT_MUL,
	OP_ARIT_EXP,
	OP_RELACIONAL_1,
	OP_RELACIONAL_2,
	OP_BOOL_E,
	OP_BOOL_OU,
	OP_BOOL_NAO,
	OP_ATRIBUICAO,
	
	// Palavras reservadas
	PR_CMD_SE,
	PR_CMD_SENAO,
	PR_CMD_PARA,
	PR_CMD_PARA_ATE,
	PR_CMD_PARA_PASSO,
	PR_CMD_ENQUANTO,
	PR_CMD_DECL_VAR,
	PR_CMD_DECL_CONST,
	PR_CMD_FUNC,
	PR_CMD_PROC,
	PR_CMD_RETORNE,
	PR_ESCREVA,
	PR_LEIA,
	PR_TIPO_INTEIRO,
	PR_TIPO_REAL,
	PR_TIPO_BOOLEANO,
	PR_TIPO_TEXTO,
	PR_TIPO_CARACTERE,
	PR_INICIO,
	
	EOF
}
