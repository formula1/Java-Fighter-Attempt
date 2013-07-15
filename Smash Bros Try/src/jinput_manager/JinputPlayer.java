package jinput_manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import controller.Player;
import controller.PlayerListener;


import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class JinputPlayer extends Player{
	HashMap<Component,String> bindings;
	Controller controller = null;
	EventQueue eq;

	public JinputPlayer(Controller controller, int pn){
		super(pn);
		bindings = new HashMap<Component,String>();
		this.controller = controller;
		this.debug = false;
	}
	
	public JinputPlayer(boolean debug, PlayerListener pl, int pn){
		super(pl, pn);
		bindings = new HashMap<Component,String>();
		this.debug = debug;
	}
	
	public JinputPlayer(boolean debug, PlayerListener pl, Controller controller, int pn){
		super(pl, pn);
		bindings = new HashMap<Component,String>();
		this.debug = debug;
		this.controller = controller;
	}
	
	
	
	
	
	public void setController(Controller newcontrol){
		bindings = new HashMap();
		controller = newcontrol;
	}
	
	public void bind(Component c,String s){
		unbind(s);
		bindings.put(c,s);
	}
	public void unbind(String s){
	    Iterator it = bindings.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<Component, String> pairs = (Map.Entry)it.next();
	    	if(s.equals(pairs.getValue())){
	    		bindings.remove(pairs.getKey());
	    		break;
	    	}
	    }
	}
	
	@Override
	public boolean poll(){
		Event ed = new Event();
		String temp2;
		boolean hasevent = false;
		if(controller != null){
			controller.poll();
			eq = controller.getEventQueue();
			while(eq.getNextEvent(ed)){
				Component temp = ed.getComponent();
				if((temp2 = bindings.get(temp)) != null){
					buttons.put(temp2, temp.getPollData());
					hasevent = true;
				}
				if(debug){
					for(int i=0;i<listeners.size();i++){
						listeners.get(i).push(playernumber, temp);
					}
				}
			}
		} else
			try {
				throw new Exception("controller doens't exsist");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hasevent;
	}
}//End Class
