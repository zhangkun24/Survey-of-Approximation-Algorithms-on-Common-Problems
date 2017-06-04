import json
import csv
from operator import add

INPUT_FILE = 'data/dev_sweep/dev_sweep.json'
OUTPUT_FILE = 'data/dev_sweep/dev_sweep.csv'

if __name__ == '__main__':
    with open(INPUT_FILE, 'r') as f:
        info = json.load(f)

    exact, *apprx = info['solvers']
    batch_size = info['batch_size']
    mod_param = info['modulated']
    data = info['data']

    headings = ([mod_param]
        + ['%s Avg' % a for a in apprx]
        + ['%s Min' % a for a in apprx])


    with open(OUTPUT_FILE, 'w') as f:
        writer = csv.writer(f)

        print('\t'.join(headings))
        writer.writerow(headings)

        for batch in data:
            mod_value = batch[mod_param]

            sum_ratio = [0.0 for a in apprx]
            min_ratio = [1.0 for a in apprx]

            for opt, *app in batch['values']:
                ratio = [float(a) / opt for a in app]
                sum_ratio = map(add, sum_ratio, ratio)
                min_ratio = list(map(min, min_ratio, ratio))

            avg_ratio = [sr / batch_size for sr in sum_ratio]

            row = [mod_value] + avg_ratio + min_ratio
            writer.writerow(row)

            print('\t'.join(map(str,row)))
