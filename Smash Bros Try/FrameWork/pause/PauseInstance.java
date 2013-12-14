package pause;

import helpers.BodyUserData;
import helpers.Ownership;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import low_level_abstract.CollisionManager;



import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;


/*
 * So a single pause happens
 * 
 * Then another person comes in and hits someone
 * -need to check if that body is paused (UserData)
 * -Everyone gets paused for x longer
 * -the person that was hit needs to change their afterhit velocities
 */

public class PauseInstance {
	int timeleft;
	HashMap<Body, PauseInfo> bodies;
		
	public PauseInstance(Contact contact){
		this.bodies = new HashMap<Body,PauseInfo>();
		contact.setEnabled(false);
		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();
		WorldManifold wm = new WorldManifold();
		contact.getWorldManifold(wm);
		Vec2 Am = A.getLinearVelocityFromWorldPoint(wm.points[0]).mul(A.getMass());
		Vec2 Bm = B.getLinearVelocityFromWorldPoint(wm.points[0]).mul(B.getMass());
		bodies.put(A, new PauseInfo(A, new Impact(Bm, wm.points[0])));
		bodies.put(B, new PauseInfo(B, new Impact(Am, wm.points[0])));
		
		Ownership ca = ((Ownership)contact.getFixtureA().getUserData());
		Ownership cb = ((Ownership)contact.getFixtureB().getUserData());
		int init = Math.round(Am.add(Bm).length()/1000);

		timeleft = (ca != null && ca.pause != null)?ca.pause.getPauseEffect(init):0;
		timeleft =(cb != null && cb.pause != null)?cb.pause.getPauseEffect(init):0;
		if(timeleft == 0) timeleft = init;

		appendFreind(A);
		appendFreind(B);
	}
	
	public PauseInstance(Contact contact, boolean isClash){
		this.bodies = new HashMap<Body,PauseInfo>();
		contact.setEnabled(false);
		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();
		WorldManifold wm = new WorldManifold();
		contact.getWorldManifold(wm);
		Vec2 Am = A.getLinearVelocityFromWorldPoint(wm.points[0]).mul(A.getMass());
		Vec2 Bm = B.getLinearVelocityFromWorldPoint(wm.points[0]).mul(B.getMass());
		bodies.put(A, new PauseInfo(A, new Impact(Bm, wm.points[0])));
		bodies.put(B, new PauseInfo(B, new Impact(Am, wm.points[0])));
		if(isClash) timeleft = -1;
		else 		timeleft = Math.round(Am.add(Bm).length()/10);


		appendFreind(A);
		appendFreind(B);
	}


	
	public void appendFreind(Body body){
		BodyUserData d = (BodyUserData)body.getUserData();
		d.paused = this;
		body.setUserData(d);
	}

	public void appendFreinds(Contact contact){
		contact.setEnabled(false);
		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();
		WorldManifold wm = new WorldManifold();
		contact.getWorldManifold(wm);
		Vec2 Am = A.getLinearVelocityFromWorldPoint(wm.points[0]).mul(A.getMass());
		Vec2 Bm = B.getLinearVelocityFromWorldPoint(wm.points[0]).mul(B.getMass());
		
		if(bodies.containsKey(A))
			bodies.get(A).impacts.add(new Impact(Bm, wm.points[0]));
		else{
			bodies.put(A, new PauseInfo(A, new Impact(Bm, wm.points[0])));
			appendFreind(A);
		}

		if(bodies.containsKey(B))
			bodies.get(B).impacts.add(new Impact(Am, wm.points[0]));
		else{
			bodies.put(B, new PauseInfo(B, new Impact(Am, wm.points[0])));
			appendFreind(B);
		}
		
		timeleft = Math.max(timeleft, Math.round(Am.add(Bm).length()/10));
	}
	
	public void setTime(int time){
		this.timeleft = time;
	}

	public boolean time(){
		if(timeleft == 0) unpause();
		else if(timeleft > 0) timeleft--;
		return (bodies.size()==0);
	}
	
	public void unpause(){
		timeleft = 0;
		int i = 0;
		Iterator<Entry<Body,PauseInfo>> x = bodies.entrySet().iterator();
		while(x.hasNext()){
		    Map.Entry<Body,PauseInfo> entry = x.next();
			if(entry.getValue().unpause(entry.getKey()))
				x.remove();
		}
		
	}
		
	public void appendImpact(Body body, Impact impact){
		bodies.get(body).impacts.add(impact);
	}
	
	private class PauseInfo{
		Vec2 appliedLinVel;
		float appliedAngVel;
		float gravscale;
		ArrayList<Impact> impacts;
		boolean paused = true;
		
		
		public PauseInfo(Body body, Impact impact){
			this.appliedAngVel = body.getAngularVelocity();
			this.appliedLinVel = body.getLinearVelocity().clone();
			this.gravscale = body.getGravityScale();
			
			impacts = new ArrayList<Impact>();
			impacts.add(impact);

			body.setGravityScale(0);
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2());
		}
		
		public boolean unpause(Body body){
			if(paused){
				body.setGravityScale(gravscale);
				body.setLinearVelocity(appliedLinVel);
				body.setAngularVelocity(appliedAngVel);
				paused = false;
			}
			if(impacts.size() > 0){
				body.applyLinearImpulse(impacts.get(0).force, impacts.get(0).point);
				impacts.remove(0);
				return false;
			}
			BodyUserData d = (BodyUserData)body.getUserData();
			d.paused = null;
			body.setUserData(d);
			return true;
		}
		
		
	}
}
