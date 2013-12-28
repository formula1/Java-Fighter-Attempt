package revolute_attack;

import java.util.ArrayList;

import low_level_abstract.CollisionManager;
import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.collision.shapes.PolygonShape;
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
	float length;
	float girth;
	boolean active;
	
	
	public Hammer(MinionEntity minion){
		owner = minion;
		currentJoints = new ArrayList<Joint>();
		ham = getAttackStructure();
		length = ((PolygonShape)(ham.getFixtureList().m_shape)).getVertex(0).x - Math.min(((PolygonShape)(ham.getFixtureList().m_shape)).getVertex(0).x * .2f, 2f);
		girth = (((PolygonShape)(ham.getFixtureList().m_shape)).getVertex(0).y + ((PolygonShape)(ham.getFixtureList().m_shape)).getVertex(1).y);
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
		if(!owner.busy){
			Box2dHelper.world.destroyJoint(currentJoints.get(0));
			if(type =="swing"){
				ham.setTransform(new Vec2(length,girth), 0);
				switch(direction){
				case 0: break;
				case 1: break;
				case 2: break;
				case 3: break;
				default: throw new Error("I have no idea what happened");
				}
				owner.busy = true;
			}
		}
	}
	
	public void time(){
		
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
