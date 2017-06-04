from generator import UniformKnapsackGen, GaussianKnapsackGen
from solver import DynamicKnapsack, ModifiedGreedyKnapsack, GreedyKnapsack

import json
import time
from itertools import chain

from functools import partial
from itertools import product

from operator import itemgetter

OUTPUT_FILE = 'data/dev_sweep/dev_sweep.json'

BATCH_SIZE = 1000

SOLVER_CLASSES = [DynamicKnapsack, ModifiedGreedyKnapsack, GreedyKnapsack]

GENERATOR_CLASS = GaussianKnapsackGen
GENERATOR_PARAMS = {
    'mode' : 'density',
    'n': 32,
    'capacity' : 1.0,
    'meanSize' : 0.5,
    'devSize' : 0.15,
    'meanDensity' : 0.5,
    'devDensity' : 0.15
}

MODULATED_PARAM = 'devSize'
VALUES = [0.001 * x for x in range(150,0,-1)]

def time_op(partial):
    start = time.clock()
    result = partial()
    end = time.clock()
    return (end-start), result

if __name__ == '__main__':
    with open(OUTPUT_FILE,'w') as f:
        gen = GENERATOR_CLASS(**GENERATOR_PARAMS)
        modulate = partial(setattr, gen, MODULATED_PARAM)

        solvers = [s() for s in SOLVER_CLASSES]

        data = list()

        for n in VALUES:
            print('Running %s=%s...' % (MODULATED_PARAM, n))
            modulate(n)

            problems = [gen.generate() for i in range(BATCH_SIZE)]

            times = [None for p in problems]
            values = [None for p in problems]

            for idx, prob in enumerate(problems):
                partials = [partial(s.solve,prob) for s in solvers]
                t, sol = zip(*map(time_op, partials))
                times[idx] = t
                values[idx] = list(map(itemgetter(0), sol))

            data.append({
                MODULATED_PARAM : n,
                'times' : times,
                'values' : values
            })

        info = {
            'solvers' : [sc.__name__ for sc in SOLVER_CLASSES],
            'batch_size' : BATCH_SIZE,
            'generator' : GENERATOR_CLASS.__name__,
            'fixed' : GENERATOR_PARAMS,
            'modulated' : MODULATED_PARAM,
            'values' : VALUES,
            'data' : data
        }

        json.dump(info, f)
