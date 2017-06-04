import json
import csv
from operator import add

INPUT_FILE = '../data/n_sweep/n_sweep.json'
OUTPUT_FILE = '../data/n_sweep/ns_times.csv'
WRITE_TO_FILE = True

if __name__ == '__main__':
    with open(INPUT_FILE, 'r') as f:
        info = json.load(f)

    solvers = info['solvers']
    batch_size = info['batch_size']
    mod_param = info['modulated']
    data = info['data']

    headings = ([mod_param]
        + ['%s Avg' % s for s in solvers]
        + ['%s Max' % s for s in solvers])


    with open(OUTPUT_FILE, 'w') as f:
        writer = csv.writer(f)

        print('\t'.join(headings))
        writer.writerow(headings)

        for batch in data:
            mod_value = batch[mod_param]

            sum_time = [0.0 for s in solvers]
            max_time = [0.0 for s in solvers]

            for trial in batch['times']:
                sum_time = map(add, sum_time, trial)
                max_time = list(map(max, max_time, trial))

            avg_time = [st / batch_size for st in sum_time]

            row = [mod_value] + avg_time + max_time
            writer.writerow(row)

            print('\t'.join(map(str,row)))
