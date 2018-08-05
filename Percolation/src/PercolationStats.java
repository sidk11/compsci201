import java.util.*;
import java.lang.IllegalArgumentException;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	public double[] values;
	public int gridsize;
	public int runnum;
	
	public PercolationStats(int N, int T) {
		if((N <= 0) || (T <= 0)) 
			throw new IllegalArgumentException();
		runnum = T;
		values = new double[runnum];
		for(int k=0; k<runnum; k++) {
			IUnionFind finder = new QuickFind();
			IPercolate perc = new PercolationUF(N, finder);
			ArrayList<int[]> counter = new ArrayList<>();
			int[] index = {0};
			
			for(int i=0; i<N; i++) {
				for(int j=0; j<N; j++)
					counter.add(new int[] {i,j});
			}
			
			Collections.shuffle(counter, ourRandom);
			Iterator<int[]> change = counter.iterator();
			
			while(change.hasNext()) {
				index = change.next();
				perc.open(index[0], index[1]);
				if(perc.percolates()) break;
			}
			values[k] = (double) (perc.numberOfOpenSites())/(N*N);
		}
	}
	
	public double mean() {
		return StdStats.mean(values);
	}
	public double stddev() {
		return StdStats.stddev(values);
	}
	public double confidenceLow() {
		return mean() - ((1.96*stddev())/Math.sqrt(runnum));
	}
	public double confidenceHigh() {
		return mean() + ((1.96*stddev())/Math.sqrt(runnum));
	}
	
	public static void main(String[] args) {
		PercolationStats ps = new PercolationStats(50,100);
	      System.out.println(ps.mean());
	      ps = new PercolationStats(200,100);
	      System.out.println(ps.mean());
	}
}
