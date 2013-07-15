package game;

import java.util.HashMap;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import controller.Player;
import abstracts.Game;
import abstracts.PhysicsManager;

public class BS_to_Game extends Game{
	
	Body[] playerBodies;
	
	
	
	public BS_to_Game(PhysicsManager w, Player[] players) {
		super(w, players);
		worldInit();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void worldInit() {
		// TODO Auto-generated method stub
		
		/*
		 * Each player is a boxer for the time being
		 * two rectangles for each arm
		 * A circle at the end for a fist
		 * A body
		 * A head
		 * Feet (which I'm thinking is just going to be a cericle of some sort
		 * 
		 * More importantly is how do I build each body?
		 * And how do I want combat to feel
		 * 
		 * 
		 * 
		 */
		
		
		world.setGravity(new Vec2(0.0f, -10.0f));
		createStructureRectangle(new Vec2(0f,-10.0f), new Vec2(50.0f, 10.0f));
		createEntityRectangle(new Vec2(5.0f,50.0f), new Vec2(1.0f, 1.0f));
		createEntityRectangle(new Vec2(4.0f,40.0f), new Vec2(1.0f, 1.0f));
		createEntityRectangle(new Vec2(6.0f,60.0f), new Vec2(1.0f, 1.0f));
		playerBodies = new Body[players.length];
		for(int i=0;i<players.length;i++){
			playerBodies[i] = createEntityCircle(new Vec2((float)(Math.random()*5)+5f,(float)(Math.random()*5)+5f), (float)(Math.random()*2.5f)+2.5f);
		}
	}
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInputs(int playernumber, HashMap<String, Float> inputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void push(int playernumber, Object o) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void playerEnter(int playernumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerLeave(int playernumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerAction(int playernumber, String button, float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void time(long time) {
		Vec2 vel;
		Vec2 forceapp;
		for(int i=0;i<playerBodies.length;i++){
			vel = playerBodies[i].getLinearVelocity();
			forceapp = new Vec2();
			float desvel;
			if(players[i].buttons.get("up") > 0.5f && players[i].buttons.get("down") < 0.5f){
				desvel = 5 - vel.y;
			}else if(players[i].buttons.get("up") < 0.5f && players[i].buttons.get("down") > 0.5f){
				desvel = -5 - vel.y;
			}else{
				desvel = 0;
			}
			forceapp.y = (float) (playerBodies[i].getMass() * desvel); //f = mv/t
			
			if(players[i].buttons.get("left") > 0.5f && players[i].buttons.get("right") < 0.5f){
				desvel = -5 - vel.x;
			}else if(players[i].buttons.get("right") > 0.5f && players[i].buttons.get("left") < 0.5f){
				desvel = 5 - vel.x;
			}else{
				desvel = 0;
			}
			forceapp.x = (float) (playerBodies[i].getMass() * desvel); //f = mv/t
			playerBodies[i].applyLinearImpulse( forceapp, playerBodies[i].getWorldCenter() );
		}
	}

}
