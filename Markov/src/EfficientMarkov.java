import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;

	public class EfficientMarkov extends MarkovModel
	{
	private HashMap<String,ArrayList<String>> myMap;
    public EfficientMarkov(int order) {
    super(order);
    myMap = new HashMap<String,ArrayList<String>>();
    }

    @Override
    public void setTraining(String text)
    { 
    	super.setTraining(text);
    	myMap.clear();
    	for (int i=0; i<myText.length() - myOrder; i+=1)
    	{
    		if (i + myOrder == text.length()) {
				String next = PSEUDO_EOS;
				String follow = getFollows(i);
				if (myMap.containsKey(follow)) {
					myMap.get(follow).add(next);
				} else {
					myMap.put(follow, new ArrayList<String>());
					myMap.get(follow).add(next);
				}
				break;
			}
    		String key = myText.substring(i, i + myOrder);
    		String follow = getFollows(i);
    		if (myMap.containsKey(key)) 
    		{
    			myMap.get(key).add(follow);
    		}
    		else 	
    		{
    			ArrayList<String> willAdd = new ArrayList<>();
    			willAdd.add(follow);
    			myMap.put(key, willAdd);
    		}
    	}
    	
    }
    
    @Override
    public ArrayList<String> getFollows(String key)
    {
    	return myMap.get(key);
    }
    
    public String getFollows(int index)
    {
    	return myText.substring(index + myOrder, index + myOrder +1);
    }
    
}
