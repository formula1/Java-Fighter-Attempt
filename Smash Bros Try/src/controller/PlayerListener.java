package controller;

import java.util.HashMap;

public interface PlayerListener {

	public void setInputs(int playernumber, HashMap<String,Float> inputs);
	
	public void push(int playernumber, Object o);
	
}
