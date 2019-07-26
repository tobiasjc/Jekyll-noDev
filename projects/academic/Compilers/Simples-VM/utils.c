///∗+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−
//| UNIFAL − Universidade Federal de Alfenas
//| BACHARELADO EM CIENCIA DA COMPUTACAO
//| Disciplina: Compiladores
//| Professor . . . : Luiz Eduardo da Silva
//| Aluno . . . . . : José Carlos Tobias da Silva
//| Data . . . . . .: 05/01/2019
//+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−∗/

#define TAM_TSIMB 100
#define TAM_PSEMA 100
#include <stdint.h>

int TOPO_TSIMB = 0;
int TOPO_PSEMA = 0;
int ROTULO = 0;
int CONTA_VARS = 0;
int POS_SIMB;
int TIPO_LISTA;  // guarda tipo das variáveis a serem inseridas em uma
                 // declaração de listas de variáveis

int aux;
int numLinha = 1;
char atomo[30];

void ERRO(char *msg, ...) {
  char formato[255];
  va_list arg;
  va_start(arg, msg);
  sprintf(formato, "\n%d", numLinha);
  strcat(formato, msg);
  strcat(formato, "\n\n");
  printf("\nERRO no programa.");
  vprintf(formato, arg);
  va_end(arg);
  exit(1);
}

struct elem_tab_simbolos {
  char id[30];
  int tipo;  // campo para tipo de símbolo adcionado a tabela
  int desloca;
} TSIMB[TAM_TSIMB], elem_tab;

int PSEMA[TAM_PSEMA];

int busca_simbolo(char *ident) {
  int i = TOPO_TSIMB - 1;
  for (; strcmp(TSIMB[i].id, ident) && i >= 0; i--)
    ;
  return i;
}

void insere_simbolo(struct elem_tab_simbolos *elem) {
  if (TOPO_TSIMB == TAM_TSIMB) {
    ERRO("OVERFOW tabela de simbolos");
  } else {
    POS_SIMB = busca_simbolo(elem->id);
    if (POS_SIMB != -1) {
      ERRO("Identificador [%s] duplicado", elem->id);
    }
    TSIMB[TOPO_TSIMB++] = *(elem);
  }
}

void insere_variavel(char *ident, int tipo) {
  strcpy(elem_tab.id, ident);
  elem_tab.tipo = tipo;
  elem_tab.desloca = CONTA_VARS;
  insere_simbolo(&elem_tab);
}

void empilha(int n) {
  if (TOPO_PSEMA == TAM_PSEMA) {
    ERRO("OVERFLOW pilha semantica");
  }
  PSEMA[TOPO_PSEMA++] = n;
}

int desempilha() {
  if (TOPO_PSEMA == 0) {
    ERRO("UNDERFLOW pilha semantica");
  }
  return PSEMA[--TOPO_PSEMA];
}