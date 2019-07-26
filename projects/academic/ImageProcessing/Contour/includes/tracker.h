/*
 * tracker.h
 *
 *  Created on: Jun 18, 2019
 *      Author: josetobias
 */

#ifndef INCLUDES_TRACKER_H_
#define INCLUDES_TRACKER_H_

typedef struct pixel_ {
	int x;
	int y;
	int seen;
} pixel;

typedef struct pixel_comp_ {
	pixel *pixel;
	struct pixel_comp_ *next;
} pixel_comp;

typedef struct pixel_stack_ {
	pixel_comp *top;
	int size;
} pixel_stack;

pixel* create_pixel(int x, int y);

pixel_comp* create_comp(pixel *p);

pixel_stack* create_stack();

void destroy_stack(pixel_stack *s);

void push(pixel_stack *s, pixel *p);

pixel* pop(pixel_stack *s);

int compare_pixel(pixel *one, pixel *other);

pixel* copy_pixel(pixel *p);

int has_pixel(pixel_stack *s, pixel *p);

#endif /* INCLUDES_TRACKER_H_ */
