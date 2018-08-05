/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 * @author Jeff Forbes
 */
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;
	double trieMax = 0;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws NullPointerException
	 *             if either argument is null
	 * @throws IllegalArgumentException
	 *             if terms and weights are different weight
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
		
		for(double i : weights) {
			if (i<0)
				throw new IllegalArgumentException("negative weight " + i);
		}
		
		if(terms.length!=weights.length) {
			throw new IllegalArgumentException("Length of parameters not equal");
		}
		
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 */
	private void add(String word, double weight) {
		// TODO: Implement add
		if (word == null) 
			throw new NullPointerException("null word given");
		
		if (weight < 0) 
			throw new IllegalArgumentException("negative weight");
		
		Node current = myRoot;
		for(int i=0; i < word.length(); i++){
			if(current.mySubtreeMaxWeight < weight)
				current.mySubtreeMaxWeight = weight;
			
			if(current.children.containsKey(word.charAt(i)))
				current = current.getChild(word.charAt(i));
			
			else {
				current.children.put(word.charAt(i), new Node(word.charAt(i), current, weight));
				current = current.getChild(word.charAt(i));
			}
		}
		current.myWeight = Math.max(weight, current.myWeight);
		current.myWord= word;
		current.isWord= true;
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An Iterable of the k words with the largest weights among all
	 *         words starting with prefix, in descending weight order. If less
	 *         than k such words exist, return all those words. If no such words
	 *         exist, return an empty Iterable
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		if (prefix == null)
			throw new NullPointerException("null prefix given");
		if(k<=0) 
			return new LinkedList<String>();
		Node current = myRoot;
		PriorityQueue<Node> npq = new PriorityQueue<>(new Node.ReverseSubtreeMaxWeightComparator());
		PriorityQueue<Term> tpq = new PriorityQueue<>(k,new Term.WeightOrder());
		
		for(char ch : prefix.toCharArray()) {
			current = current.getChild(ch);
			if(current==null) 
				return new LinkedList<>();
		} 
		npq.add(current);
		while (!npq.isEmpty()) {
			if(tpq.size() >= k) {
				if(tpq.peek().getWeight() > npq.peek().getWeight())
					break;
			}
			current = npq.remove();
			if (current.isWord) 
				tpq.add(new Term(current.getWord(), current.getWeight()));
			if (tpq.size() > k) 
				tpq.remove();
			for(Node temp : current.children.values()) {
				npq.add(temp);
			}
		}

		LinkedList<String> retList = new LinkedList<String>();
		while(tpq.size() > 0) {
			String removed = tpq.remove().getWord();
			retList.addFirst(removed);
		}
		return retList;
	}

	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from with the largest weight starting with prefix, or an
	 *         empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		Node current = myRoot;
		
		if(prefix == null) 
			throw new NullPointerException("String is null");
		
		if(current == null) 
			return "";
		
		for(int i = 0; i < prefix.length(); i++) {
			if(!current.children.containsKey(prefix.charAt(i))){ 
				return "";
			}
			current = current.children.get(prefix.charAt(i));
		}
		
		if(current.children.isEmpty()){
			return prefix;
		}
		
		double maxWeight = current.mySubtreeMaxWeight;
		
		while(current.myWeight != maxWeight) {
			for(Node temp: current.children.values()) {
				if(temp.mySubtreeMaxWeight == maxWeight) {
					current = temp;
					break;
				}
			}
		}
		return current.getWord();
		
	}

	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		// TODO complete weightOf
		Node current = myRoot;
		for (int i = 0; i<term.length();i++){
			if (current.children.containsKey(term.charAt(i))){
				current = current.getChild(term.charAt(i));
			}
			else{
				return 0;
			}
		}
		
		return current.getWeight();
	}
}
