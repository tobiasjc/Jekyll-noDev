%{
///∗+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−
//| UNIFAL − Universidade Federal de Alfenas
//| BACHARELADO EM CIENCIA DA COMPUTACAO
//| Disciplina: Compiladores
//| Professor . . . : Luiz Eduardo da Silva
//| Aluno . . . . . : José Carlos Tobias da Silva
//| Data . . . . . .: 05/01/2019
//+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−∗/
	#include "tree.h"
    #define YYSTYPE ptno

    #include <stdlib.h>
	#include "sintatico.h"

    #define MAX_STR_CONST 512
    char string_buf[MAX_STR_CONST];
    char *string_buf_ptr;   

    void yyerror(char *);

    /* esquema de string ("quoted string") retirado da documentação original do flex sem verificação de tamanho máximo disponível em: */
    // http://www.delorie.com/gnu/docs/flex/flex_11.html
%}


identificador [A-Za-z]([A-Za-z0-9])*
numero [0-9]+
espaco [ \t]+
novalinha [\n]

%x              comentario
%x              str

%%

\"                  string_buf_ptr = string_buf;BEGIN(str);

<str>\"        { /* saw closing quote - all done */
                    BEGIN(INITIAL);
                    *string_buf_ptr = '\0';
                    /* return string constant token type and
                    * value to parser
                    */
                }

<str>\n        {
                    /* error - unterminated string constant */
                    /* generate error message */
                    ERRO("\tString mal analisada!", yytext);
                }

<str>\\[0-7]{1,3}   {
                        /* octal escape sequence */
                        int result;

                        (void) sscanf( yytext, "%o", &result );

                        if ( result > 0xff )
                            ERRO("\tConstante fora dos limites!", yytext);
                        /* error, constant is out-of-bounds */
                        *string_buf_ptr++ = result;
                    }

<str>\\[0-9]+       {
                        /* generate error - bad escape sequence; something
                        * like '\48' or '\0777777'
                        */
                        ERRO("\tSequência de escape mal constuída!", yytext);
                    }

<str>\\n  *string_buf_ptr++ = '\n';
<str>\\t  *string_buf_ptr++ = '\t';
<str>\\r  *string_buf_ptr++ = '\r';
<str>\\b  *string_buf_ptr++ = '\b';
<str>\\f  *string_buf_ptr++ = '\f';

<str>\\(.|\n)   {
                    *string_buf_ptr++ = yytext[1];
                }

<str>[^\\\n\"]+ {
                    char *yptr = yytext;
                    while ( *yptr )
                        *string_buf_ptr++ = *yptr++;
                    strcpy(atomo, yytext);
                    return T_TEXTO;
                }


"programa"		return T_PROGRAMA;
"inicio"		return T_INICIO;
"fimprograma"	return T_FIM;

"leia"			return T_LEIA;
"escreva"		return T_ESCREVA;

"enquanto"		return T_ENQTO;
"faca"			return T_FACA;
"fimenquanto"	return T_FIMENQTO;
"se"			return T_SE;
"entao"			return T_ENTAO;
"senao"			return T_SENAO;
"fimse"			return T_FIMSE;

"+"				return T_MAIS;
"-"				return T_MENOS;
"<-"			return T_ATRIB;
"*"				return T_VEZES;
"/"				return T_DIV;

"<"				return T_MENOR;
">"				return T_MAIOR;
"="			    return T_IGUAL;

"&&"			return T_E;
"||"			return T_OU;
"verdadeiro"	return T_V;
"falso"			return T_F;
"nao"			return T_NAO;

"("				return T_ABRE;
")"				return T_FECHA;

"booleano"		return T_LOGICO;
"inteiro"		return T_INTEIRO;

{numero} {
    yylval = (ptno) criarNo(T_IDENTIF, yytext);
    strcpy(atomo, yytext);
    return T_NUMERO;
}

{identificador} {
    yylval = (ptno) criarNo(T_IDENTIF, yytext);
    strcpy(atomo, yytext);
    return T_IDENTIF;
}

{espaco}           ; // ignore spaces and tabs
{novalinha}	numLinha++;

"//"(.*)                ; // ignore comments

"/*"                    BEGIN(comentario);
<comentario>.			;	/* faz nada */
<comentario>\n			numLinha++; // add line count
<comentario>"*/"		BEGIN(INITIAL);

.	ERRO("\ttERRO LEXICO");

%%

