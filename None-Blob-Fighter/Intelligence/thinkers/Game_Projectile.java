package thinkers;

import low_level_abstract.Logic_Entity;
import game.BS_to_Game;
import helpers.Box2dHelper;
import helpers.Ownership;


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import pause.PauseInterface;

import abstracts.Game;


public abstract class Game_Projectile implements Logic_Entity, PauseInterface{

	/*
	 * With each game projectile there is, init position, init angle, initial velocity (which is the players velocity)
	 * and then obviously the projectile itself
	 * 
	 * 
	 * What I could do is make this abstract class more of a management class
	 * Where each projectile isn't considered a seperate instance of a game projectile but rather gets
	 * reads from this book to see what it does but by nature is just a body
	 * 
	 * the issue with that is instance variables
	 * -Each instance will have different values for their respective variables that may be needed to keep track
	 * of artificial intelligence and such
	 * -thus it doesn't make too much sense
	 * 
	 * 
	 * 	 * Interesting projectiles
	 * 	-Boomerang
	 * 	-ball and chain -
	 * 	-Homing - very important
	 * 
	 * 
	 */
	
	public float damage_on_impact;
	private int starting_frame;
	public float gravityscale;
	public int max_time;
	public int max_impacts;
	public Body projectile_body;
	
	public Game_Projectile(int max_time, int max_impacts, float gravityscale){
		this.max_time = max_time;
		this.max_impacts = max_impacts;
		BS_to_Game.le.add(this);
	}
	
	
	protected void create(MinionEntity owner){
		
		Body r = owner.bodies.get("turret");
		BodyDef grounddef = new BodyDef();
		grounddef.gravityScale = gravityscale;
		grounddef.type = BodyType.DYNAMIC;
		grounddef.angle = r.getAngle();
		grounddef.position.set(r.getWorldCenter().x, r.getWorldCenter().y);
		grounddef.bullet = true;

		grounddef.angularVelocity = getAngleVelocity(r.m_angularVelocity);
		grounddef.linearVelocity = getLinearVelocity(r.m_linearVelocity, r.getAngle());
		grounddef.userData = new Ownership(owner.pn,this,owner,"basic_projectile");
		projectile_body = BS_to_Game.world.createBody(grounddef);

		FixtureDef[] fs = getFixtures();
		for(FixtureDef f : fs){
			f.userData = new Ownership(owner.pn,this,owner,"basic_projectile");
			f.filter.categoryBits = (int)Math.pow(2, (Integer)owner.pn+3);
			f.filter.maskBits = Box2dHelper.getMaskIndex(owner.pn);
			projectile_body.createFixture(f);
		}

		onCreate();
		starting_frame = BS_to_Game.current_frame;
	}

	public abstract FixtureDef[] getFixtures();
	public abstract float getAngleVelocity(float player_vel);
	public abstract Vec2 getLinearVelocity(Vec2 player_vel, float turret_angle);
	public abstract void onCreate();

	public void time(int frame_num){
		if(starting_frame > 0 && frame_num-starting_frame >= max_time){
			die();
			return;
		}
		onTime(frame_num);
	}

	public abstract void onTime(int frame_num);
	
//	public abstract void onImpact(Contact contact, Entity other);
	
	public void die(){
		BS_to_Game.kill(projectile_body);
		onDeath();
	}
	public abstract void onDeath();
	
	public String getType(){
		return "destructible";
	}
}
