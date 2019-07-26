/*
 * converter.c
 *
 *  Created on: Jun 26, 2019
 *      Author: josetobias
 */

#include "converter.h"

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char* build_nibble_hexa(int *b, int size) {
	int quant = size * 3;

	char *tmp = (char*) malloc(sizeof(char) * quant);

	for (int i = 0; i < size * 3; i += 4) {
		int val = 0;
		for (int k = 0; k < 4; k++) {
			if (i + k < size * 3) {
				val += b[i + k] * pow(2, 3 - k);
			} else {
				val += 0;
			}
		}
		sprintf(&tmp[i], "%X", val);
	}

	return tmp;
}

int* build_binary(int *c, int size) {
	int *tmp = (int*) malloc(sizeof(int) * size * 3);

	for (int i = 0; i < size; i++) {
		int n = c[i];
		for (int k = 2; k >= 0; k--) {
			tmp[3 * i + k] = n % 2;
			n = n / 2;
		}
	}
	return tmp;
}

int* build_chain(pixel_stack s) {
	int *chain = (int*) malloc(sizeof(int) * s.size);

	pixel_comp *from = s.top;

	pixel *copy_f = create_pixel(from->pixel->y, from->pixel->x);

	pixel_comp *to = from->next;

	for (int i = 0; i < s.size; i++) {
		if (from->pixel->x == to->pixel->x) {
			if (from->pixel->y == (to->pixel->y - 1)) {
				chain[i] = 6;
			} else if (from->pixel->y == (to->pixel->y + 1)) {
				chain[i] = 2;
			}
		} else if (from->pixel->y == to->pixel->y) {
			if (from->pixel->x == (to->pixel->x - 1)) {
				chain[i] = 0;
			} else if (from->pixel->x == (to->pixel->x + 1)) {
				chain[i] = 4;
			}
		} else {
			if (from->pixel->x == (to->pixel->x - 1) && from->pixel->y == (to->pixel->y - 1)) {
				chain[i] = 7;
			} else if (from->pixel->x == (to->pixel->x - 1) && from->pixel->y == (to->pixel->y + 1)) {
				chain[i] = 1;
			} else if (from->pixel->x == (to->pixel->x + 1) && from->pixel->y == (to->pixel->y - 1)) {
				chain[i] = 5;
			} else if (from->pixel->x == (to->pixel->x + 1) && from->pixel->y == (to->pixel->y + 1)) {
				chain[i] = 3;
			}
		}
		from = to;
		to = from->next;
		if (to == NULL) {
			to = create_comp(copy_f);
		}
	}
	return chain;
}
