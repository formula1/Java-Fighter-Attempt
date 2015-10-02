package state;

import helpers.BodyUserData;
import helpers.Ownership;

import java.util.HashMap;

import low_level_abstract.FrameListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import clash.ClashManager;

import effect_abstracts.EffectManager;

public abstract class StateEntity implements FrameListener{

	/*
	 * Simply put this is a minion entity thats based off states
	 * 
	 * What I do is I apply Modules to the state entity
	 * 	-each module has a get fixture list
	 * 	-each module has a "statelist"
	 * 		-what happens is on "state", call the module.doState("state")
	 * 		--this returns an int, and prevent any action change until int is = 0
	 * 
	 * Entity Controllers
	 * 	-Facing-left, right
	 * 	-Movement
	 * 		-On ground
	 * 		-in air
	 * 		
	 * 
	 * 
	 * 
	 * 
	 */
	int statetime;
	EffectManager effects;
	String current_state;
	String stored_state;
	boolean facing_left;
	HashMap<String,StateModule> state_actions;
	public Body main;
	
	public StateEntity(Vec2 position){
		establishCenter(setMainBox(position));
		StateModule[] sms = getStateModules();
		state_actions = new HashMap<String,StateModule>();
		
		for(StateModule sm : sms){
			String[] states = sm.getStateActions();
			for(String s : states){
				state_actions.put(s, sm);
			}
			FixtureDef[] fixes = sm.getFixtures(this);
			if(fixes != null)
			for(FixtureDef fix : fixes){
				main.createFixture(fix);
			}
		}
	}
	
	public abstract Body setMainBox(Vec2 initposition);
	public abstract StateModule[] getStateModules();
	public abstract String getAIState();
	
	public void time(int frame_num){
		/*
		 * Need to also be able to cancel states into states
		 * like running attack, air attack
		 * 
		 * How do I work this?
		 * -I want to create tags, where only certian actions can be done so long as those
		 * tags have not been taken.
		 * 		-if * is taken, no other action can be done
		 * 
		 * I'd also like to make sure I'm canceling things properly
		 * 
		 */
		

		if(current_state == null){
				if(stored_state != null){
					setState(stored_state);
					stored_state = null;
				}else setState(getAIState());
		}else{
			if(state_actions.get(current_state).canStore(current_state))
				stored_state = getAIState();
			state_actions.get(current_state).time(current_state);
		}

		if(
			current_state != null
		||	state_actions.get(current_state).canApplyEffects(current_state)
		) 	effects.time(frame_num);
	}
	
	public void setState(String statename){
		current_state = statename;
		if(statename != null)
			state_actions.get(statename).stateBegin(statename);
	}

	public void setState(String statename, int statetime){
		this.statetime = statetime;
		state_actions.get(statename).setStateTime(statetime);
		setState(statename);
	}

	
	public boolean hasState(String statename){
		return state_actions.containsKey(statename);
	}
	
	public void storeState(String statename){
		stored_state = statename;
	}
	
	private void establishCenter(Body bod){
		main = bod;
		Fixture f = main.getFixtureList();
/*		while(f.getNext() != null){
			f.setUserData(new Ownership(pn,this,this));
		}
*/
//		main.setUserData(new BodyUserData(this));
	}
	
	public boolean isPaused(){
		return (((BodyUserData)main.getUserData()).paused != null);
	}
	
	private void stateRegister(){
		String[] states = current_state.split("|");
		
	}
	
}
