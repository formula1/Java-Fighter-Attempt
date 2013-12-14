package thinkers;

import java.util.HashMap;
import java.util.Map.Entry;

import low_level_abstract.Logic_Entity;

import effect_abstracts.EffectManager;
import game.BS_to_Game;
import helpers.BodyUserData;
import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import clash.ClashManager;

import pause.Impact;



public abstract class MinionEntity implements Logic_Entity {

	/*
	 * Perhaps Minion Entity is getting to be too much...
	 * 
	 * Things that are very important
	 * -Impacts-Mostly combat related however can be anything
	 * 	-impacts enable combos, create "board pressence"
	 * -Clashing
	 * -Attacks
	 * 
	 * Design Space
	 * -Being able to add listeners to anything
	 * 
	 * 
	 * 
	 */
	
	
	public int pn;
	
	public float mass;
	public HashMap<String, Body> bodies;
	
	public EffectManager effects;
	public ClashManager clash;
	
	private int stun = 0;
	private boolean isstunned = false;
	public Body main;
	
	public MinionEntity(int pn, Vec2 position){
		this.pn = pn;
		clash = new ClashManager(this, 1f, 500);
		effects = new EffectManager();
		bodies = new HashMap<String,Body>();
		BS_to_Game.le.add(this);
		establishCenter(setHitbox(position));
	}
	

		
	protected abstract Body setHitbox(Vec2 Position);
	
	private void establishCenter(Body bod){
		main = bod;
		Fixture f = main.getFixtureList();
		while(f.getNext() != null){
			f.setUserData(new Ownership(pn,this,this));
		}
		
		main.setUserData(new BodyUserData(this));
	}
	
	

		
	
	public void setStun(int frames){
		if(isPaused()){
			if(clash.clashes.size() > 0){
				clash.loseAll();
			}
		}
		stun = (Integer)effects.callEffects("stun", frames);
		
	}
	
	public void setVelocity(Vec2 velocity, Vec2 point){
		velocity = (Vec2)effects.callEffects("recieveimpulse", velocity);
		Vec2 negativeforce = main.getLinearVelocity().mul(main.getMass()*-1);
		if(point == null)point = main.getWorldCenter();
		if(isPaused()){
			((BodyUserData)main.getUserData()).paused.appendImpact(main, new Impact(negativeforce.add(velocity),point));
		}else main.applyLinearImpulse(negativeforce.add(velocity), point);
	}
	
	
	public void time(int frame_num) {
		// TODO Auto-generated method stub
		System.out.println("ispaused?"+isPaused()+", isstunned?"+(stun > 0));
		if(isPaused()){
			if(clash.clashes.size() > 0){
				
				clash.action += clashAI(frame_num);
			}
			return;
		}
		effects.time(frame_num);
		if(stun > 0){
			if(!isstunned){
				isstunned = true;
				main.setFixedRotation(false);
			}
			stun--;
			return;
		}else if(isstunned){
			isstunned = false;
			main.setAngularVelocity(0);
			main.setTransform(main.getPosition(), 0f);
			main.setFixedRotation(true);
		}
		doAI(frame_num);
	}
	
	
	public abstract void doAI(int frame_num);
	
	public abstract int clashAI(int frame_num);

	
	public float getMass(){
		float total = 0;
		for(Entry<String,Body> e : bodies.entrySet()){
			total += e.getValue().getMass();
		}
		mass = total;
		return total;
	}

	public Vec2 getCenter(){
		return bodies.get("rest").getWorldCenter();
	}

	public boolean isPaused(){
		return (((BodyUserData)main.getUserData()).paused != null);
	}
	

}
