package helpers;

import game.BS_to_Game;
import low_level_abstract.CollisionManager;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;

import state.StateEntity;
import thinkers.MinionEntity;


public class Box2dHelper {

	static public World world;
	public static float standard_friction;
	public static float standard_density;
	static int fc_Scene = 0x0001;
	static int fm_Scene = -1;
	static int fc_nuetral = 0x0002;
	static int fm_nuetral = -1;
	static int fc_bady = 0x0004;
	static int number_players;
	
	public Box2dHelper(World world,int number_players, float standard_density, float standard_friction){
		Box2dHelper.world = world;
		Box2dHelper.standard_density = standard_density;
		Box2dHelper.standard_friction = standard_friction;
		Box2dHelper.number_players = number_players;
	}
	
	

	
	//helpers
	public static Body createStructureCircle(Vec2 position, float radius){
		Body groundbody = world.createBody(setupStruc(position));

		FixtureDef temp = circle(radius);
		temp.filter.categoryBits = fc_Scene;
		temp.filter.maskBits = fm_Scene;
		groundbody.createFixture(temp);
		
		return groundbody;
	}
	public static Body createStructureRectangle(Vec2 position, Vec2 width_height){
		Body groundbody = world.createBody(setupStruc(position));
		
		FixtureDef temp= rectangle(width_height);
		temp.filter.categoryBits = fc_Scene;
		temp.filter.maskBits = fm_Scene;
		groundbody.createFixture(temp);
		return groundbody;

	}
	
	public static Body createRingoutBound(Vec2 vertex1, Vec2 vertex2){
		
		Body groundbody = world.createBody(setupStruc(vertex1.add(vertex2).mul(1/2)));
		
		CollisionManager c = new CollisionManager(){

			@Override
			public void preSolve(Contact contact,
					boolean is_fix_a) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beginContact(Contact contact,
					boolean is_fix_a) {
				Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
				Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
				if(!n_bod.isSensor())
					BS_to_Game.kill(n_bod.getBody());
			}

			@Override
			public void endContact(Contact contact,
					boolean is_fix_a) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact,
					boolean is_fix_a) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		FixtureDef temp= edge(vertex1,vertex2);
		temp.filter.categoryBits = fc_Scene;
		temp.filter.maskBits = fm_Scene;
		temp.isSensor = true;
		temp.userData = new Ownership(c);
		groundbody.createFixture(temp);
		return groundbody;

	}
	

	
	public static Body createEntityCircle(Vec2 position, float radius){
		Body groundbody = world.createBody(setupBody(position));

		FixtureDef boxing = circle(radius);

		boxing.filter.categoryBits = fc_nuetral;
		boxing.filter.maskBits = fm_nuetral;
		groundbody.createFixture(boxing);
		
		return groundbody;
	}
	
	public static Body createEntityRectangle(Vec2 position, Vec2 width_height){
		Body groundbody = world.createBody(setupBody(position));
		
		FixtureDef temp= rectangle(width_height);
		temp.filter.categoryBits = fc_nuetral;
		temp.filter.maskBits = fm_nuetral;
		groundbody.createFixture(temp);
		return groundbody;
	}
	
	
	
	public static Body createNullEntityRectangle(Vec2 position, Vec2 width_height, boolean fixed){
		BodyDef btemp = setupBody(position);
		btemp.fixedRotation = true;
		Body groundbody = world.createBody(btemp);
		FixtureDef temp= rectangle(width_height);
		temp.filter.categoryBits = 0x0000;
		temp.filter.maskBits = 0x0000;
		groundbody.createFixture(temp);
		return groundbody;
	}
	
	public static Body createFixedEntityRectangle(Vec2 position, Vec2 width_height){
		BodyDef btemp = setupBody(position);
		btemp.fixedRotation = true;
		Body groundbody = world.createBody(btemp);
		
		FixtureDef temp= rectangle(width_height);
		temp.filter.categoryBits = fc_Scene;
		temp.filter.maskBits = fm_Scene;
		groundbody.createFixture(temp);
		return groundbody;
	}
	
	public static Body createPlayerEntityCircle(Ownership play_info, Vec2 position, float radius, boolean fixed){
		BodyDef btemp = setupBody(position);
		btemp.fixedRotation = fixed;
		Body groundbody = world.createBody(btemp);
		
		FixtureDef temp= circle(radius);
		temp.filter.categoryBits =(play_info.player_number == -1)?fc_bady:(int)Math.pow(2, play_info.player_number+4);
		temp.filter.maskBits = getMaskIndex(play_info.player_number);
		temp.userData = play_info;
		groundbody.createFixture(temp);
		return groundbody;
		
	}

	
	
	public static Body createPlayerEntityRectangle(Ownership play_info, Vec2 position, Vec2 width_height, boolean fixed){
		BodyDef btemp = setupBody(position);
		btemp.fixedRotation = fixed;
		Body groundbody = world.createBody(btemp);
		
		FixtureDef temp= rectangle(width_height);
		temp.filter.categoryBits =(play_info.player_number == -1)?fc_bady:(int)Math.pow(2, play_info.player_number+4);
		temp.filter.maskBits = getMaskIndex(play_info.player_number);
		temp.userData = play_info;
		
		groundbody.createFixture(temp);
		return groundbody;
		
	}

	
	
	
//		public static Fixture createAttackBox(Ownership play_info, Vec2 offset, )

	
	public static void attemptToRetainAngle(Joint joint, float Angle){
		
	}
	
	
	public static int getMaskIndex(int playernumber){
		int ret = fc_Scene | fc_nuetral ;
		if(playernumber != -1) ret |= fc_bady;
		for(int i=0;i<number_players;i++){
			if(playernumber == i)continue;
			ret = ret | (int)Math.pow(2,i+4);
		}
		System.out.println("playernumber:"+playernumber+"|ret:"+ret);
		return ret;
	}
	
	public static int getAttackMaskIndex(int playernumber){
		int ret = (playernumber != -1)?fc_bady|fc_nuetral:fc_nuetral;
		for(int i=0;i<number_players;i++){
			if(playernumber == i)continue;
			ret = ret | (int)Math.pow(2,i+4);
		}
		return ret;
	}

	
	private static BodyDef setupBody(Vec2 position){
		BodyDef grounddef = new BodyDef();
		grounddef.type = BodyType.DYNAMIC;
		grounddef.angle = 0;
		grounddef.position.set(position.x, position.y);
		grounddef.userData = new BodyUserData(null);
		return grounddef;
	}

	private static BodyDef setupStruc(Vec2 position){
		BodyDef grounddef = new BodyDef();
		grounddef.type = BodyType.STATIC;
		grounddef.angle = 0;
		grounddef.position.set(position.x, position.y);
		grounddef.userData = new BodyUserData(null);
		return grounddef;
	}

	
	public static FixtureDef circle(float radius){
		CircleShape gBox = new CircleShape();
		gBox.setRadius(radius);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		return boxing;
	}
	
	public static FixtureDef rectangle(Vec2 width_height){
		PolygonShape gBox = new PolygonShape();
		gBox.setAsBox(width_height.x, width_height.y);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		return boxing;
		
	}

	public static FixtureDef edge(Vec2 vertex1,Vec2 vertex2){
		EdgeShape e = new EdgeShape();
		e.set(vertex1, vertex2);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = e;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		return boxing;
		
	}

	
	public static FixtureDef rectangle(Vec2 width_height, Vec2 offset){
		PolygonShape gBox = new PolygonShape();
		gBox.setAsBox(width_height.x, width_height.y, offset, 0);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		return boxing;
		
	}

	
	public static FixtureDef createPlayersAttackBox(Ownership play_info, Vec2 width_height, Vec2 offset){
		PolygonShape gBox = new PolygonShape();
		gBox.setAsBox(width_height.x, width_height.y, offset, 0f);
		FixtureDef boxing = new FixtureDef();
		boxing.density = 0;
		boxing.shape = gBox;
		boxing.friction = standard_friction;
		boxing.density = standard_density;
		boxing.filter.categoryBits =0;
		boxing.filter.maskBits = 0;
		boxing.userData = play_info;

		return boxing;
		
	}

	
	public final static float TWELVTH = ((float)Math.PI)/12f;
	public final static float EIGHTH = ((float)Math.PI)/8;
	public final static float QUART = ((float)Math.PI)/4;
	
	
	public static Filter setFilter(int player_num, Filter filt, boolean onoff){
		if(onoff){
			filt.categoryBits = (player_num == -1)?fc_bady:(int)Math.pow(2, player_num+4);
			filt.maskBits = getAttackMaskIndex(player_num);
		}else{
			filt.categoryBits = 0;
			filt.maskBits = 0;
		}
		return filt;
	}
	
	
	public static void velocityHelp(Body b, byte dir){
	 Vec2 vel = b.getLinearVelocity();
	    float desiredVel = 0;
	    switch ( dir )
	    {
	      case -1:  desiredVel = -10; break;
	      case 0:  desiredVel =  0; break;
	      case 1: desiredVel =  10; break;
	    }
	    float velChange = desiredVel - vel.x;
	    float impulse = b.getMass() * velChange; //disregard time factor
	    b.applyLinearImpulse(new Vec2(impulse,0), b.getWorldCenter() );
	}

	public static void velocityHelp(MinionEntity p, int desired_velocity){
		 Vec2 vel = p.main.getLinearVelocity();
	    float desiredVel = 0;
	    desiredVel = desired_velocity;
	    float velChange = desiredVel - vel.x;
	    float impulse = p.getMass() * velChange; //disregard time factor
	    p.main.applyLinearImpulse(new Vec2(impulse,0), p.main.getWorldCenter() );
	}

	//maybe create a logrythimic version that increases only a bit
	
	public static void accelHelp(MinionEntity p, float desired_vel, float increment){
		Vec2 vel = p.main.getLinearVelocity();
		if(desired_vel == vel.x) return;
		float velchange = 0;
		if(vel.x < desired_vel){
			velchange = Math.min(vel.x + increment, desired_vel);
		}else{
			velchange = Math.max(vel.x - increment, desired_vel);
		}
		float velChange = velchange - vel.x;
		float impulse = p.getMass() * velChange; //disregard time factor
		p.main.applyLinearImpulse(new Vec2(impulse,0), p.main.getWorldCenter() );
	}



	public static void accelHelp(Body bod, float desired_vel, float increment) {
		Vec2 vel = bod.getLinearVelocity();
		if(desired_vel == vel.x) return;
		float velchange = 0;
		if(vel.x < desired_vel){
			velchange = Math.min(vel.x + increment, desired_vel);
		}else{
			velchange = Math.max(vel.x - increment, desired_vel);
		}
		float velChange = velchange - vel.x;
		float impulse = bod.getMass() * velChange; //disregard time factor
		bod.applyLinearImpulse(new Vec2(impulse,0), bod.getWorldCenter() );
	}

	

		
}
