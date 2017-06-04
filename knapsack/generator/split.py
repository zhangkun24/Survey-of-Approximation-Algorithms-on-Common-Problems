from .core import KnapsackGenerator
from problem import KnapsackProblem, KnapsackItem

import random
from itertools import islice

MINISCULE = 0.00000001

class SplitKnapsackGen(KnapsackGenerator):
    def __init__(self, **params):
        super().__init__(params.get('n', 10), params.get('seed', None))

        self.mode = params.get('mode', 'density')

        self.meanCapacity = params.get('meanCapacity', 10.0)
        self.devCapacity = params.get('devCapacity', 0.0)

        self.meanProfit = params.get('meanProfit', 4.0)
        self.devProfit = params.get('devProfit', 1.0)

        self.meanDensity = params.get('meanDensity', 0.3)
        self.devDensity= params.get('devDensity', 1.0)

        self.totalSize = params.get('totalSize', 100.0)

    def generate(self):
        capacity = self.random.gauss(self.meanCapacity, self.devCapacity)
        sizes = self.generate_sizes()

        if self.mode == 'size':
            universe = list(map(KnapsackItem,
                self.generate_sizes(),
                self.generate_profits()))
        else:
            universe = map(lambda x, y: KnapsackItem(x, x*y),
                self.generate_sizes(),
                self.generate_densities())

        return KnapsackProblem(capacity, universe)

    def generate_profits(self, size):
        for i in range(self.n):
            yield max(0.0, self.random.gauss(self.meanProfit, self.devProfit))

    def generate_densities(self):
        for i in range(self.n):
            yield max(0.0, self.random.gauss(self.meanDensity, self.devDensity))

    def generate_sizes(self):
        just_under = self.totalSize - MINISCULE
        cracks = [random.uniform(0.0, just_under) for i in range(self.n-1)]
        cracks.sort()
        last = 0.0
        for crack in cracks:
            size = max(MINISCULE, crack - last)
            last += size
            yield size

        yield max(MINISCULE, just_under - last)
