package minions;


import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import thinkers.MinionEntity;

import ai.FeetHelper;
import ai.MoveToAI;
import attacks.GoblinAttack;

public class Goblin extends MinionEntity{

	public FeetHelper feet;
	public MoveToAI eyes;
	public GoblinAttack attack;
	private boolean attacking = false;

	public Goblin(Vec2 position){
		super(-1, position);
		System.out.println("catbits for gobs"+main.getFixtureList().getFilterData().categoryBits);
		feet = new FeetHelper(this,10);
		eyes = new MoveToAI(this, 80);
		attack = new GoblinAttack(this);
		
		
	}
	
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAI(int frame_num) {
		/*
		 * The Goblin has an "area of aknowledgement"
		 * 
		 * within that area, anything that is not an ally, it will go after
		 * 
		 * which usually means run at them
		 * And jump at them if they are above them
		 * 
		 * 
		 */
		eyes.time(frame_num);

		if(eyes.focus != null){
			float ang = (float)((eyes.angle+Math.PI*2)%(Math.PI*2));
			if(eyes.distance < 20 && !attacking){
				attacking = true;
				attack.toggleAttack((int)Math.round((Math.PI/2 + ang)*2/(Math.PI))%4, true);
			}else{
				attack.attacksOff();
				attacking = false;
			}
			
			
			if(Math.cos(ang) > 0) feet.walk(-20);
			if(Math.cos(ang) < 0) feet.walk(20);
			if(Math.sin(ang) < 0) feet.jump(30);
		}
		
	}

	int frameoffset;
	int fs = -1;
	public int clashAI(int frame_num) {
		// TODO Auto-generated method stub
		if(fs == -1 ||fs+frameoffset == frame_num){
			frameoffset = 10+Math.round((float)Math.random()*15);
			fs = frame_num;
			return 1;
		}else return 0;
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

	@Override
	protected Body setHitbox(Vec2 position) {
		// TODO Auto-generated method stub
		bodies.put("rest", Box2dHelper.createPlayerEntityRectangle(new Ownership(-1, this, this, "rest"), position.add(new Vec2(0,0f)), new Vec2(2,2), true));
		return 	bodies.get("rest");
	}

}
