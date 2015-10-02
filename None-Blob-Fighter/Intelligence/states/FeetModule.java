package states;

import low_level_abstract.CollisionManager;
import helpers.Box2dHelper;
import helpers.Ownership;
import helpers.StateFixData;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import state.StateEntity;
import state.StateModule;

public class FeetModule extends StateModule {

	boolean air_attempt=false;
	int floor = 0;
	boolean in_air = true;
	
	public FeetModule(StateEntity tobe) {
		super(tobe);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getStateActions() {
		// TODO Auto-generated method stub
		return new String[]{
				"walk:l","walk:r"
				,"air:l","air:r"
				,"jump", "fastfall"
				};
	}

	@Override
	public FixtureDef[] getFixtures(StateEntity Minion) {
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
		feet.userData = new StateFixData(new feetCM(), "feet");
		
		return new FixtureDef[]{feet};
	}

	@Override
	public int stateBegin(String statename) {
		// TODO Auto-generated method stub
		float max = 40;
		float accell = 10;
		
		if(statename == "jump"){
			if(!in_air && !air_attempt)
				owner.main.applyLinearImpulse(new Vec2(0,owner.main.getMass()*max), owner.main.getWorldCenter());
				air_attempt = true;
		}else if(statename=="fastfall"){
			if(!in_air)
				owner.main.applyLinearImpulse(new Vec2(0,-owner.main.getMass()*10), owner.main.getWorldCenter());
		}else if(statename=="walk"){
			if(!in_air) Box2dHelper.accelHelp(owner.main, max, accell);
		}
		
		
		return 10;
	}

	@Override
	public void doAction(String statename, int statetime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateEnd(String statename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canApplyEffects(String statename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStore(String statename) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	
	private class feetCM implements CollisionManager{

		@Override
		public void preSolve(Contact contact, boolean is_fix_a) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beginContact(Contact contact, boolean is_fix_a) {
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
		public void endContact(Contact contact, boolean is_fix_a) {
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
		public void postSolve(Contact contact, boolean is_fix_a) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
