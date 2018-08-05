
public class WordGram implements Comparable<WordGram>{
	
	private int myHash;
	private String[] myWords;
	
	public WordGram(String[] source, int index, int size) {
		myWords = new String[size];
		 for (int i=0; i<size; i++)
		 {
			 myWords[i] = source[index + i];
		 }
		 
		myHash = 17;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public String toString() {
		String ret = new String("");
		ret = String.join(" ", myWords);
		return ret;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || ! (other instanceof WordGram)) {
			return false;
		}
		WordGram wg = (WordGram) other;
		for (int k=0; k<this.myWords.length; k++)
		{
			if (!wg.myWords[k].equals(myWords[k]))
			{
				return false;
			}
			if ((this.hashCode())==(other.hashCode()))
			{
				return true;
			}
		}
		
		return true;
	}
	
	@Override
	public int compareTo(WordGram wg) {
		String test = this.toString();
		String test2 = wg.toString();
		return test.compareTo(test2);
	}
	
	public int length() {
		return myWords.length;
	}
	
	public WordGram shiftAdd(String last) {
		String[] edited = new String [this.myWords.length];
		for (int i=0; i<myWords.length-1; i++) 
		{
			edited[i]= this.myWords[i+1];
		}
		edited[myWords.length-1]= last;
		WordGram shifted = new WordGram(edited, 0, edited.length);
		return shifted;
	}
}
