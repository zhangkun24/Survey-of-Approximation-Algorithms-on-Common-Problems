import random

from .core import KnapsackGenerator
from problem import KnapsackProblem, KnapsackItem

from itertools import repeat

MINISCULE = 0.00000001

class GaussianKnapsackGen(KnapsackGenerator):
    def __init__(self, **params):
        super().__init__(params.get('n', 10), params.get('seed', None))

        self.mode = params.get('mode', 'density')

        capacity = params.get('capacity', None)

        if capacity:
            self.meanCapacity = capacity
            self.devCapacity = 0.0
        else:
            self.meanCapacity = params.get('meanCapacity', 10.0)
            self.devCapacity = params.get('devCapacity', 0.0)

        self.meanSize = params.get('meanSize', 3.0)
        self.devSize = params.get('devSize', 1.0)
        self.meanProfit = params.get('meanProfit', 4.0)
        self.devProfit = params.get('devProfit', 1.0)
        self.meanDensity = params.get('meanDensity', 0.3)
        self.devDensity= params.get('devDensity', 1.0)


    def generate(self):
        capacity = self.random.gauss(self.meanCapacity, self.devCapacity)
        if self.mode == 'size':
            universe = map(KnapsackItem, self.gen_sizes(), self.gen_profits)
        else:
            universe = map(lambda s, d: KnapsackItem(s, s*d),
                self.gen_sizes(), self.gen_densities())

        return KnapsackProblem(capacity, universe)

    def gen_profits(self):
        for i in range(self.n):
            yield max(MINISCULE,
                self.random.gauss(self.meanProfit,self.devProfit))

    def gen_densities(self):
        for i in range(self.n):
            yield max(0, self.random.gauss(self.meanDensity, self.devDensity))

    def gen_sizes(self):
        for i in range(self.n):
            yield max(MINISCULE, self.random.gauss(self.meanSize, self.devSize))
