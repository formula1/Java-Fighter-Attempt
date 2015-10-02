package states;

import org.jbox2d.dynamics.FixtureDef;

import state.StateEntity;
import state.StateModule;

public class BounceStunState extends StateModule{
	int stuntime = 0;
	boolean isstunned=false;

	public BounceStunState(StateEntity tobe) {
		super(tobe);
		// TODO Auto-generated constructor stub
	}

	
	public String[] getStateActions() {
		// TODO Auto-generated method stub
		return new String[]{"stun"};
	}
	
	public FixtureDef[] getFixtures(StateEntity Minion) {
		// TODO Auto-generated method stub
		return null;
	}

	public int stateBegin(String statename) {
		isstunned = true;
		owner.main.setFixedRotation(false);
		return statetime;
	}

	public void doAction(String statename, int statetime) {
		// TODO Auto-generated method stub

	}
	public void stateEnd(String statename) {
		owner.main.setAngularVelocity(0);
		owner.main.setTransform(owner.main.getPosition(), 0f);
		owner.main.setFixedRotation(true);
		
	}
	

	public boolean canApplyEffects(String statename) {
		return true;
	}
	public boolean canStore(String statename) {
		return false;
	}


}
