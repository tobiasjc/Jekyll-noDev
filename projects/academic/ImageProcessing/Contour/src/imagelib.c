/*
 * imagelib.c
 *
 *  Created on: Jun 19, 2019
 *      Author: josetobias
 */

#include "../includes/imagelib.h"

#include <bits/types/FILE.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "../includes/converter.h"

figure* create_figure(int x_dim, int y_dim) {
	figure *tmp = (figure*) malloc(sizeof(figure));
	tmp->image = (int*) malloc(sizeof(int) * y_dim * x_dim);
	tmp->x_dim = x_dim;
	tmp->y_dim = y_dim;
	return tmp;
}

void destroy_figure(figure *fig) {
	free(fig->image);
	free(fig);
}

figure* read_image(char *path) {
	FILE *fp;

	if ((fp = fopen(path, "r")) == NULL) {
		printf("Problem opening file from <%s>\n", path);
		abort();
	}

	char batch[100];
	fgets(batch, 80, fp);

	if (!strstr(batch, "P1")) {
		printf("File is not of PBM type.");
		abort();
	}

	do {
		fgets(batch, 80, fp);
	} while (strchr(batch, '#'));

	int y_dim;
	int x_dim;

	sscanf(batch, "%d %d", &x_dim, &y_dim);

	figure *fig = create_figure(x_dim, y_dim);
	int *image = fig->image;

	for (int y = 0; y < y_dim; ++y) {
		for (int x = 0; x < x_dim; ++x) {
			fscanf(fp, "%d", &image[y * x_dim + x]);
		}
	}

	return fig;
}

void write_chain(char *path, figure *fig, int *c, pixel_stack *stack) {
	FILE *file;

	if ((file = fopen(path, "w")) == NULL) {
		printf("Problema identificado na abertura do arquivo final.");
	}

	fprintf(file, "%d %d\n\n", fig->y_dim, fig->x_dim);

	fprintf(file, "%d %d\n\n", stack->top->pixel->y, stack->top->pixel->x);

	fprintf(file, "%d\n\n", stack->size);

	int *b = build_binary(c, stack->size);

	int size = stack->size - 1;

	for (int i = 0; i < size * 3; i += 4) {
		int val = 0;
		for (int k = 0; k < 4; k++) {
			if (i + k < size * 3) {
				val += b[i + k] * pow(2, 3 - k);
			} else {
				val += 0;
			}
		}
		fprintf(file, "%X", val);
	}
}

