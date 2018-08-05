public class PercolationDFSFast extends PercolationDFS {
//sk546
    public PercolationDFSFast(int n) {

        super(n);
    }

    @Override
    public boolean isOpen(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        return super.isOpen(row, col);
    }

    @Override
    public void open(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        super.open(row, col);
    }

    @Override
    public boolean isFull(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        return super.isFull(row, col);
    }
    
    @Override
    public void updateOnOpen(int row, int col) {
    	int[] rowgrid = {0,-1,0,1}; 
		int[] colgrid = {-1,0,1,0};
		for(int i = 0; i < rowgrid.length; i++){ 
		     int numrow = row + rowgrid[i];
		     int numcol = col + colgrid[i];
		     if(inBounds(numrow,numcol))
			     if (isFull(numrow,numcol)){
			    		 dfs(row, col); 
			    	 }
		}
		if (isFull(row, col)) 
			for(int d = 0; d < rowgrid.length; d+= 1){ 
			     int numrow = row + rowgrid[d];
			     int numcol = col + colgrid[d];
			     if(inBounds(numrow,numcol)) 
			    		 dfs(numrow, numcol); 
			}

		}
   	
    }
    
   