package controller;

import game.BabySteps;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Player implements Runnable{
	
	public int playernumber;
	public static final String[] butnames = {	
										"up","down","left","right",
										"a", "b", "x", "y",
										"r1", "r2", "l1", "l2",
										"start", "select"
									};
	
	public HashMap<String,Float> buttons;
	protected boolean running;
	protected ArrayList<PlayerListener> listeners;
	protected boolean debug = false;

	public void setDebug(boolean d){
		debug = d;
	}

	public void addListener(PlayerListener g){
		listeners.add(g);
		listeners.get(listeners.size()-1).setInputs(playernumber, buttons);
	}
	public void removeListener(PlayerListener g){
		listeners.remove(g);
	}

	public Player(){
		listeners = new ArrayList<PlayerListener>();
		playernumber = 0;
		buttons = new HashMap<String,Float>();
		for(int i=0;i<butnames.length;i++)
			buttons.put(butnames[i], 0f);
 	}

	public Player(PlayerListener bs){
		listeners = new ArrayList<PlayerListener>();
		playernumber = 0;
		listeners.add(bs);
		buttons = new HashMap<String,Float>();
		for(int i=0;i<butnames.length;i++)
			buttons.put(butnames[i], 0f);
 	}

	public Player(PlayerListener bs, int pn){
		listeners = new ArrayList<PlayerListener>();
		playernumber = pn;
		listeners.add(bs);
		buttons = new HashMap<String,Float>();
		for(int i=0;i<butnames.length;i++)
			buttons.put(butnames[i], 0f);
 	}
	
	public Player(int pn){
		listeners = new ArrayList<PlayerListener>();
		playernumber = pn;
		buttons = new HashMap<String,Float>();
		for(int i=0;i<butnames.length;i++)
			buttons.put(butnames[i], 0f);
 	}

	
	public void run(){
		running=true;
		System.out.println("is runnning...");
		while(running){
			if(poll()){
				for(int i=0;i<listeners.size();i++) listeners.get(i).setInputs(playernumber, buttons);
			}
			try {
				Thread.sleep((int)(1000/60));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public abstract boolean poll();
		
	public void end(){
		running = false;
	}
	
}//End Player Class
