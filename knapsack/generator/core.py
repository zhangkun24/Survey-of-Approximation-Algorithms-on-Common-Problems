import random

class KnapsackGenerator:
    def __init__(self, n=10, seed=None):
        self.seed = seed
        self.random = random.Random(self.seed)
        self.n = n

    def reseed(self, seed):
        self.seed = seed
        self.random.seed(seed)

    def reset(self):
        self.random.seed(self.seed)
