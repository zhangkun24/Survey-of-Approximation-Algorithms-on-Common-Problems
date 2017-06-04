package binpacking;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author zhangkun
 *
 */
public class Greedy {
	public static int BIN_SIZE = 100;
	public static int SMALL_SIZE = (int) (BIN_SIZE * 0.20);
	public static int ITEM_NUMBER = 10000;

	// Next Fit bin packing algorithm;
	public static int nextFit(List<Integer> items) {
		List<Integer> bins = new ArrayList<Integer>();
		if (items == null || items.size() == 0)
			return 0;
		bins.add(0);
		for (Integer item : items) {
			int sz = bins.size() - 1;
			if (item + bins.get(sz) <= BIN_SIZE) {
				bins.set(sz, item + bins.get(sz));
			} else {
				bins.add(item);
			}
		}
		return bins.size();
	}

	// Helper function for First Fit bin packing algorithm;
	public static List<Integer> firstFitHelper(List<Integer> items,
			List<Integer> bins) {
		if (items == null || items.size() == 0)
			return bins;
		bins.add(0);
		for (Integer item : items) {
			int index = findFitBin(bins, item);
			if (index != -1) {
				bins.set(index, bins.get(index) + item);
			} else {
				bins.add(item);
			}
		}
		return bins;
	}

	// First Fit Bin Packing Algorithm;
	public static int firstFit(List<Integer> items) {
		List<Integer> bins = new ArrayList<>();
		bins = firstFitHelper(items, bins);
		return bins.size();
	}

	// Helper function for First Fit algorithm;
	private static int findFitBin(List<Integer> bins, int item) {
		if (bins == null || bins.size() == 0)
			return -1;
		int index = -1;
		for (int i = 0; i < bins.size(); i++) {
			if (bins.get(i) + item <= BIN_SIZE) {
				index = i;
				break;
			}
		}
		return index;
	}

	// Worst Fit bin packing algorithm;
	public static int worstFit(List<Integer> items) {
		List<Integer> bins = new ArrayList<Integer>();
		if (items == null || items.size() == 0)
			return 0;

		bins.add(0);
		for (Integer item : items) {
			int index = findEmptiestBin(bins);
			if (index != -1 && item + bins.get(index) <= BIN_SIZE) {
				bins.set(index, item + bins.get(index));
			} else {
				bins.add(item);
			}
		}
		return bins.size();
	}

	// Helper function to find the emptiest bin from bins list.
	private static int findEmptiestBin(List<Integer> bins) {
		if (bins == null || bins.size() == 0)
			return -1;
		int minSize = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < bins.size(); i++) {
			if (bins.get(i) < minSize) {
				minSize = bins.get(i);
				index = i;
			}
			continue;
		}
		return index;
	}

	// Helper function for Best Fit bin packing algorithm;
	public static List<Integer> bestFitHelper(List<Integer> items,
			List<Integer> bins) {
		if (items == null || items.size() == 0)
			return bins;

		bins.add(0);
		for (Integer item : items) {
			int index = findTightestBin(bins, item);
			if (index != -1) {
				bins.set(index, item + bins.get(index));
			} else {
				bins.add(item);
			}
		}
		return bins;
	}

	// Best Fit algorithm;
	public static int bestFit(List<Integer> items) {
		List<Integer> bins = new ArrayList<>();
		bins = bestFitHelper(items, bins);
		return bins.size();

	}

	// Helper function to find the tightest bin from bins list.
	private static int findTightestBin(List<Integer> bins, int item) {
		if (bins == null || bins.size() == 0)
			return -1;

		int minSize = Integer.MAX_VALUE;
		int index = -1;

		for (int i = 0; i < bins.size(); i++) {
			int leftSize = BIN_SIZE - item - bins.get(i);
			if (leftSize >= 0 && leftSize < minSize) {
				minSize = leftSize;
				index = i;
			}
			continue;
		}
		return index;
	}

	// function for producing number n of items with random size between 1 to
	// BIN_SIZE;
	public static List<Integer> produceItems(int n) {
		List<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < n/2; i++) {
			items.add(1 + (int) (Math.random() * BIN_SIZE));
		}
		for (int i = 0; i < n/2; i++) {
			//items.add(BIN_SIZE/10 + (int) (Math.random() * BIN_SIZE));
			
			items.add(ThreadLocalRandom.current().nextInt(BIN_SIZE/10, BIN_SIZE + 1));
		}
		return items;
	}

	// calculate the total size of items;
	public static int totalSize(List<Integer> items) {
		int total = 0;
		for (int i = 0; i < items.size(); i++) {
			int val = items.get(i);
			if(val<=BIN_SIZE){
				total += val;
			}
		}
		return total;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[] itemNumber = new int[]{100, 500, 2000, 4000, 8000};
		
		for(int i=0; i< itemNumber.length; i++){
			List<Integer> items = new ArrayList<>();
			items = produceItems(itemNumber[i]);
			for(int j=0; j<3; j++){
				//printFunction(items);
			}
		}
		
		List<Integer> items = produceItems(10);
		System.out.println(items.toString());
	}
}

