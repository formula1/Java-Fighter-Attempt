package projectiles;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import thinkers.MinionEntity;

import helpers.Ownership;

public class Basic_Projectile extends thinkers.Game_Projectile{


	
	public Basic_Projectile(MinionEntity owner) {
		super(120, 1, 0);
		this.create(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FixtureDef[] getFixtures() {
		// TODO Auto-generated method stub
		CircleShape gBox = new CircleShape();
		gBox.setRadius(.5f);
		FixtureDef boxing = new FixtureDef();
		boxing.shape = gBox;
		boxing.friction = 0;
		boxing.density = 0;
		return new FixtureDef[] {boxing};
	}

	@Override
	public float getAngleVelocity(float player_vel) {
		return -player_vel;
	}

	@Override
	public Vec2 getLinearVelocity(Vec2 player_vel, float turretangle) {
		float orig_vel = 100f;
		Vec2 attempt = new Vec2((float)Math.cos(turretangle),(float)Math.sin(turretangle)).mul(orig_vel);
		
		return attempt.add(player_vel);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTime(int frame_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getPauseEffect(int pausetime) {
		// TODO Auto-generated method stub
		return Math.round(pausetime*2f);
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


}
