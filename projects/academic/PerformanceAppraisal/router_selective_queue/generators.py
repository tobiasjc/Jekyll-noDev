import math
import random

import helpers as hlps


def _gen_factor():
    return 1.0 - random.random()


def gen_exp(l):
    return (-1.0 / l) * math.log(_gen_factor())


def gen_packet_size():
    f = _gen_factor()

    if f <= 0.5:
        return hlps.byte_to_mb(550.00)
    elif f <= 0.9:
        return hlps.byte_to_mb(40.00)
    else:
        return hlps.byte_to_mb(1500.00)


def gen_cbr_interval():
    f = _gen_factor()

    if f <= 0.1:
        return 0.011
    elif f <= 0.2:
        return 0.012
    elif f <= 0.3:
        return 0.013
    elif f <= 0.4:
        return 0.014
    elif f <= 0.5:
        return 0.015
    elif f <= 0.6:
        return 0.016
    elif f <= 0.7:
        return 0.017
    elif f <= 0.8:
        return 0.018
    elif f <= 0.9:
        return 0.019
    elif f <= 1.0:
        return 0.020
