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

import revolute_attack.Hammer;
import thinkers.MinionEntity;


import abstracts.Game;
import ai.FeetHelper;
import attack_abstracts.AttackBox;
import attacks.MyFirstAttack;

public class PlayerPushFeetEntity extends MinionEntity{
	AttackBox tilt;
	FeetHelper ground;
	boolean fired = false;
	Hammer ham;
	
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
		
		ham = new Hammer(this);
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
		if(Game.players[pn].buttons.get("left") > 0.5f && Game.players[pn].buttons.get("right") < 0.5f ){
			ground.walk((Game.players[pn].buttons.get("y") > 0.5)?-100:-10);
		}else if(Game.players[pn].buttons.get("right") > 0.5f && Game.players[pn].buttons.get("left") < 0.5f ){
			ground.walk((Game.players[pn].buttons.get("y") > 0.5)?100:10);
		}else
			ground.walk(0);
		
		if(	Game.players[pn].buttons.get("a") > 0.5f){
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
		if(			Game.players[pn].buttons.get("up") > 0.5f		&&		Game.players[pn].buttons.get("down") < 0.5f
		){
			ham.activate((byte) 1, "swing");
		}else if(	Game.players[pn].buttons.get("up") < 0.5f		&&		Game.players[pn].buttons.get("down") > 0.5f){
			ham.activate((byte) 3, "swing");
		}else{
			ham.deactivate();
		}
	}



	boolean toggling = false;;
	
	public int clashAI(int frame_num) {
		// TODO Auto-generated method stub
		if(!toggling && Game.players[pn].buttons.get("x") > 0.5){
			toggling = true;
			return 1;
		}else if(toggling && Game.players[pn].buttons.get("x") < 0.5) toggling = false;
		return 0;
	}








	@Override
	public Body setHitbox(Vec2 position) {
		bodies.put("rest", Box2dHelper.createPlayerEntityRectangle(new Ownership(pn, this, this, "rest"), position.add(new Vec2(0,0f)), new Vec2(4,4), true));
		return bodies.get("rest");
	}
		
}//end playerEntity