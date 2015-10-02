package ai;

import helpers.Box2dHelper;
import helpers.Ownership;
import low_level_abstract.CollisionManager;
import low_level_abstract.Logic_Entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import thinkers.MinionEntity;


public class FeetHelper implements CollisionManager{
	
	private int floor = 0;
	private boolean air_attempt = false;
	private boolean in_air = true;
	
	
	MinionEntity Minion;
	float accelleration;
	

	public FeetHelper(MinionEntity Minion, float accelleration){
		this.Minion = Minion;
		float width = 0;
		float vertoff = 0;
		if(Minion.main.getFixtureList().getShape().getType() == ShapeType.CIRCLE){
			width = ((CircleShape)Minion.main.getFixtureList().getShape()).getRadius();
			vertoff = -1*width;
		}else{
			Vec2[] verts = ((PolygonShape)Minion.main.getFixtureList().getShape()).getVertices();
			float leftbound=0, rightbound=0, bot = 0;
			Vec2 center = new Vec2();
			for(Vec2 v : verts){
				center.add(v);
				if(v.y < bot) bot = v.y;
				if(v.x > rightbound) rightbound = v.x;
				if(v.x < leftbound) leftbound = v.y;
			}
			center.mul(1f/(float)verts.length);
			
			width = (rightbound - leftbound)/2;
			vertoff = bot;
		}
		
		
		FixtureDef feet = Box2dHelper.rectangle(new Vec2(width,.5f), new Vec2(0,vertoff));
		feet.isSensor = true;
		feet.userData = new Ownership(Minion.pn, this, Minion, "feet");
		Minion.main.createFixture(feet);
		
		this.accelleration = accelleration;

	}



	@Override
	public void preSolve(Contact contact,
			boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void beginContact(Contact contact,
			boolean is_fix_a) {
		Body p_bod = (is_fix_a)?contact.m_fixtureA.getBody():contact.m_fixtureB.getBody();
		Fixture n_fix = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;

		if(p_bod.getWorldCenter().y > contact.getManifold().points[0].localPoint.y && !n_fix.isSensor()){
			if(air_attempt){
				air_attempt = false;
			}
			floor++;
			in_air = false;
		}

		
	}



	@Override
	public void endContact(Contact contact,
			boolean is_fix_a) {
		Body p_bod = (is_fix_a)?contact.m_fixtureA.getBody():contact.m_fixtureB.getBody();
		Fixture p_fix = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
		Ownership o = (Ownership)p_fix.getUserData();
		Fixture n_fix = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		if(((String)o.stored_info).equals("feet")){
			if(p_bod.getWorldCenter().y > contact.getManifold().points[0].localPoint.y && !n_fix.isSensor() ){
				floor--;
				if(floor <= 0){
					in_air = true;
					air_attempt = false;
				}
			}
		}
		
	}



	@Override
	public void postSolve(Contact contact,
			boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void jump(float max){
		if(!in_air && !air_attempt)
			Minion.main.applyLinearImpulse(new Vec2(0,Minion.getMass()*max), Minion.main.getWorldCenter());
			air_attempt = true;

	}
	
	public void walk(float max){
		if(!in_air) Box2dHelper.accelHelp(Minion, max, accelleration);
	}	
}
