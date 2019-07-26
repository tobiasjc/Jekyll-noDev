/*
 * tracker.c
 *
 *  Created on: Jun 18, 2019
 *      Author: josetobias
 */

#include "../includes/tracker.h"

#include <stdlib.h>

pixel_comp* create_comp(pixel *p) {
	pixel_comp *tmp = (pixel_comp*) malloc(sizeof(pixel_comp));
	tmp->pixel = p;
	tmp->next = NULL;
	return tmp;
}

pixel_stack* create_stack() {
	pixel_stack *tmp = (pixel_stack*) malloc(sizeof(pixel_stack));
	tmp->size = 0;
	tmp->top = NULL;
	return tmp;
}

void destroy_stack(pixel_stack *s) {
	while (s->top != NULL) {
		pixel_comp *tmp = s->top;
		s->top = tmp->next;
		free(tmp);
	}
}

void push(pixel_stack *s, pixel *p) {
	pixel_comp *tmp = create_comp(p);

	if (s->size == 0) {
		s->top = tmp;
	} else {
		tmp->next = s->top;
		s->top = tmp;
	}

	s->size += 1;
}

pixel* pop(pixel_stack *s) {
	pixel_comp *tmp = s->top;
	s->top = tmp->next;
	pixel *r = tmp->pixel;
	free(tmp);
	s->size -= 1;
	return r;
}

pixel* create_pixel(int y, int x) {
	pixel *tmp = (pixel*) malloc(sizeof(pixel));
	tmp->x = x;
	tmp->y = y;
	tmp->seen = 0;
	return tmp;
}

int compare_pixel(pixel *one, pixel *other) {
	if (one->x == other->x) {
		if (one->y == other->y) {
			return 1;
		}
	}
	return 0;
}

pixel* copy_pixel(pixel *p) {
	pixel *tmp = create_pixel(p->y, p->x);
	return tmp;
}

int has_pixel(pixel_stack *s, pixel *p) {
	pixel_comp *tmp_pc = s->top;
	while (tmp_pc != NULL) {
		pixel *tmp_p = tmp_pc->pixel;
		if (compare_pixel(tmp_p, p)) {
			return 1;
		}
		tmp_pc = tmp_pc->next;
	}
	return 0;
}
