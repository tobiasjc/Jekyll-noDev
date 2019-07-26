///∗+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−
//| UNIFAL − Universidade Federal de Alfenas
//| BACHARELADO EM CIENCIA DA COMPUTACAO
//| Disciplina: Compiladores
//| Professor . . . : Luiz Eduardo da Silva
//| Aluno . . . . . : José Carlos Tobias da Silva
//| Data . . . . . .: 05/01/2019
//+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−∗/

typedef struct no* ptno;

struct no {
  int tipo;
  char* valor;
  ptno filho, irmao;
};

int text_var_cont = 0;  // contagem de variáveis em "const_str"

/*
Estrutura utilizada para variáveis que não são guardadas em árvore (texto,
basicamente). Esta estrutura é indexada por text_var_cont.
*/
char* const_str[100];

// para nó
ptno criarNo(int, char*);
void adcionarFilho(ptno, ptno);

// métodos de análise na árvore
void geracod(ptno);
void geradot(ptno p);
void geradot1(ptno);
void geradot2(ptno);

// checagem de operações
void checatipo(ptno);
void verifica(ptno);