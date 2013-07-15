package abstracts;

import graphic.GameGraphic;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashMap;

import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import abstracts.GameRenderer.UIGraphic;

import controller.Player;
import controller.PlayerListener;

abstract public class Game implements ContactListener, PlayerListener{
/*
 * The game is the base for everything
 * The game isn't a thread but rather a communication engine
 * -If a player does something, then it will tell the game to do something
 * -If time is = x, then it will do something
 * -if a collision happens, then it will do something
 * 
 * the game communicates with three outputs
 * -The Physics Engine
 * -The Graphics
 * -The Sound
 * 
 * the game takes 3 inputs
 * -Players (technically this may be unlimited)
 * -Time change
 * -Collision detection
 * 
 * Again, a game doesn't have threads, but rather its ment to be a communication interface
 * That reads inputs and sets outputs
 * 
 */
	protected PhysicsManager world;
	protected HashMap<String,UIGraphic> theUI;
	protected Player[] players;
	protected float standard_friction = 0.2f;
	protected float standard_density = 1.0f;
	
	public Game(PhysicsManager w, Player[] players){
		this.world = w;
		this.players = players;
		theUI = new HashMap<String,UIGraphic>();
	}
	
//	Begin inputs
	public abstract void worldInit();
	
	public abstract void playerEnter(int playernumber);
	public abstract void playerLeave(int playernumber);
	public abstract void playerAction(int playernumber, String button, float amount);
	
	public abstract void time(long time);
	
	//implements Contact Listener

//end inputs
	
//begin outputs
	public boolean hasUI(){
		return (theUI.size() > 0);
	}
	public Collection<UIGraphic> getUI(){
		return theUI.values();
	}

	
//helpers
	public Body createStructureCircle(Vec2 position, float radius){
		BodyDef grounddef = new BodyDef();
		grounddef.position.set(position.x, position.y);
		Body groundbody = world.createBody(grounddef);
		CircleShape gBox = new CircleShape();
		gBox.setRadius(radius);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		groundbody.createFixture(boxing);
		
		return groundbody;
	}
	public Body createStructureRectangle(Vec2 position, Vec2 height_width){
		BodyDef grounddef = new BodyDef();
		grounddef.position.set(position.x, position.y);
		Body groundbody = world.createBody(grounddef);
		PolygonShape gBox = new PolygonShape();
		gBox.setAsBox(height_width.x, height_width.y);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		groundbody.createFixture(boxing);

		return groundbody;
	}
	
	public Body createEntityCircle(Vec2 position, float radius){
		BodyDef grounddef = new BodyDef();
		grounddef.type = BodyType.DYNAMIC;
		grounddef.position.set(position.x, position.y);
		Body groundbody = world.createBody(grounddef);
		CircleShape gBox = new CircleShape();
		gBox.setRadius(radius);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		groundbody.createFixture(boxing);
		
		return groundbody;
	}
	
	public Body createEntityRectangle(Vec2 position, Vec2 height_width){
		BodyDef grounddef = new BodyDef();
		grounddef.type = BodyType.DYNAMIC;
		grounddef.position.set(position.x, position.y);
		Body groundbody = world.createBody(grounddef);
		PolygonShape gBox = new PolygonShape();
		gBox.setAsBox(height_width.x, height_width.y);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		groundbody.createFixture(boxing);
		
		return groundbody;
	}
	
}//End Game Class
