package state;

import org.jbox2d.dynamics.FixtureDef;

public abstract class StateModule {
	
	protected StateEntity owner;
	protected int statetime = 0;
	public StateModule(StateEntity tobe){
		owner = tobe;
	}
	
	public abstract String[] getStateActions();
//	public abstract String[] takeOverFlags(); 
		//legs, arms. 
		//! means it takes it regardless
		//^ means it gives to anyone willing to take it. but exsists as concept
		//
	public abstract FixtureDef[] getFixtures(StateEntity Minion);
	
	public void begin(String statename){
		statetime = stateBegin(statename);
	}
	
	public boolean time(String statename){
		if(statetime == 0){
			stateEnd(statename);
			return false;
		}else{
			statetime--;
			doAction(statename,statetime);
			return true;
		}
	}
	
	public abstract int stateBegin(String statename);
	public abstract void doAction(String statename, int statetime);
	public abstract void stateEnd(String statename);
	
	public abstract boolean canApplyEffects(String statename);
	public abstract boolean canStore(String statename);
	
	public void setStateTime(int statetime){
		this.statetime = statetime;
	}

}
