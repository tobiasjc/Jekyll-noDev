/*
 * converter.h
 *
 *  Created on: Jun 26, 2019
 *      Author: josetobias
 */

#ifndef INCLUDES_CONVERTER_H_
#define INCLUDES_CONVERTER_H_

#include "tracker.h"

char* build_nibble_hexa(int *b, int size);

int* build_chain(pixel_stack s);

int* build_binary(int *c, int size);

#endif /* INCLUDES_CONVERTER_H_ */
