from problem import KnapsackItem, KnapsackProblem
from solver import ModifiedGreedyKnapsack, DynamicKnapsack
from operator import itemgetter

PROBLEM_IDS = ['p01','p02','p03','p04','p05','p06','p07','p08']

CAPACITY_FORMAT= 'validation/%s_c.txt'
SIZE_FORMAT= 'validation/%s_w.txt'
PROFIT_FORMAT= 'validation/%s_p.txt'
SOLUTION_FORMAT= 'validation/%s_s.txt'

def load_problem(problem_id):
    capacity_file = CAPACITY_FORMAT % problem_id
    size_file = SIZE_FORMAT % problem_id
    profit_file = PROFIT_FORMAT % problem_id
    solution_file = SOLUTION_FORMAT % problem_id

    with open(capacity_file, 'r') as c:
        capacity = int(c.read().strip())

    with open(solution_file, 'r') as s:
        solution = list(map(lambda x: int(x.strip()), s))

    with open(size_file, 'r') as w:
        with open(profit_file, 'r') as p:
            sizes = map(lambda x: int(x.strip()), w)
            profits = map(lambda x: int(x.strip()), p)

            universe = map(KnapsackItem, sizes, profits)
            problem = KnapsackProblem(capacity, universe)


    return problem, solution

def vectorize_solution(solution, universe):
    return

if __name__ == '__main__':
    solver = DynamicKnapsack()
    apprx = ModifiedGreedyKnapsack()

    total = 0
    for problem_id in PROBLEM_IDS:
        problem, known_sol = load_problem(problem_id)
        util, sol = solver.solve(problem)
        a_util, a_sol = apprx.solve(problem)

        vectorized = [(1 if item in sol else 0) for item in problem.universe]

        if vectorized != known_sol:
            print("Solver failed problem \'%s\'" % problem_id)
            print('\tKnown: \t%s' % known_sol)
            print('\tGot: \t%s' % vectorized)
        else:
            print("Solver completed problem \'%s\'" % problem_id)
            total += 1
            ratio = float(a_util) / util
            print('\tGreedy ratio:\t%f' % ratio)

    print("\nResults: %d / %d" %(total, len(PROBLEM_IDS)))
