from problem import KnapsackItem
from operator import itemgetter

density = lambda i : float(i.p) / i.s
profit = itemgetter(1)

class GreedyKnapsack:
    def __init__(self, metric=density):
        self.metric = metric

    def solve(self, problem):
        by_metric = sorted(problem.universe, key=self.metric, reverse=True)

        solution = list()
        utility = 0.0
        space = problem.capacity

        for item in by_metric:
            if space >= item.s:
                solution.append(item)
                space -= item.s
                utility += item.p

        return utility, solution

class ModifiedGreedyKnapsack:
    def __init__(self):
        self.density_solver = GreedyKnapsack(metric = density)
        self.profit_solver = GreedyKnapsack(metric = profit)

    def solve(self, problem):
        d_util, d_sol = self.density_solver.solve(problem)
        p_util, p_sol = self.profit_solver.solve(problem)

        return (d_util, d_sol) if d_util > p_util else (p_util, p_sol)
