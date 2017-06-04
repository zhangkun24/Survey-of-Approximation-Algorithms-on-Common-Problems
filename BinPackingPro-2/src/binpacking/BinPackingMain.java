package binpacking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinPackingMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int[] itemNumber = new int[]{10000};
		List<Integer> numRes = new ArrayList<Integer>();
		List<Integer> totalRes = new ArrayList<Integer>();
		
		
		List<Integer> nextFitRes = new ArrayList<Integer>();
		List<Long> nextFitResTime = new ArrayList<Long>();
		
		List<Integer> firstFitRes = new ArrayList<Integer>();
		List<Long> firstFitResTime = new ArrayList<Long>();
		
		List<Integer> bestFitRes = new ArrayList<Integer>();
		List<Long> bestFitResTime = new ArrayList<Long>();
		
		List<Integer> worstFitRes = new ArrayList<Integer>();
		List<Long> worstFitResTime = new ArrayList<Long>();
		
		List<Integer> fullFitRes = new ArrayList<Integer>();
		List<Long> fullFitResTime = new ArrayList<Long>();
		
		List<Integer> modFullFitRes = new ArrayList<Integer>();
		List<Long> modFullFitResTime = new ArrayList<Long>();
		
		List<Integer> snextFitRes = new ArrayList<Integer>();
		List<Long> snextFitResTime = new ArrayList<Long>();
		
		List<Integer> sfirstFitRes = new ArrayList<Integer>();
		List<Long> sfirstFitResTime = new ArrayList<Long>();
		
		List<Integer> sbestFitRes = new ArrayList<Integer>();
		List<Long> sbestFitResTime = new ArrayList<Long>();
		
		List<Integer> sworstFitRes = new ArrayList<Integer>();
		List<Long> sworstFitResTime = new ArrayList<Long>();
		
		List<Integer> sfullFitRes = new ArrayList<Integer>();
		List<Long> sfullFitResTime = new ArrayList<Long>();
		
		List<Integer> smodFullFitRes = new ArrayList<Integer>();
		List<Long> smodFullFitResTime = new ArrayList<Long>();
		
		for(int i=0; i< itemNumber.length; i++){
			
			for(int j=0; j<10; j++){
				System.out.println("iteration: " + i + ": " + j);
				
				List<Integer> items = new ArrayList<>();
				items = Greedy.produceItems(itemNumber[i]);
				int total = Greedy.totalSize(items);
				
				numRes.add(itemNumber[i]);
				totalRes.add(total);
				
				long start = System.nanoTime();
				int cnt = Greedy.nextFit(items);
				//long end = System.currentTimeMillis();
				long time = System.nanoTime() - start;
				nextFitRes.add(cnt);
				nextFitResTime.add(time);
				
				long startf = System.nanoTime();
				int cntf = Greedy.firstFit(items);
				//long endf = System.currentTimeMillis();
				long timef = System.nanoTime() - startf;
				firstFitRes.add(cntf);
				firstFitResTime.add(timef);
				
				long startb = System.nanoTime();
				int cntb = Greedy.bestFit(items);
				//long endb = System.currentTimeMillis();
				long timeb = System.nanoTime() - startb;
				bestFitRes.add(cntb);
				bestFitResTime.add(timeb);
				
				long startw = System.nanoTime();
				int cntw = Greedy.worstFit(items);
				//long endw = System.currentTimeMillis();
				long timew = System.nanoTime() - startw;
				worstFitRes.add(cntw);
				worstFitResTime.add(timew);
				
				long startfu = System.currentTimeMillis();
				int cntfu = FullFit.fullFit(items);
				long endfu = System.currentTimeMillis();
				long timefu = endfu - startfu;
				fullFitRes.add(cntfu);
				fullFitResTime.add(timefu);
				System.out.println("fullFit time: " + timefu);
				
				long startmd = System.currentTimeMillis();
				int cntmd = FullFit.modifiedFullFit(items);
				long endmd = System.currentTimeMillis();
				long timemd = endmd - startmd;
				modFullFitRes.add(cntmd);
				modFullFitResTime.add(timemd);
				
				Collections.sort(items, Collections.reverseOrder());
				
				long sstart = System.nanoTime();
				int scnt = Greedy.nextFit(items);
				//long send = System.currentTimeMillis();
				long stime = System.nanoTime() - sstart;
				snextFitRes.add(scnt);
				snextFitResTime.add(stime);
				
				long sstartf = System.nanoTime();
				int scntf = Greedy.firstFit(items);
				//long sendf = System.currentTimeMillis();
				long stimef = System.nanoTime() - sstartf;
				sfirstFitRes.add(scntf);
				sfirstFitResTime.add(stimef);
				
				long sstartb = System.nanoTime();
				int scntb = Greedy.bestFit(items);
				//long sendb = System.currentTimeMillis();
				long stimeb = System.nanoTime() - sstartb;
				sbestFitRes.add(scntb);
				sbestFitResTime.add(stimeb);
				
				long sstartw = System.nanoTime();
				int scntw = Greedy.worstFit(items);
				//long sendw = System.currentTimeMillis();
				long stimew = System.nanoTime() - sstartw;
				sworstFitRes.add(scntw);
				sworstFitResTime.add(stimew);
				
				long sstartfu = System.nanoTime();
				int scntfu = FullFit.fullFit(items);
				//long sendfu = System.currentTimeMillis();
				long stimefu = System.nanoTime() - sstartfu;
				sfullFitRes.add(scntfu);
				sfullFitResTime.add(stimefu);
				
				long sstartmd = System.nanoTime();
				int scntmd = FullFit.modifiedFullFit(items);
				//long sendmd = System.currentTimeMillis();
				long stimemd = System.nanoTime() - sstartmd;
				smodFullFitRes.add(scntmd);
				smodFullFitResTime.add(stimemd);
			}
		}
		
		try(  PrintWriter num = new PrintWriter( "itemsNumber.txt" )  ){
		    num.println(numRes.toString());
		}
		
		try(  PrintWriter out = new PrintWriter( "totalSize.txt" )  ){
		    out.println(totalRes.toString());
		}
		
		try(  PrintWriter outn = new PrintWriter( "nextFitBins.txt" )  ){
		    outn.println(nextFitRes.toString());
		}
		
		try(  PrintWriter outnt = new PrintWriter( "nextFitTime.txt" )  ){
		    outnt.println(nextFitResTime.toString());
		}
		
		try(  PrintWriter outf = new PrintWriter( "firstFitBins.txt" )  ){
		    outf.println(firstFitRes.toString());
		}
		
		try(  PrintWriter outft = new PrintWriter( "firstFitTime.txt" )  ){
		    outft.println(firstFitResTime.toString());
		}
		
		
		try(  PrintWriter outb = new PrintWriter( "bestFitBins.txt" )  ){
		    outb.println(bestFitRes.toString());
		}
		
		try(  PrintWriter outbt = new PrintWriter( "bestFitTime.txt" )  ){
		    outbt.println(bestFitResTime.toString());
		}
		
		try(  PrintWriter outw = new PrintWriter( "worstFitBins.txt" )  ){
		    outw.println(worstFitRes.toString());
		}
		
		try(  PrintWriter outwt = new PrintWriter( "worstFitTime.txt" )  ){
		    outwt.println(worstFitResTime.toString());
		}
		
		try(  PrintWriter outff = new PrintWriter( "fullFitBins.txt" )  ){
		    outff.println(fullFitRes.toString());
		}
		
		try(  PrintWriter outfft = new PrintWriter( "fullFitTime.txt" )  ){
		    outfft.println(fullFitResTime.toString());
		}
		
		try(  PrintWriter outmd = new PrintWriter( "modFullFitBins.txt" )  ){
		    outmd.println(modFullFitRes.toString());
		}
		
		try(  PrintWriter outmdt = new PrintWriter( "modFullFitTime.txt" )  ){
		    outmdt.println(modFullFitResTime.toString());
		}

		try(  PrintWriter soutn = new PrintWriter( "sortedNextFitBins.txt" )  ){
		    soutn.println(snextFitRes.toString());
		}
		
		try(  PrintWriter soutnt = new PrintWriter( "sortedNextFitTime.txt" )  ){
		    soutnt.println(snextFitResTime.toString());
		}
		
		try(  PrintWriter soutf = new PrintWriter( "sortedFirstFitBins.txt" )  ){
		    soutf.println(sfirstFitRes.toString());
		}
		
		try(  PrintWriter soutft = new PrintWriter( "sortedFirstFitTime.txt" )  ){
		    soutft.println(sfirstFitResTime.toString());
		}
		
		
		try(  PrintWriter soutb = new PrintWriter( "sortedBestFitBins.txt" )  ){
		    soutb.println(sbestFitRes.toString());
		}
		
		try(  PrintWriter soutbt = new PrintWriter( "sortedBestFitTime.txt" )  ){
		    soutbt.println(sbestFitResTime.toString());
		}
		
		try(  PrintWriter soutw = new PrintWriter( "sortedWorstFitBins.txt" )  ){
		    soutw.println(sworstFitRes.toString());
		}
		
		try(  PrintWriter soutwt = new PrintWriter( "sortedWorstFitTime.txt" )  ){
		    soutwt.println(sworstFitResTime.toString());
		}
		
		try(  PrintWriter soutff = new PrintWriter( "sortedFullFitBins.txt" )  ){
		    soutff.println(sfullFitRes.toString());
		}
		
		try(  PrintWriter soutfft = new PrintWriter( "sortedFullFitTime.txt" )  ){
		    soutfft.println(sfullFitResTime.toString());
		}
		
		try(  PrintWriter soutmd = new PrintWriter( "sortedModFullFitBins.txt" )  ){
		    soutmd.println(smodFullFitRes.toString());
		}
		
		try(  PrintWriter soutmdt = new PrintWriter( "sortedModFullFitTime.txt" )  ){
		    soutmdt.println(smodFullFitResTime.toString());
		}

	}

}

