import random

from .core import KnapsackGenerator
from problem import KnapsackProblem, KnapsackItem

from itertools import repeat

class UniformKnapsackGen(KnapsackGenerator):
    def __init__(self, **params):
        super().__init__(params.get('n', 10), params.get('seed', None))

        self.mode = params.get('mode', 'density')

        capacity = params.get('capacity', None)
        if capacity:
            self.minCapacity = capacity
            self.maxCapacity = capacity
        else:
            self.minCapacity = params.get('minCapacity', 1.0)
            self.maxCapacity = params.get('maxCapacity', 10.0)

        self.minSize = params.get('minSize', 0.5)
        self.maxSize = params.get('maxSize', 10.0)
        self.minProfit = params.get('minProfit', 1.0)
        self.maxProfit = params.get('maxProfit', 10.0)
        self.minDensity = params.get('minDensity', 0.5)
        self.maxDensity= params.get('maxDensity', 1.0)


    def generate(self):
        capacity = self.random.uniform(self.minCapacity, self.maxCapacity)

        if self.mode == 'size':
            universe = map(KnapsackItem, self.gen_sizes(), self.gen_profits())
        else:
            universe = map(lambda s, d: KnapsackItem(s, s*d),
                self.gen_sizes(), self.gen_densities())

        return KnapsackProblem(capacity, universe)

    def gen_profits(self):
        for i in range(self.n):
            yield self.random.uniform(self.minProfit,self.maxProfit)

    def gen_densities(self):
        for i in range(self.n):
            yield self.random.uniform(self.minDensity, self.maxDensity)

    def gen_sizes(self):
        for i in range(self.n):
            yield self.random.uniform(self.minSize, self.maxSize)
