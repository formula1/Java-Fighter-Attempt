package effect_abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class EffectManager{
	public HashMap<String, Effect> effects;
	HashMap<String,ArrayList<String>> listener;
	ArrayList<String> cleanup;
	
	
	public EffectManager(){
		effects = new HashMap<String,Effect>();
		listener = new HashMap<String,ArrayList<String>>();
		cleanup = new ArrayList<String>();
	}
	
	public void kill(Effect e){
		cleanup.add(e.tag);
	}
	
	public void time(int framenum){
		callEffects("time",framenum);
		for(String s : cleanup) effects.remove(s);
		for(Map.Entry<String, Effect> m : effects.entrySet()){
			if(m.getValue().frame ==0) effects.remove(m.getKey());
			if(m.getValue().frame >-1) m.getValue().frame--;
		}
	}
	
	public void applyEffect(Effect e){
		if(effects.containsKey(e.tag)) effects.get(e.tag).append(e);
		else{
			for(String s : e.getListeners()){
				if(!listener.containsKey(s)) listener.put(s, new ArrayList<String>());
				listener.get(s).add(e.tag);
			}
			effects.put(e.tag, e);
		}
	}
	
	public Object callEffects(String event, Object initialValue){
		ArrayList<String> l;
		if((l = listener.get(event)) == null) return initialValue;
		
		ArrayList<Effect> e = new ArrayList<Effect>();
		
		for(int i=0;i<l.size();i++)
			if(effects.containsKey(l.get(i))) e.add(effects.get(l.get(i)));
			else l.remove(i);
		
		Collections.sort(e,new Comparator<Effect>(){

			@Override
			public int compare(Effect o1, Effect o2) {
				// TODO Auto-generated method stub
				return o1.getPriority() - o2.getPriority();
			}
			
		});
		
		for(Effect ef : e) initialValue = ef.activate(event, initialValue);
		
		return initialValue;
	}
	
}

