/*
 * imagelib.h
 *
 *  Created on: Jun 19, 2019
 *      Author: josetobias
 */

#ifndef INCLUDES_IMAGELIB_H_
#define INCLUDES_IMAGELIB_H_

#include "tracker.h"

typedef struct figure_ {
	int *image;
	int x_dim;
	int y_dim;
} figure;

figure* create_figure(int x_dim, int y_dim);

void destroy_figure(figure *fig);

figure* read_image(char *path);

void write_chain(char *path, figure *fig, int *c, pixel_stack *stack);

#endif /* INCLUDES_IMAGELIB_H_ */
