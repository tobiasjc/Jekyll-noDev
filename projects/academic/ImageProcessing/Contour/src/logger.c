/*
 * logger.c
 *
 *  Created on: Jun 25, 2019
 *      Author: josetobias
 */

#include "../includes/logger.h"
#include <math.h>

void show_results(pixel_stack *s) {
	printf("Ponto inicial: [%d, %d]\n", s->top->pixel->y, s->top->pixel->x);
	printf("Nro. de pontos: %d", s->size - 1);

	_show_contour(*s);

	int *chain = build_chain(*s);
	_show_chain(chain, s->size);
	int *binary = build_binary(chain, s->size - 1);
	_show_binary(binary, s->size - 1);
	_show_nibble(binary, s->size - 1);
	_show_hexa(binary, s->size - 1);
}

void _show_binary(int *b, int size) {
	printf("\nEm Binário: [");
	for (int i = 0; i < size; i++) {
		for (int k = 0; k < 3; k++) {
			printf("%d", b[i * 3 + k]);
		}
		if (i + 1 < size) {
			printf(" ");
		}
	}
	printf("]");
}

void _show_nibble(int *b, int size) {
	printf("\nEm Nibble: [");
	for (int i = 0; i < size * 3; i += 4) {
		for (int k = 0; k < 4; k++) {
			if (i + k < size * 3) {
				printf("%d", b[i + k]);
			} else {
				printf("%d", 0);
			}
		}
		if (i + 4 < size * 3) {
			printf(" ");
		}
	}
	printf("]");
}

void _show_hexa(int *b, int size) {
	printf("\nEm Hexa = [");
	for (int i = 0; i < size * 3; i += 4) {
		int val = 0;
		for (int k = 0; k < 4; k++) {
			if (i + k < size * 3) {
				val += b[i + k] * pow(2, 3 - k);
			} else {
				val += 0;
			}
		}
		printf("%X", val);
	}
	printf("]");
}

void _show_contour(pixel_stack s) {
	printf("\nContorno = [");
	int counter = 0;
	while (1) {
		counter++;
		printf("(%d, %d)", s.top->pixel->y, s.top->pixel->x);
		s.top = s.top->next;
		if (s.top != NULL) {
			printf(", ");
		} else {
			break;
		}
		if (counter == LS) {
			printf("\n");
			counter = 0;
		}
	}
	printf("]");
}

void _show_chain(int *c, int size) {
	printf("\nCadeia = [");
	for (int i = 0; i < size - 1; i++) {
		printf("%d", c[i]);
		if (i + 1 < size - 1) {
			printf(" ");
		}
	}
	printf("]");
}
