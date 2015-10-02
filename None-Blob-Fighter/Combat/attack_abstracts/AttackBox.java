package attack_abstracts;

import java.util.HashMap;

import low_level_abstract.CollisionManager;
import low_level_abstract.Logic_Entity;
import helpers.BodyUserData;
import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import pause.PauseInterface;

import thinkers.MinionEntity;

import clash.ClashPriorityEffect;

import effect_abstracts.Effect;
import effects.Damage;



/*
 * 
 * Create Combat Boxes
 * -Combat Boxes "don't exist" until a specific move is done
 * 		-Based off Contact filtering mostly
 * -On hit, apply forces, set effects
 * 		-I may want to create effect Listeners of some sort So that I can apply effects as a routine effect
 * 		-Also I may have to multithread the listeners simply because there may be alot of effects that take place
 * 		in terms of multiplication and such
 * 		-It would be chaining, where all the listsners for a specific chain would be in its own thread
 * 		-Checks PreSolve
 * 
 * 
 * 
 * 
 */
/*
 * Punch
 * 
 * need to create a Punch with filtermask (nonexsistant)
 * 
 * -On press, 
 * -set the Filtermask to player

 * Create a presolve condition
 * -If its button set velocities
 * -If it struck before, no worries
 * 
 * 
 * functions:
 * -create attack boxes (shape, offsets, on hit)
 * -turn attack box on (string)
 * -turn attack box off (string)
 * 
 * 
 */


public abstract class AttackBox implements CollisionManager, PauseInterface{
//	Body theHolder;
	Fixture[] fixtures;
	protected MinionEntity owner;
	HashMap<Body,Integer> b_2_s;
	int maxhits;

	
	public AttackBox(MinionEntity owner, int maxhits){
		this.owner = owner;
		Shape[] shapes = createShapes();
		this.maxhits = maxhits;
		b_2_s = new HashMap<Body,Integer>();

//		theHolder = Box2dHelper.world.createBody(bod);
		fixtures = new Fixture[shapes.length];
		System.out.println(shapes.length+" sdsdfs ");
		for(int i=0;i<shapes.length;i++){
			FixtureDef boxing = new FixtureDef();
			boxing.shape = shapes[i];
			boxing.friction = Box2dHelper.standard_friction;
			boxing.density =  Box2dHelper.standard_density;
			boxing.filter.categoryBits =0;
			boxing.filter.maskBits = 0;
			boxing.isSensor = true;
			boxing.userData = new Ownership(owner.pn,this, owner, "attack|"+getAttackLabel()+"-"+i, this);

			fixtures[i] = owner.main.createFixture(boxing);
		}
		

	}
	
	protected abstract String getAttackLabel();
	
	protected abstract Shape[] createShapes();
	
	@Override
	public void preSolve(Contact contact,
			boolean is_fix_a) {
	}


	@Override
	public void postSolve(Contact contact,
			boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void beginContact(Contact contact,
			boolean is_fix_a) {
		Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
		Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;

		if(contact.isTouching()){
			if(b_2_s.containsKey(n_bod.getBody()) && b_2_s.get(n_bod.getBody()) >= maxhits) return;
			int hit = (b_2_s.containsKey(n_bod.getBody()))?b_2_s.get(n_bod.getBody())+1:1;
			b_2_s.put(n_bod.getBody(), hit);
			
			
			Ownership fo = (Ownership)p_bod.getUserData();
			
			if(n_bod.getUserData() != null){
				BodyUserData other = (BodyUserData)n_bod.getBody().getUserData();
				hitPlayer(other.owner,contact, is_fix_a, (String)fo.stored_info);
			}else	hitObject(n_bod,contact, is_fix_a, (String)fo.stored_info);
		}
	}

	@Override
	public void endContact(Contact contact,
			boolean is_fix_a) {
		Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
		Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		// TODO Auto-generated method stub
		b_2_s.remove(n_bod.getBody());
	}

	
	
	public abstract ClashPriorityEffect getClashPriority();
	
	public void toggleAttack(int direction, boolean onoff){
		if(onoff) owner.effects.applyEffect(getClashPriority());
		Box2dHelper.setFilter(owner.pn, fixtures[direction].getFilterData(), onoff);
	}
	
	public void attacksOff(){
		for(int i=0;i<fixtures.length;i++)
		Box2dHelper.setFilter(owner.pn, fixtures[i].getFilterData(), false);
		
		b_2_s.clear();
	}
	
	public abstract void hitPlayer(MinionEntity owner, Contact contact, boolean is_fix_a, String fixname);
	public abstract void hitObject(Fixture other, Contact contact, boolean is_fix_a, String fixname);
		
}
