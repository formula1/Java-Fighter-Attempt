package abstracts;


import game.BS_to_Game;
import graphic.Camera;
import helpers.Box2dHelper;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public abstract class GameLevel {
/*
 * Each game level needs plaforms
 * -Platforms can have their own
 * Background-with parallax concept
 * Ring out radius - which effects camera control as well
 * 
 * And then time effects
 * 
 * 
 */
	
	protected BS_to_Game game;
	Vec2[] spawnpoints;
	Vec2[] cam_constraints;
	float ringout_radius;
	
	public GameLevel(BS_to_Game game, Vec2 ringoutconstraints){
		this.game = game;
		Vec2 vertex1 = new Vec2();
		Vec2 vertex2 = new Vec2();
		for(int i=0;i<4;i++){
			if(i%2 == 1){
				vertex2.x = vertex1.x = (2-i)*ringoutconstraints.x/2;
				vertex1.y = ringoutconstraints.y/2;
				vertex2.y = ringoutconstraints.y/-2;
			}else{
				vertex1.x = ringoutconstraints.x/2;
				vertex2.x = ringoutconstraints.x/-2;
				vertex2.y = vertex1.y = (1-i)*ringoutconstraints.y/2;
			}
			
			Box2dHelper.createRingoutBound(vertex1,vertex2);
		}
	}
	
	public abstract Body[] getBodies(World world);
	
	
	public abstract Camera cameraSetting(Camera origCamera);
		//find the mid poisition between all the players
		//get location of each player, set camera left/right and up/bottom bounds to the most extreme of each player
		//make sure those bounds are within the camera limits
		//if the bounds are the same, set minimum seen
		//
	
	/*
	 * create other objects, keep things simple
	 * 
	 */
	public abstract void time(int framenum);
	
}
