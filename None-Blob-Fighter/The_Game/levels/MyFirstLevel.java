package levels;

import java.util.ArrayList;
import java.util.Random;

import game.BS_to_Game;
import graphic.Camera;
import helpers.Box2dHelper;

import minions.Goblin;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import abstracts.GameLevel;
import assets.Vec2c;

public class MyFirstLevel extends GameLevel{

	
	
	public MyFirstLevel(BS_to_Game game) {
		super(game, new Vec2(500,500));
		// TODO Auto-generated constructor stub
	}

	@Override
	public Body[] getBodies(World world) {
		// TODO Auto-generated method stub
		Body[] ret = new Body[3];
		ret[0] = Box2dHelper.createStructureRectangle(new Vec2(0f,-10.0f), new Vec2(150.0f, 10.0f));
//		ret[1] = Box2dHelper.createStructureRectangle(new Vec2(-300f,50.0f), new Vec2(10.0f, 50.0f));
//		ret[2] = Box2dHelper.createStructureRectangle(new Vec2(300f,50.0f), new Vec2(10.0f, 50.0f));
		return ret;
	}

	@Override
	public Camera cameraSetting(Camera origCamera) {
		
		float buffer = 20;
		Vec2 level_bounds = new Vec2(500,500);
		float minimum_camera_bounds = 100;
		float maximum_camera_bounds = 500;
		

		
		Vec2 cur_pos = new Vec2();
		Vec2 minimums = null;
		Vec2 maximums = new Vec2();
		for(int i=0;i<game.pee.length;i++){
			cur_pos = game.pee[i].getCenter().mul(1);
			//This gets minimum and maximum position so we can scale the camera properly
			if(minimums == null){
				minimums = Vec2.max(cur_pos, level_bounds.mul(-1)).clone();
				maximums = Vec2.min( cur_pos,  level_bounds).clone();
			}else{
				if(minimums.y > cur_pos.y)	minimums.y = Math.max(cur_pos.y, level_bounds.y*-1);
				else if(maximums.y < cur_pos.y)maximums.y = Math.min(cur_pos.y, level_bounds.y);

				if(minimums.x > cur_pos.x) minimums.x = Math.max(cur_pos.x, level_bounds.x*-1);
				else if(maximums.x < cur_pos.x) maximums.x = Math.min(cur_pos.x, level_bounds.x);
			}
		}
		
		
		//get Midpoint between extremes
		Vec2 Midpoint = maximums.add(minimums);
		Midpoint = Midpoint.mul(1f/2f);
		
		
		//if the minimum and maximum position are the same, we need to use the minimum scaling
		//Otherwise we need to make sure its within the appropiate camera bounds
		
		Vec2 camera_bounds = new Vec2();

		if(game.pee.length == 0 || (camera_bounds.x = maximums.x-minimums.x) == 0){
			camera_bounds.x = minimum_camera_bounds;
		}else{
			camera_bounds.x = (float)Math.max(minimum_camera_bounds, Math.min(camera_bounds.x, maximum_camera_bounds));
		}

		if(game.pee.length == 0 || (camera_bounds.y = maximums.y-minimums.y) == 0){
			camera_bounds.y = minimum_camera_bounds;
		}else{
			camera_bounds.y = (float)Math.max(minimum_camera_bounds, Math.min(camera_bounds.y, maximum_camera_bounds));
		}
		
		camera_bounds.y = camera_bounds.x = Math.max(camera_bounds.x+buffer*2, camera_bounds.y+buffer*2);
		
				
		Camera cam = new Camera(new Vec2c(Midpoint), new Vec2c(camera_bounds), 0f);

		return cam;
	}

	
	int num_gobs =0;
	public void time(int framenum) {
		if(framenum%360 == 0){
			Body bullet = Box2dHelper.createEntityCircle(new Vec2(0,10), 2);
			bullet.applyForce(new Vec2(bullet.getMass()*5000,0),bullet.getWorldCenter());
			
		}
		if(framenum%720 == 1 && num_gobs++ < 10){
			Random rand = new Random();
			new Goblin(new Vec2(100-rand.nextInt(200), 30));
		}
	}

}
