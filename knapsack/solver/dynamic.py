from collections import namedtuple
from itertools import chain
from heapq import merge
from operator import itemgetter

KnapsackItemset = namedtuple('Itemset', ['s', 'p', 'items'])
KnapsackItemset.from_item = lambda item: Itemset(item.s, item.p, [item])
KnapsackItemset.plus_item = lambda itemset, item: KnapsackItemset(
    itemset.s + item.s,
    itemset.p + item.p,
    itemset.items + [item])

KnapsackItemset.profit = itemgetter(1)

class DynamicKnapsack:
    def solve(self, problem):
        U = problem.universe
        B = problem.capacity
        N = len(U)

        A = [ KnapsackItemset(0,0,[]) ]

        for i, item in enumerate(U):
            feasible = filter(lambda iset: iset.s + item.s <= B , A)
            new_items = map(lambda iset: iset.plus_item(item), feasible)
            A = list(remove_dominated(merge(A, new_items)))

        best = max(A, key=itemgetter(1))

        return best.p, best.items


def remove_dominated(sorted_iter):
    max_profit = -1
    for itemset in sorted_iter:
        if itemset.p > max_profit:
            max_profit = itemset.p
            yield itemset
