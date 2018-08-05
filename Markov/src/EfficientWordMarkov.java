import java.util.ArrayList;
import java.util.HashMap;

public class EfficientWordMarkov extends WordMarkovModel {
	private HashMap<WordGram, ArrayList<String>> myMap;

	public EfficientWordMarkov(int use) {
		super(use);
		myMap = new HashMap<WordGram, ArrayList<String>>();
	}

	@Override
	public void setTraining(String text) {
		myWords = text.split("\\s+");
		myMap.clear();
		for (int i = 0; i < myWords.length; i++) {
			// String[] myString = new String[myOrder];

			/*
			 * for (int k=0;k<myOrder; k++) { myString[k] = myWords[i+k]; }
			 */
			if (i + myOrder == myWords.length) {
				String next = PSEUDO_EOS;
				WordGram wg = new WordGram(myWords, i, myOrder);
				if (myMap.containsKey(wg)) {
					myMap.get(wg).add(next);
				} else {
					myMap.put(wg, new ArrayList<String>());
					myMap.get(wg).add(next);
				}
				break;
			}
			String next = myWords[myOrder + i];
			WordGram wg = new WordGram(myWords, i, myOrder);

			if (myMap.containsKey(wg)) {
				myMap.get(wg).add(next);
			} else {
				myMap.put(wg, new ArrayList<String>());
				myMap.get(wg).add(next);
			}
		}
	}

	@Override
	public ArrayList<String> getFollows(WordGram wg) {
		if (myMap.containsKey(wg)) {
			return myMap.get(wg);
		} else {
			return new ArrayList<String>();
		}
	}
}
