///∗+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−
//| UNIFAL − Universidade Federal de Alfenas
//| BACHARELADO EM CIENCIA DA COMPUTACAO
//| Disciplina: Compiladores
//| Professor . . . : Luiz Eduardo da Silva
//| Aluno . . . . . : José Carlos Tobias da Silva
//| Data . . . . . .: 05/01/2019
//+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−∗/

%start programa

%token T_PROGRAMA
%token T_INICIO
%token T_FIM
%token T_IDENTIF

%token T_LEIA
%token T_ESCREVA
%token T_ENQTO

%token T_FACA
%token T_FIMENQTO
%token T_SE
%token T_ENTAO
%token T_SENAO
%token T_FIMSE
%token T_ATRIB

%token T_VEZES
%token T_DIV
%token T_MAIS
%token T_MENOS
%token T_MAIOR
%token T_MENOR
%token T_IGUAL

%token T_E
%token T_OU
%token T_V
%token T_F

%token T_NUMERO
%token T_NAO
%token T_ABRE
%token T_FECHA
%token T_LOGICO
%token T_INTEIRO

// tokens criados para formatação da árvore
%token T_TEXTO
%token T_VAR
%token T_TIPO_LISTA_VAR
%token T_LISTA_VAR
%token T_DECLA_VAR
%token T_LISTA_COM

%left T_E T_OU
%left T_IGUAL
%left T_MAIOR T_MENOR
%left T_MAIS T_MENOS
%left T_VEZES T_DIV

%{
	#include <stdio.h>
	#include <string.h>
	#include <stdlib.h>
	#include <stdarg.h>
	#include <string.h>
	#include "utils.c"
	#include "lexico.c"
	#include "tree.c"
	#include <stdint.h>

	ptno raiz = NULL, pt;
	int sym[26];

	void yyerror(char*);
%}

%%

programa:
	cabecalho {
		raiz = $1;
	} variaveis T_INICIO lista_comandos {
		adcionarFilho(raiz, $5);
		adcionarFilho(raiz, $3);
		adcionarFilho(raiz, $2);
	} T_FIM {
		$$ = NULL;
	}
    ;

cabecalho:
	T_PROGRAMA T_IDENTIF {
		pt = criarNo(T_PROGRAMA, "programa");
		$$ = pt; 
	}
    ;
variaveis:
	{
		$$ = NULL;
	}
    | declaracao_variaveis
    ;

declaracao_variaveis:
	declaracao_variaveis tipo lista_variaveis {
		pt = criarNo(T_DECLA_VAR, "declaracao de variaveis");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $2);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | tipo lista_variaveis {
		pt = criarNo(T_DECLA_VAR, "declaracao de variaveis");
		adcionarFilho(pt, $2);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    ;

tipo:
	T_LOGICO {
		pt = criarNo(T_TIPO_LISTA_VAR, "logico");
		TIPO_LISTA = T_LOGICO;
		$$ = pt;
	}
    | T_INTEIRO {
		pt = criarNo(T_TIPO_LISTA_VAR, "inteiro");
		TIPO_LISTA = T_NUMERO;
		$$ = pt;
	}
    ;

lista_variaveis:
	lista_variaveis T_IDENTIF {
		pt = criarNo(T_LISTA_VAR, "lista variaveis");
		adcionarFilho(pt, $1);
		adcionarFilho(pt, $2);
		insere_variavel($2->valor, TIPO_LISTA);
		CONTA_VARS++;
		$$ = pt;
	}
    | T_IDENTIF {
		pt = criarNo(T_LISTA_VAR, "lista variaveis");
		adcionarFilho(pt, $1);
		insere_variavel($1->valor, TIPO_LISTA);
		CONTA_VARS++;
		$$ = pt;
	}
    ;

lista_comandos:
	{
		$$ = NULL;
	}
	| comando lista_comandos {
		pt = criarNo(T_LISTA_COM, "lista comandos");
		adcionarFilho(pt, $2);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    ;

comando:
	entrada_saida
    | repeticao
    | selecao
    | atribuicao
    ;

entrada_saida:
	leitura 
    | escrita
    ;

leitura:
	T_LEIA T_IDENTIF {
		pt = criarNo(T_LEIA, "leitura");
		adcionarFilho(pt, $2);
		$$ = pt;
	}
    ;

escrita:
	T_ESCREVA expressao {
		pt = criarNo(T_ESCREVA, "escrita");
		adcionarFilho(pt, $2);
		$$ = pt;
	}
    ;

repeticao:
	T_ENQTO expressao T_FACA lista_comandos T_FIMENQTO {
		pt = criarNo(T_ENQTO, "repeticao");
		adcionarFilho(pt, $4);
		adcionarFilho(pt, $2);
		$$ = pt;
	}
    ;

selecao:
	T_SE expressao T_ENTAO lista_comandos T_SENAO lista_comandos T_FIMSE {
		pt = criarNo(T_SE, "selecao");
		adcionarFilho(pt, $6);
		adcionarFilho(pt, $4);
		adcionarFilho(pt, $2);
		$$ = pt;
	}
    ;

atribuicao:
	 T_IDENTIF T_ATRIB expressao {
		pt = criarNo(T_ATRIB, "atribuicao");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    ;

expressao: 
	expressao T_VEZES expressao {
		pt = criarNo(T_VEZES, "multiplica");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_DIV expressao {
		pt = criarNo(T_DIV, "divide");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_MAIS expressao {
		pt = criarNo(T_MAIS, "soma");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_MENOS expressao {
		pt = criarNo(T_MENOS, "subtrai");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_MAIOR expressao {
		pt = criarNo(T_MAIOR, "compara maior");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_MENOR expressao {
		pt = criarNo(T_MENOR, "compara menor");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_IGUAL expressao {
		pt = criarNo(T_IGUAL, "compara igual");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_E expressao {
		pt = criarNo(T_E, "conjuncao (E)");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | expressao T_OU expressao {
		pt = criarNo(T_OU, "disjuncao (OU)");
		adcionarFilho(pt, $3);
		adcionarFilho(pt, $1);
		$$ = pt;
	}
    | termo {
		$$ = $1;
	}
	| T_TEXTO {
		char nome[10];
		sprintf(nome, "_const%d", text_var_cont);
		const_str[text_var_cont++] = (char *) malloc((strlen(atomo) + 1) * sizeof(char));
		strcpy(const_str[text_var_cont - 1], atomo);
		pt =  criarNo(T_TEXTO, nome);
		$$ = pt;
	}
    ;

termo:
	T_IDENTIF
	{
		pt = criarNo(T_VAR, atomo);
		$$ = pt;
	}
    | T_NUMERO {
		pt = criarNo(T_NUMERO, atomo);
		$$ = pt;
	}
    | T_V {
		pt = criarNo(T_V, "verdadeiro");
		$$ = pt;
	}
    | T_F {
		pt = criarNo(T_F, "falso");
		$$ = pt;
	}
    | T_NAO termo {
		pt = criarNo(T_NAO, "negacao (NAO)");
		adcionarFilho(pt, $2);
		$$ = pt;
	}
    | T_ABRE expressao T_FECHA  {
		$$ = $2;
	}
    ;

%%

int yywrap(){
	return 1;
}

void yyerror(char *s){
	ERRO("\tErro sintático!");
}

int main(int argc, char* argv[]){
	/*
	Função principal. Executa chamada do parser ao invocar programa. Este parser tem
	o objetivo de criar a árvore sintática abstrata para o programa dado como entrada.
	Parâmetros podem ser passados para se invocar ações sobre a árvore, sendo eles:
		"+d" - invoca função para criar o modelo da árvore em arquivo de tipo ".dot". Es-
		te arquivo contém o modelo visual da árvore em forma de grafo direcionado.
		"+g" - invoca função para escrita de código MIPS em saída de sistema. Este código
		pode ser testado com simuladores MIPS, como MARS. É um código real.
	Após ação também executa checagem de tipo para comandos de atribuição, seleção e repetição.
	*/	
	yyparse();
	if(argc > 1){
		int i = 0;
		while(i < argc - 1){
			++i;
			if(!strcmp(argv[i], "+d")){
				geradot(raiz);
			}
			if(!strcmp(argv[i], "+g")){
				geracod(raiz);
			}
		}
	}
	checatipo(raiz); // irá enviar mensagem de erro na função ERRO caso tipos não estejam de acordo
	return 0;
}
