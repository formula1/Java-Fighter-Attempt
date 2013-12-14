package characters;

import game.BS_to_Game;
import helpers.Box2dHelper;
import helpers.Ownership;


import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import thinkers.MinionEntity;


import abstracts.Game;
import ai.FeetHelper;
import attack_abstracts.AttackBox;
import attacks.MyFirstAttack;

public class PlayerPushFeetEntity extends MinionEntity{
	AttackBox tilt;
	FeetHelper ground;
	RevoluteJoint turret;
	boolean fired = false;
	Body hammer;
	
	public PlayerPushFeetEntity(int player_number, Vec2 position){

		super(player_number, position);
		ground = new FeetHelper(this, 20);
		tilt = new MyFirstAttack(this);

		getMass();
		
		
		/*
		 * 
		 * If what both of the two bodies are "controlled"
		 * -it goes into clash
		 * 
		 * parameters for a player are
		 * 	-how much can we lift-
		 * 	-how fast do we lift things-
		 * 
		 * 	-foot power-how fast can we go
		 * 	-accelleration-how fast can we accellerate
		 * 	-stamina-how long can we do it for
		 * 
		 * 
		 * 
		 */
		
		
		hammer = Box2dHelper.createPlayerEntityRectangle(
				new Ownership(this.pn,this, this, "hammer"), position, new Vec2(5,1), false);
		
		RevoluteJointDef rjd = new RevoluteJointDef();
		

		
		rjd = new RevoluteJointDef();
		rjd.bodyA = main;
		rjd.localAnchorA = new Vec2(-1.5f, 0);
		rjd.bodyB = hammer;
		rjd.localAnchorB = new Vec2(-5f, 0);
		rjd.collideConnected = false;
		rjd.enableMotor = true;
		rjd.maxMotorTorque = hammer.getMass()*2*(float)Math.PI*360;
		rjd.motorSpeed = 0;
		turret = (RevoluteJoint)Game.world.createJoint(rjd);
		
	}
		
	
	

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
	public void die() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beginContact(Contact contact, 
			boolean is_fix_a) {

	}


	@Override
	public void endContact(Contact contact, 
			boolean is_fix_a) {
				
	}


	private boolean attacking = false;

	public void doAI(int framenum) {
		if(Game.players[pn].get("left") > 0.5f && Game.players[pn].get("right") < 0.5f ){
			ground.walk((Game.players[pn].get("y") > 0.5)?-100:-10);
		}else if(Game.players[pn].get("right") > 0.5f && Game.players[pn].get("left") < 0.5f ){
			ground.walk((Game.players[pn].get("y") > 0.5)?100:10);
		}else
			ground.walk(0);
		
		if(	Game.players[pn].get("a") > 0.5f){
			ground.jump(20);
		}

		/*
		if(Game.players[pn].get("x") > 0.5f && !attacking){
			if(Game.players[pn].get("up") > 0.5f){
				tilt.toggleAttack(0, true);
			}else if(Game.players[pn].get("left") > 0.5f){
				tilt.toggleAttack(1, true);
			}else if(Game.players[pn].get("down") > 0.5f){
				tilt.toggleAttack(2, true);
			}else if(Game.players[pn].get("right") > 0.5f){
				tilt.toggleAttack(3, true);
			}
			
			
			attacking = true;
		}else if(Game.players[pn].get("x") < 0.5f){
			tilt.attacksOff();
			attacking = false;
		}
*/
		if(			Game.players[pn].get("up") > 0.5f		&&		Game.players[pn].get("down") < 0.5f
		){
			turret.setMotorSpeed(hammer.getMass()*2*(float)Math.PI);
		}else if(	Game.players[pn].get("up") < 0.5f		&&		Game.players[pn].get("down") > 0.5f){
			turret.setMotorSpeed(-hammer.getMass()*2*(float)Math.PI);
		}else{
			turret.setMotorSpeed(0);
		}
	}



	boolean toggling = false;;
	
	public int clashAI(int frame_num) {
		// TODO Auto-generated method stub
		if(!toggling && Game.players[pn].get("x") > 0.5){
			toggling = true;
			return 1;
		}else if(toggling && Game.players[pn].get("x") < 0.5) toggling = false;
		return 0;
	}








	@Override
	public Body setHitbox(Vec2 position) {
		bodies.put("rest", Box2dHelper.createPlayerEntityRectangle(new Ownership(pn, this, this, "rest"), position.add(new Vec2(0,0f)), new Vec2(4,4), true));
		return bodies.get("rest");
	}
		
}//end playerEntity