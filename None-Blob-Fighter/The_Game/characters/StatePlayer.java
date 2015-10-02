package characters;

import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import state.StateEntity;
import state.StateModule;
import states.BounceStunState;
import states.FeetModule;

public class StatePlayer extends StateEntity{

	int pn;
	public StatePlayer(int playernumber, Vec2 position) {
		super(position);
		this.pn = playernumber;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Body setMainBox(Vec2 initposition) {
		// TODO Auto-generated method stub
		return Box2dHelper.createPlayerEntityRectangle(new Ownership(pn, this, this, "rest"), initposition.add(new Vec2(0,0f)), new Vec2(4,4), true);
	}

	@Override
	public StateModule[] getStateModules() {
		// TODO Auto-generated method stub
		return new StateModule[]{new FeetModule(this), new BounceStunState(this)};
	}

	@Override
	public String getAIState() {
		// TODO Auto-generated method stub
		return null;
	}

}
