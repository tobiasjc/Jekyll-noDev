///∗+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−
//| UNIFAL − Universidade Federal de Alfenas
//| BACHARELADO EM CIENCIA DA COMPUTACAO
//| Disciplina: Compiladores
//| Professor . . . : Luiz Eduardo da Silva
//| Aluno . . . . . : José Carlos Tobias da Silva
//| Data . . . . . .: 05/01/2019
//+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−∗/

ptno criarNo(int tipo, char* valor) {
  ptno n = (ptno)malloc(sizeof(struct no));
  n->tipo = tipo;  // tipo de nó da árvore
  n->valor = (char*)malloc(strlen(valor) * sizeof(char) + 1);
  strcpy(n->valor, valor);
  n->filho = NULL;
  n->irmao = NULL;
  return n;
}

void adcionarFilho(ptno pai, ptno filho) {
  if (filho) {
    filho->irmao = pai->filho;
    pai->filho = filho;
  }
}

/**
 * @ptno p raíz da árvore
 **/
void geradot2(ptno p) {
  /*
  Esta função tem como objetivo direcionar os ligamentos do grafo direcionado
  que forma a árvore sintática abstrata em sua forma gráfica.
  */
  if (!p) return;

  ptno k = p;

  if (k->filho) {
    printf("\tk%p -> k%p\n", p, k->filho);
    k = k->filho;
    while (k->irmao) {
      printf("\tk%p -> k%p\n", p, k->irmao);
      k = k->irmao;
    }
  }

  geradot2(p->filho);
  geradot2(p->irmao);
}

/**
 * @ptno p raíz da árvore
 **/
void geradot1(ptno p) {
  /*
  Esta função executa a primeira parte da escrita no arquivo de modo a dar duas
  informações cruciais: 1 - identificação única de nome de nó 2 - formatação de
  amostragem do nó
  */
  if (!p) return;

  geradot1(p->filho);

  switch (p->tipo) {
    case T_IDENTIF:
      printf("\tk%p [label = \"identificador | %s\"];\n", p, p->valor);
      break;
    case T_TIPO_LISTA_VAR:
      printf("\tk%p [label = \"tipo | %s\"];\n", p, p->valor);
      break;
    case T_NUMERO:
      printf("\tk%p [label = \"numero | %s\"];\n", p, p->valor);
      break;
    case T_VAR:
      printf("\tk%p [label = \"variavel | %s\"];\n", p, p->valor);
      break;
    case T_V:
      printf("\tk%p [label = \"logico | %s\"];\n", p, p->valor);
      break;
    case T_F:
      printf("\tk%p [label = \"logico | %s\"];\n", p, p->valor);
      break;
    case T_TEXTO:
      printf("\tk%p [label = \"texto | %s\"];\n", p, p->valor);
      break;
    default:
      printf("\tk%p [label = \"%s | \"];\n", p, p->valor);
      break;
  }

  geradot1(p->irmao);
}

/**
 * @ptno p raíz da árvore
 **/
void geradot(ptno p) {
  /*
  Função de suporte para a chamada das outroas duas funções de escrita. Essa
  função tem como papel principal a formatação requerida do arquivo em partes
  que não diponham de configuração de nó.
  */

  printf("digraph G {\n");
  printf("\tnode [shape=record, height=.1];\n");

  geradot1(p);
  geradot2(p);

  printf("}\n");
}

/**
 * @ptno p raíz da árvore
 **/
void verifica(ptno p) {
  /*
  Função auxiliar de verificação de tipos. Essa função tem como objetivo
  verificar se o retorno de uma operação é condizente com o esperado por ela
  dado seus operandos.
  */

  if (!p) return;

  ptno p1, p2;

  int tipo = desempilha();

  if (tipo == T_LOGICO) {
    if (p->tipo == T_VAR) {
      int tipo_var = TSIMB[busca_simbolo(p->valor)].tipo;
      if (tipo_var != tipo) {
        ERRO("\tTipos incompatíveis em operação lógica!\n");
      }
    } else if (p->tipo != T_MAIOR && p->tipo != T_MENOR && p->tipo != T_IGUAL &&
               p->tipo != T_NAO && p->tipo != T_V && p->tipo != T_F) {
      ERRO("\tTipos incompatíveis em operação lógica!\n");
    } else {
      empilha(T_LOGICO);
    }
  } else if (tipo == T_NUMERO) {
    if (p->tipo == T_VAR) {
      int tipo_var = TSIMB[busca_simbolo(p->valor)].tipo;
      if (tipo_var != tipo) {
        ERRO("\tTipos incompatíveis em operação numérica!\n");
      }
    } else if (p->tipo != T_MAIS && p->tipo != T_MENOS && p->tipo != T_VEZES &&
               p->tipo != T_DIV && p->tipo != T_NUMERO) {
      ERRO("\tTipos incompatíveis em operação numérica!\n");
    } else {
      empilha(T_NUMERO);
    }
  }
}

/**
 * @ptno p raíz da árvore
 **/
void checatipo(ptno p) {
  /*
  Esta função fará a checagem dos tipos de nós para verificar se os mesmos são
  compatíveis com suas operações. Serão checados apenas os nós de atribuição,
  seleção e repetição.
  */

  if (!p) return;

  ptno p1, p2, p3;

  switch (p->tipo) {
    case T_ATRIB:
      p1 = p->filho;
      p2 = p1->irmao;
      empilha(TSIMB[busca_simbolo(p1->valor)].tipo);
      verifica(p2);
      break;
    case T_SE:
      p1 = p->filho;
      p2 = p1->irmao;
      empilha(T_LOGICO);
      verifica(p1);
      break;
    case T_ENQTO:
      p1 = p->filho;
      p2 = p1->irmao;
      empilha(T_LOGICO);
      verifica(p1);
      break;
  }

  checatipo(p->filho);
  checatipo(p->irmao);
}

/**
 * @ptno p raíz da árvore
 **/
void geracod(ptno p) {
  /*
  Esecuta a tradução da árvore sintática abstrata para código MIPS executando
  também ações semânticas durantes o percursos na árvore. O código de saída
  por ser testado no simulador MIPS MARS
  (http://courses.missouristate.edu/KenVollmar/mars/) para verificação de
  consistência do código, uma vez que o mesmo é gerado de acordo com padrões
  utilizados pelo simulador.
  */
  if (!p) return;

  ptno p1, p2, p3;

  switch (p->tipo) {
    case T_PROGRAMA:
      p1 = p->filho;
      p2 = p1->irmao;
      if (p2) p3 = p2->irmao;
      printf(".data\n");
      if (text_var_cont) {
        printf("\t_esp: .asciiz \" \"\n");
        printf("\t_ent: .asciiz \"\\n\"\n");
        for (int i = 0; i < text_var_cont; i++) {
          printf("\t_const%d: .asciiz \"%s\"\n", i, const_str[i]);
        }
        text_var_cont = 0;
      }
      if (p2 && p2->tipo == T_DECLA_VAR) {
        geracod(p2);
      }
      if (p2 && p2->tipo == T_LISTA_COM) {
        printf(".text\n\t.globl main\n");
        printf("main:\tnop\n");
        geracod(p2);
        printf("fim:\tnop\n");
      }
      if (p3 && p3->tipo == T_LISTA_COM) {
        printf(".text\n\t.globl main\n");
        printf("main:\tnop\n");
        geracod(p3);
        printf("fim:\tnop\n");
      }
      if (!p2) {
        printf(".text\n\t.globl main\n");
        printf("main:\tnop\n");
        printf("fim:\tnop\n");
      }
      break;
    case T_V:
      printf("\tli $a0, 1\n");
      break;
    case T_F:
      printf("\tli $a0, 0\n");
      break;
    case T_ENQTO:
      p1 = p->filho;
      p2 = p1->irmao;
      printf("L%d:\tnop\n", ++ROTULO);
      empilha(ROTULO);
      geracod(p1);
      printf("\tbeqz $a0, L%d\n", ++ROTULO);
      empilha(ROTULO);
      geracod(p2);
      aux = desempilha();
      printf("\tj L%d\n", desempilha());
      printf("L%d:\tnop\n", aux);
      break;
    case T_ATRIB:
      p1 = p->filho;
      p2 = p1->irmao;
      if (p2->tipo == T_NUMERO) {
        printf("\tli $a0, %s\n", p2->valor);
      } else {
        geracod(p2);
      }
      printf("\tsw $a0, %s\n", p1->valor);
      break;
    case T_SE:
      p1 = p->filho;
      p2 = p1->irmao;
      p3 = p2->irmao;
      geracod(p1);
      printf("\tbeqz $a0, L%d\n", ++ROTULO);
      empilha(ROTULO);
      geracod(p2);
      printf("\tj L%d\n", ++ROTULO);
      aux = desempilha();
      empilha(ROTULO);
      printf("L%d:\tnop\n", aux);
      geracod(p3);
      printf("L%d:\tnop\n", desempilha());
      break;
    case T_LEIA:
      p1 = p->filho;
      printf("\tli $v0, 5\n");
      printf("\tsyscall\n");
      printf("\tsw $v0, %s\n", p1->valor);
      break;
    case T_ESCREVA:
      p1 = p->filho;
      geracod(p1);
      if (p1->tipo == T_TEXTO) {
        printf("\tli $v0, 4\n");
      } else {
        printf("\tli $v0, 1\n");
      }
      printf("\tsyscall\n");
      break;
    case T_TEXTO:
      printf("\tla $a0 _const%d\n", text_var_cont++);
      break;
    case T_VEZES:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tmult $t1 $a0\n");
      printf("\tmflo $a0\n");
      break;
    case T_NUMERO:
      printf("\tli $a0 %s\n", p->valor);
      break;
    case T_VAR:
      printf("\tlw $a0 %s\n", p->valor);
      break;
    case T_IDENTIF:
      printf("\tlw $a0 %s\n", p->valor);
      break;
    case T_DIV:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tdiv $t1 $a0\n");
      printf("\tmflo $a0\n");
      break;
    case T_MAIS:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tadd $a0 $t1 $a0\n");
      break;
    case T_MENOS:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tsub $a0, $t1, $a0\n");
      break;
    case T_IGUAL:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tbeq $a0, $t1, L%d\n", ++ROTULO);
      empilha(ROTULO);
      printf("\tli $a0, 0\n");
      printf("\tj L%d\n", ++ROTULO);
      aux = desempilha();
      empilha(ROTULO);
      printf("L%d:\tli $a0, 1\n", aux);
      printf("L%d:\tnop\n", desempilha());
      break;
    case T_OU:
      printf("\tbnez $a0, L%d\n", ++ROTULO);
      printf("\tbnez $t1, L%d\n", ROTULO);
      empilha(ROTULO);
      printf("\tli $a0, 0\n");
      printf("\tj L%d\n", ++ROTULO);
      printf("L%d:\tli $a0, 1\n", desempilha());
      printf("L%d:\tnop\n", ROTULO);
      break;
    case T_E:
      printf("\tbnez $a0, L%d\n", ++ROTULO);
      printf("\tbnez $t1, L%d\n", ROTULO);
      empilha(ROTULO);
      printf("\tli $a0, 1\n");
      printf("\tj L%d\n", ++ROTULO);
      printf("L%d:\tli $a0, 0\n", desempilha());
      printf("L%d:\tnop\n", ROTULO);
      break;
    case T_MENOR:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tslt $a0, $t1, $a0\n");
      break;
    case T_MAIOR:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      printf("\tsw $a0 0($sp)\n");
      printf("\taddiu $sp $sp -4\n");
      geracod(p2);
      printf("\tlw $t1 4($sp)\n");
      printf("\taddiu $sp $sp 4\n");
      printf("\tslt $a0, $a0, $t1\n");
      break;
    case T_NAO:
      p1 = p->filho;
      geracod(p1);
      printf("\tbeqz $a0, L%d\n", ++ROTULO);
      empilha(ROTULO);
      printf("\tli $a0, 0\n");
      printf("\tj L%d\n", ++ROTULO);
      aux = desempilha();
      empilha(ROTULO);
      printf("L%d:\tli $a0, 1\n", aux);
      printf("L%d:\tnop\n", desempilha());
      break;
    case T_LISTA_COM:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p1);
      geracod(p2);
      break;
    case T_DECLA_VAR:
      p1 = p->filho;
      p2 = p1->irmao;
      p3 = p2->irmao;
      geracod(p1);
      geracod(p2);
      geracod(p3);
      break;
    case T_LISTA_VAR:
      p1 = p->filho;
      p2 = p1->irmao;
      geracod(p2);
      if (p2) {
        if (p2->tipo == T_TEXTO) {
          printf("\t.asciiz");
        }
        printf("\t%s: .word 1\n", p1->valor);
      } else if (p1) {
        printf("\t%s: .word 1\n", p1->valor);
      }
      break;
  }
}