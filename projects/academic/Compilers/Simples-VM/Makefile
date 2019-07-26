simples: lexico.l sintatico.y tree.c utils.c;\
	flex -t lexico.l > lexico.c;\
	bison -v -d sintatico.y -o sintatico.c;\
	gcc sintatico.c -o simples;\

limpa: ;\
	rm -rf lexico.c sintatico.c sintatico.output sintatico.h simples;\
