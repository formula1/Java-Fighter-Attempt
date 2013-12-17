package revolute_attack;

import java.util.ArrayList;

import low_level_abstract.CollisionManager;
import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import thinkers.MinionEntity;

public class Hammer implements CollisionManager{

	Body ham;
	MinionEntity owner;
	ArrayList<Joint> currentJoints;
	
	public Hammer(MinionEntity minion){
		owner = minion;
		currentJoints = new ArrayList<Joint>();
		ham = getAttackStructure();
		deactivate();
	}
	
	public Body getAttackStructure(){
		return Box2dHelper.createPlayerEntityRectangle(
				new Ownership(owner.pn,this, owner, "hammer")
				, owner.getCenter(), new Vec2(5,1), false
		);
	}
	
	public float getInitialAngle(){
		return (float)(Math.PI/4);
	}
	
	public void deactivate(){
		Fixture f = ham.getFixtureList();
		Box2dHelper.setFilter(owner.pn, f.getFilterData(), false);
		ham.setTransform(owner.getCenter(), getInitialAngle());
		for(Joint j : currentJoints)	Box2dHelper.world.destroyJoint(j);
		currentJoints.clear();
		WeldJointDef weld = new WeldJointDef();
		weld.initialize(owner.main, ham, owner.getCenter());
		currentJoints.add(Box2dHelper.world.createJoint(weld));
	}

	public void activate(byte direction, String type){
		
	}

	@Override
	public void preSolve(Contact contact, boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginContact(Contact contact, boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact, boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}
	
	
}
