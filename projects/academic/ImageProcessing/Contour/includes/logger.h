/*
 * logger.h
 *
 *  Created on: Jun 25, 2019
 *      Author: josetobias
 */

#ifndef INCLUDES_LOGGER_H_
#define INCLUDES_LOGGER_H_

#define LS 10

#include "tracker.h"
#include <stdio.h>
#include "converter.h"

void show_results(pixel_stack *s);

void _show_contour(pixel_stack s);

void _show_chain(int *c, int size);

void _show_binary(int *c, int size);

void _show_nibble(int *b, int size);

void _show_hexa(int *c, int size);

#endif /* INCLUDES_LOGGER_H_ */
