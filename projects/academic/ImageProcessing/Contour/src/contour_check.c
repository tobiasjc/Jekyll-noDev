/*
 * coutour_check.c
 *
 *  Created on: 19 de jun de 2019
 *      Author: jose
 */

#include "contour_check.h"

pixel* find_next(pixel *p, pixel *c, figure *fig) {
	pixel *tmp;

	if ((c->x + 1) == p->x && c->y == p->y) {
		if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y + 1, p->x - 1);
		}
	} else if (c->x == p->x && (c->y + 1) == p->y) {
		if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);
		}
	} else if ((c->x - 1) == p->x && c->y == p->y) {
		if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y - 1, p->x + 1);
		}
	} else if (c->x == p->x && (c->y - 1) == p->y) {
		if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);
		}
	} else if ((c->x + 1) == p->x && (c->y + 1) == p->y) {
		if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y, p->x + 1);
		}
	} else if ((c->x + 1) == p->x && (c->y - 1) == p->y) {
		if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);
		}
	} else if ((c->x - 1) == p->x && (c->y + 1) == p->y) {
		if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			tmp = create_pixel(p->y, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x - 1);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);
		}
	} else if ((c->x - 1) == p->x && (c->y - 1) == p->y) {
		if (fig->image[(p->y - 1) * fig->x_dim + (p->x)]) {
			tmp = create_pixel(p->y - 1, p->x);

		} else if (fig->image[(p->y - 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x - 1;
			tmp = create_pixel(p->y - 1, p->x + 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x;
			tmp = create_pixel(p->y, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x + 1)]) {
			c->y = p->y - 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x + 1);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x)]) {
			c->y = p->y;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x);

		} else if (fig->image[(p->y + 1) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x + 1;
			tmp = create_pixel(p->y + 1, p->x - 1);

		} else if (fig->image[(p->y) * fig->x_dim + (p->x - 1)]) {
			c->y = p->y + 1;
			c->x = p->x;
			tmp = create_pixel(p->y, p->x - 1);
		}
	}

	return tmp;
}
