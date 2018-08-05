
public class LinkStrand implements IDnaStrand {

	private class Node {
		String info;
		Node next;
		public Node(String s) {
			info = s;
			next = null;
		}
	}
	private Node myFirst,myLast;
	private long mySize;
	private int myAppends;
	private int myIndex;
	private int myLocalIndex;
	private Node myCurrent;

	public LinkStrand(String string) {
		initialize(string);
	}
	
	public LinkStrand(){
		this("");
	}
	@Override
	public long size() {
		return this.mySize;
	}

	@Override
	public void initialize(String source) {
		this.myFirst = new Node(source);
		this.myLast = this.myFirst;
		this.mySize = source.length();
		this.myAppends = 0;
		this.myIndex = 0;
		this.myLocalIndex = 0;
		this.myCurrent = this.myFirst;
	}

	@Override
	public IDnaStrand getInstance(String source) {
		return new LinkStrand(source);
	}


	@Override
	public IDnaStrand append(String dna) {
		this.myLast.next = new Node(dna);
		this.myLast = this.myLast.next;
		this.mySize += dna.length();
		this.myAppends++;
		return this;
	}
	
	public String toString() {
		StringBuilder temp = new StringBuilder();
		Node list = this.myFirst;
		while(list!=null) {
			temp.append(list.info);
			list = list.next;
		}
		return temp.toString();
	}

	@Override
	public IDnaStrand reverse() {
		Node last = null;
		Node copy = this.myFirst;
		Node temp = null;
		while(copy!=null) {
			temp = new Node(new StringBuilder(copy.info).reverse().toString());
			temp.next = last;
			last = temp;
			copy = copy.next;
		}
		LinkStrand revStrand = new LinkStrand();
		revStrand.myFirst = temp;
		revStrand.mySize = this.mySize;
		return revStrand;
	}

	@Override
	public int getAppendCount() {
		return this.myAppends;
	}

	@Override
	public char charAt(int index) {
		int count = myIndex;
		int dex = myLocalIndex;
		Node list = myCurrent;
		if (list==null) {
			list = myFirst;
		}
		if (count >= index) {
			count = 0;
			dex = 0;
			list = myFirst;
		}
			while (count != index) {
				count++;
				dex++;
				if (dex >= list.info.length()) {
					dex = 0;
					list = list.next;
				}
			}
			myIndex = count;
			myLocalIndex = dex;
			myCurrent = list;
			return list.info.charAt(dex);
	}

}
