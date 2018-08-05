import java.util.*;

/**
 *	Interface that all compression suites must implement. That is they must be
 *	able to compress a file and also reverse/decompress that process.
 * 
 *	@author Brian Lavallee
 *	@since 5 November 2015
 *  @author Owen Atrachan
 *  @since December 1, 2016
 */
public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); // or 256
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;
	public static final int HUFF_COUNTS = HUFF_NUMBER | 2;

	public enum Header{TREE_HEADER, COUNT_HEADER};
	public Header myHeader = Header.TREE_HEADER;
	
	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){
	    int[] counts = readForCounts(in);
	    HuffNode root = makeTreeFromCounts(counts);
	    String[] codings = makeCodingsFromTree(root);
	    writeHeader(root, out);
	    in.reset();
	    writeCompressedBits(in, codings, out);
	}

	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	
	public void decompress(BitInputStream in, BitOutputStream out){
		int id = in.readBits(BITS_PER_INT);//read input
		
		if(id != HUFF_TREE && id != HUFF_NUMBER)//check if valid
		{
			throw new HuffException("Invalid");
		}
		
		HuffNode root = readTreeHeader(in);//root
		readCompressedBits(in, out, root);//value
	}
	
	public HuffNode readTreeHeader(BitInputStream in)
	{
		int val = in.readBits(1);
		
		if (val == 0) {								//if val turns out to be internal
			HuffNode left = readTreeHeader(in);
			HuffNode right = readTreeHeader(in);
			return new HuffNode(0, 0, left, right);
		} 
		
		else {
			int retVal = in.readBits(BITS_PER_WORD + 1);// if leaf, read 9 bits
			return new HuffNode(retVal, 0, null, null); 
		}

	}
	
	public void readCompressedBits(BitInputStream in, BitOutputStream out, HuffNode tree) {
		HuffNode current = tree;           			// as always, we start at root
		while (true) {
			int bit = in.readBits(1); 
			
			if (bit == -1) {
				throw new HuffException("Illegal input");
			} 
			
			else {
				
				if (bit == 0) {						// if bit = 0
					current = current.left();
				} 
				
				else {
					current = current.right();    	 // if bit = 1
				}

				if (current.left() == null && current.right() == null) {
					
					if (current.value() == PSEUDO_EOF) {  // end of file
						break;
					} 
					
					else {
						out.writeBits(BITS_PER_WORD, current.value());
						current = tree; 
					}
				}
			}
		}
	}
	
	public int[] readForCounts(BitInputStream in) {
		int[] ret = new int[256];
		while (true)
		{
	        int val = in.readBits(BITS_PER_WORD); //read value
	        if (val == -1)
	        {
	        		break;
	        }
	        ret[val] ++;  //goes up by one
		}
		
		int cnt = 0;      // counter
		for (int k : ret) {
			if (k > 0) {
				cnt++;
			}
		}
		System.out.println("Number of nodes: " + cnt);

		return ret;
	}
	
	public HuffNode makeTreeFromCounts(int[] ret) {
		Queue<HuffNode> pq = new PriorityQueue<>();
		int size = 0;
		for(int i = 0; i < ret.length; i++) {  //browse values
			if(ret[i] > 0) {
				pq.add(new HuffNode(i, ret[i]));
				size++;
			}
			pq.add(new HuffNode(PSEUDO_EOF, 1, null, null));  //for eof
		}
		System.out.println(size);
		
		while(pq.size() > 1) {
			HuffNode left = pq.remove();
			HuffNode right = pq.remove();
			HuffNode t = new HuffNode(-1, left.weight() + right.weight(), left, right);
			pq.add(t); //add tree to queue
		}
		HuffNode root = pq.remove(); //last node left
		return root;
	}
	
	public String[] makeCodingsFromTree(HuffNode tree) {
		return makeCodingsHelper(tree, "", new String[257]);  //initial value
	}
	
public String[] makeCodingsHelper(HuffNode tree, String path, String[] ret) {
		if (tree == null){
			return null;
		}
		
		if(tree.left() == null && tree.right() == null){
			ret[tree.value()] = path; 
		}

		else{
				makeCodingsHelper(tree.left(), path + "0", ret);
				makeCodingsHelper(tree.right(), path + "1", ret);
		}
		
		return ret;
	}

	public void setHeader(Header header) {
        myHeader = header;
        System.out.println("header set to "+myHeader);
    }
	
	public void writeHeader(HuffNode tree, BitOutputStream out) {
		out.writeBits(32, HUFF_TREE);  // writing the HuffTree
		writeTree(tree, out); //recursive
	}

	public void writeTree(HuffNode tree, BitOutputStream out) {
		
		if (tree.left() == null && tree.right() == null) {  // if it's a leaf
			out.writeBits(1, 1);						
			out.writeBits(BITS_PER_WORD + 1, tree.value()); 
		} 
		
		else {
			out.writeBits(1, 0);// internal is 0
			writeTree(tree.left(), out);
			writeTree(tree.right(), out);
		}
	}

	public void writeCompressedBits(BitInputStream in, String[] encodings, BitOutputStream out) {
		
		while (true) {
			int val = in.readBits(BITS_PER_WORD);
			
			if (val == -1) {
				break;
			}
			
			String encode = encodings[val];   // get the string for value
			out.writeBits(encode.length(), Integer.parseInt(encode, 2));  // convert string to int

		}
		out.writeBits(encodings[PSEUDO_EOF].length(), Integer.parseInt(encodings[PSEUDO_EOF], 2)); //eof
	}

}