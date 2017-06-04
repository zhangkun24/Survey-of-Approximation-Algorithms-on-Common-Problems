from collections import namedtuple

KnapsackItem = namedtuple('KnapsackItem', ['s', 'p'])

class KnapsackProblem:
    def __init__(self, capacity, universe):
        self.universe = list(universe) # list of (size, profit) named tuples
        self.capacity = capacity # float of capacity
