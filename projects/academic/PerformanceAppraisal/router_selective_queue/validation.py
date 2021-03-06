import sys


class Accumulator:
    past_time = 0.0
    area = 0.0
    packets_count = 0

    def cummulative_action(self, time_actual, up):
        self.area += self.packets_count * (time_actual - self.past_time)
        self.past_time = time_actual
        
        # tweak so we can count packets entering, exit and their differences at the same function
        if up:
            self.packets_count += 1
        elif not up:
            self.packets_count -= 1
        else:
            sys.exit()


class Validations:
    occupancy = 0.0
    total = Accumulator() # count packets entering and exiting the system
    arrive = Accumulator() # count packets entering the system
    left = Accumulator() # count packets exiting the system

    def enter(self, time_actual):
        self.total.cummulative_action(time_actual, True)
        self.arrive.cummulative_action(time_actual, True)

    def exit(self, time_actual):
        self.total.cummulative_action(time_actual, False)
        self.left.cummulative_action(time_actual, True)

    def use(self, time_actual, event_time):
        self.occupancy += event_time - time_actual
