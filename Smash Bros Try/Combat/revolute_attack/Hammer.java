package revolute_attack;

import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Hammer {

	Body ham;
	Ownership owner;
	
	public Hammer(Ownership owner, Body bod){
		
		this.owner = owner;
		ham = Box2dHelper.createPlayerEntityRectangle(owner, bod.getWorldCenter(), new Vec2(30,30), false);
		
		WeldJointDef
		RevoluteJointDef rj = new RevoluteJointDef();
		rj.bodyA = bod;
		
	}
	
	public void deactivate(){
		Fixture f = ham.getFixtureList();
		Box2dHelper.setFilter(owner.player_number, f.getFilterData(), false);
		
	}
	
	public void activate(byte direction){
		
	}
	
	
}
