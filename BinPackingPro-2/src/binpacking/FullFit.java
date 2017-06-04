package binpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FullFit {


	// Helper function to remove sub set of items;
	private static void removeSubList(List<Integer> items, List<Integer> sub) {
		if (items.size() == 0 || items == null || sub == null
				|| sub.size() == 0)
			return;
		for (Integer s : sub) {
			for (int j = 0; j < items.size(); j++) {
				if ((int) s == (int) items.get(j)) {
					items.remove(j);
					break;
				}
			}
		}
	}
	
	// Divide items into two groups: bigItems and smallItems based on the
	// SMALL_SIZE;
	public static List<List<Integer>> bigSmallItems(List<Integer> items) {
		List<List<Integer>> res = new ArrayList<>();
		if (items == null || items.size() == 0)
			return res;
		List<Integer> bigItems = new ArrayList<>();
		List<Integer> smallItems = new ArrayList<>();
		for (Integer item : items) {
			if (item > Greedy.SMALL_SIZE) {
				bigItems.add(item);
			} else {
				smallItems.add(item);
			}
		}
		res.add(bigItems);
		res.add(smallItems);
		return res;
	}

	
	  //helper function to find the best full fit item list.
	  private static List<Integer> subSetDP(List<Integer> items, int size) {
		  List<Integer> res = new ArrayList<>();
		  
		  int sz = items.size();
		  boolean[][] solution = new boolean[sz + 1][size + 1];
		  for(int i=1;i<=size;i++){
			  solution[0][i]=false;
		  }
	     for(int i=0;i<=sz;i++){
	        solution[i][0]=true;
	     }
	     for(int i=1;i<=sz;i++){
	        for(int j=1;j<=size;j++){
	           solution[i][j] = solution[i-1][j];
	           if(solution[i][j]==false && j>=items.get(i-1)){
	             solution[i][j] = solution[i][j] || solution[i-1][j-items.get(i-1)];
	           }
	         } 
	      }
	     
	     int rw = sz;
	     int col = size;
	     if(solution[rw][col] == false) return res;
	     
	     while(col > 0 && rw >0){
	    	 if(solution[rw][col] == true && solution[rw-1][col]==true){
	    		 rw--;
	    	 }
	    	 else if(solution[rw][col] == true && solution[rw][col-items.get(rw-1)]==true){
	    		 res.add(items.get(rw-1));
	    		 col -= items.get(rw-1); 
	    	 }
	     }
	      return res;
	 }
	  
	  public static List<Integer> fullFitHelper(List<Integer> items){
		  
		  List<Integer> itemsCopy = new ArrayList<>();
		  itemsCopy.addAll(items);
		  
		  List<Integer> bins = new ArrayList<Integer>();
		  int cnt = Greedy.BIN_SIZE;
		  List<Integer> tmp= new ArrayList<>();
		  
		  while(cnt>0){
			  tmp = subSetDP(itemsCopy, cnt);
			  if(tmp.size() == 0||tmp == null){
				  cnt--;
			  }
			  else{
				  bins.add(cnt);
				  removeSubList(itemsCopy, tmp);
			  }
		  }
		  return bins;
	  }

	// Full Fit function on items;
	public static int fullFit2(List<Integer> items) {
		List<Integer> itemsCopy = new ArrayList<Integer>();
		itemsCopy.addAll(items);
		List<Integer> bins = new ArrayList<>();
		bins = fullFitHelper(itemsCopy);
		return bins.size();
	}

	
	public static int fullFit(List<Integer> items){
		List<Integer> bins = new ArrayList<>();
		bins = fullFitHelper(items);
		return bins.size();
	}
	// Modified Full Fit function, which packing bigItems with full fit
	// algorithm,
	// Then pack small items with first fit algorithm;
	public static int modifiedFullFit(List<Integer> items) {
		List<List<Integer>> res = new ArrayList<>();
		res = bigSmallItems(items);
		List<Integer> bigItems = res.get(0);
		List<Integer> smallItems = res.get(1);
		List<Integer> bins = new ArrayList<>();
		bins = fullFitHelper(bigItems);
		bins = Greedy.firstFitHelper(smallItems, bins);
		return bins.size();
	}

	// Modified Full Fit function, which packing bigItems with full fit
	// algorithm,
	// Then pack small items with best fit algorithm;
	public static int modifiedFullFit2(List<Integer> items) {
		List<List<Integer>> res = new ArrayList<>();
		res = bigSmallItems(items);
		List<Integer> bigItems = res.get(0);
		List<Integer> smallItems = res.get(1);
		List<Integer> bins = new ArrayList<>();
		bins = fullFitHelper(bigItems);
		bins = Greedy.bestFitHelper(smallItems, bins);
		return bins.size();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Integer> items = Greedy.produceItems(20);
		Collections.sort(items, Collections.reverseOrder());

		System.out.println("total: "+ Greedy.totalSize(items));
		System.out.println(items.toString());
		System.out.println("Full Fit: " + fullFit(items));
		
		List<Integer> itemsCopy2 = new ArrayList<Integer>();
		itemsCopy2.addAll(items);
		//System.out.println("Full Fit: " + modifiedFullFit(itemsCopy2));
		
		List<Integer> itemsCopy3 = new ArrayList<Integer>();
		itemsCopy3.addAll(items);
		//System.out.println("Full Fit: " + modifiedFullFit2(itemsCopy3));

	}


}
