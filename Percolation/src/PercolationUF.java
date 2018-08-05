public class PercolationUF implements IPercolate {

    public QuickFind myFinder = new QuickFind();
    public int myOpenCount, gridsize;
    private final int VTOP;
    private final int VBOTTOM;
    boolean[][] myGrid;
    

    PercolationUF(int size, IUnionFind finder){
    	myGrid = new boolean[size][size];
    	gridsize=size;
        myFinder.initialize(size*size+2);
        VTOP=size*size;
        VBOTTOM=VTOP+1;
    }

    public int getIndex(int row,int col){
    	if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        return gridsize*(row)+col;
    }

    public void updateOnOpen(int row, int col) {
        // full or open, don't process
    	if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        if (row==0){
        	myFinder.union(getIndex(row,col),VTOP);
        }
        if (row==gridsize-1){
        	myFinder.union(getIndex(row,col),VBOTTOM);
        }
        if (inBounds(row-1, col)) {
    		if(isOpen(row-1, col)) {
    			myFinder.union(getIndex(row,col),getIndex(row-1,col));
    		}
        }
    	if (inBounds(row+1, col)) {
    		if(isOpen(row+1, col)) {
    			myFinder.union(getIndex(row,col),getIndex(row+1,col));
    		}
        }
    	if (inBounds(row, col+1)) {
    		if(isOpen(row, col+1)) {
    			myFinder.union(getIndex(row,col),getIndex(row,col+1));
    		}
        }
    	if (inBounds(row, col-1)) {
    		if(isOpen(row, col-1)) {
    			myFinder.union(getIndex(row,col),getIndex(row,col-1));
    		}
        }
    }
    
    public boolean inBounds(int row, int col) {
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}
    
    @Override
    public boolean isOpen(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        return myGrid[row][col];
    }
    
    @Override
    public boolean isFull(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
        return myFinder.connected(getIndex(row,col), VTOP);
    }
    
    @Override
    public void open(int row, int col) {
    	if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException("Out of bounds");
        }
		else if (myGrid[row][col])
			return;
		myOpenCount += 1;
		myGrid[row][col] = true;
		updateOnOpen(row,col);
	}
    
   @Override
   public boolean percolates() {
	   return myFinder.connected(VTOP, VBOTTOM);
   }
   
   @Override
   public int numberOfOpenSites() {
	   return myOpenCount;
   }
}
