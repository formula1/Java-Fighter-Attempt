package ai;

import java.util.ArrayList;

import helpers.BodyUserData;
import helpers.Ownership;

import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import thinkers.MinionEntity;

import low_level_abstract.CollisionManager;

public class MoveToAI  implements CollisionManager{

	private MinionEntity minion;
	private Fixture eyes;
	private ArrayList<MinionEntity> badies = new ArrayList<MinionEntity>();
	public MinionEntity focus;
	public float angle;
	public float distance;
	
	
	public MoveToAI(MinionEntity minion, float eye_radius){
		this.minion = minion;
		FixtureDef eyes = new FixtureDef();
		eyes.isSensor = true;
		CircleShape gBox = new CircleShape();
		gBox.setRadius(40);
		eyes.shape = gBox;
		eyes.userData = new Ownership(minion.pn,this,minion,"eyes");
		
		this.eyes = minion.main.createFixture(eyes);


	}
	
	public void time(int framenum){
		float closest = 0;
		if(badies.size() == 0){ focus = null; return;}
		for(MinionEntity m : badies){
			Vec2 difference = minion.getCenter().add(m.getCenter().mul(-1));
			float angle = (float)Math.atan2(difference.y,difference.x);
			float hyp = (float)Math.sqrt(Math.pow(difference.x, 2)+Math.pow(difference.y, 2));
			if(closest == 0 || hyp < closest){
				focus = m;
				this.angle = angle;
				this.distance = hyp;
			}
		}
	}

	@Override
	public void preSolve(Contact contact,
			boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}

	public void beginContact(Contact contact,
			boolean is_fix_a) {
		Fixture n_fix = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		BodyUserData other = (BodyUserData)n_fix.getBody().getUserData();
		
		if(!n_fix.isSensor() && checkIfBad(other)){
			badies.add(other.owner);
			System.out.println("baddy!"+minion.pn+":"+other.owner.pn);
		}
	}

	@Override
	public void endContact(Contact contact,
			boolean is_fix_a) {
		Fixture n_fix = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		BodyUserData other = (BodyUserData)n_fix.getBody().getUserData();

		if(!n_fix.isSensor() && checkIfBad(other))	
			badies.remove(other.owner);
		
	}

	@Override
	public void postSolve(Contact contact,
			boolean is_fix_a) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean checkIfBad(BodyUserData other){
		return (
					other != null 
					&& other.owner != null 
					&& other.owner.pn != minion.pn 
					&& badies.contains(other.owner)
				);
	}
	
}
