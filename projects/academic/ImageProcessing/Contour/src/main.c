#include <stdio.h>

#include "../includes/contour_check.h"
#include "../includes/converter.h"
#include "../includes/imagelib.h"
#include "../includes/logger.h"
#include "../includes/tracker.h"

void run(pixel_stack *stack, figure *fig);


int main(int argc, char **argv) {
	pixel_stack *stack = create_stack();

	figure *fig = read_image(argv[1]);

	run(stack, fig);

	int *chain = build_chain(*stack);

	show_results(stack);

	write_chain("./contorno.txt", fig, chain, stack);

	destroy_stack(stack);
	destroy_figure(fig);

	return 0;
}

void run(pixel_stack *stack, figure *fig) {
	pixel *b_0 = NULL;
	pixel *c_0 = NULL;

	for (int y = 0, jumper = 0; y < fig->y_dim && jumper == 0; ++y) {
		for (int x = 0; x < fig->x_dim; ++x) {
			if (fig->image[y * fig->x_dim + x]) {
				b_0 = create_pixel(y, x);
				c_0 = create_pixel(y, x - 1);

				push(stack, b_0);

				jumper = 1;
				break;
			}
		}
	}

	pixel* c_1 = create_pixel(c_0->y, c_0->x);
	pixel *b_1 = find_next(b_0, c_1, fig);


	while(1){
		b_1 = find_next(stack->top->pixel, c_1, fig);
		if(compare_pixel(b_1, b_0) && compare_pixel(c_0, c_1)){
			break;
		}
		push(stack, b_1);
	}



//	pixel *b_1 = find_next(b_0, c_0, fig);
//
//	while (1) {
//		pixel *p = stack->top->pixel;
//		pixel *p_n = find_next(p, c_0, fig);
//
//		if (compare_pixel(p_n, b_0) && compare_pixel(b_1, find_next(p_n, c_0, fig))) {
//			break;
//		} else {
//			push(stack, p_n);
//		}
//	}
}

