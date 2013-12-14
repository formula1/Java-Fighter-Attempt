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
		
	public PauseInstance(Contact contact, ContactImpulse impulse){
		this.bodies = new HashMap<Body,PauseInfo>();

		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();

		bodies.put(A, new PauseInfo(A));
		bodies.put(B, new PauseInfo(B));
		
		Ownership ca = ((Ownership)contact.getFixtureA().getUserData());
		Ownership cb = ((Ownership)contact.getFixtureB().getUserData());

		float max = 0;
		for(float f : impulse.normalImpulses)	max = Math.max(max, f);
		int init = Math.round(max/100);
		
		System.out.println("imp:"+max);
		
		timeleft = (ca != null && ca.pause != null)?ca.pause.getPauseEffect(init):0;
		timeleft +=(cb != null && cb.pause != null)?cb.pause.getPauseEffect(init):0;
		if(timeleft == 0) timeleft = init;

		appendFreind(A);
		appendFreind(B);
	}
	/*
	public PauseInstance(Contact contact, boolean isClash){
		this.bodies = new HashMap<Body,PauseInfo>();
		contact.setEnabled(false);
		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();
		WorldManifold wm = new WorldManifold();
		contact.getWorldManifold(wm);
		Vec2 Am = A.getLinearVelocityFromWorldPoint(wm.points[0]).mul(A.getMass());
		Vec2 Bm = B.getLinearVelocityFromWorldPoint(wm.points[0]).mul(B.getMass());
		bodies.put(A, new PauseInfo(A));
		bodies.put(B, new PauseInfo(B));
		if(isClash) timeleft = -1;
		else 		timeleft = Math.round(Am.add(Bm).length()/10);


		appendFreind(A);
		appendFreind(B);
	}
*/

	
	public void appendFreind(Body body){
		BodyUserData d = (BodyUserData)body.getUserData();
		d.paused = this;
		body.setUserData(d);
	}

	public void appendFreinds(Contact contact, ContactImpulse impulse){
		contact.setEnabled(false);
		Body A = contact.getFixtureA().getBody();
		Body B = contact.getFixtureB().getBody();
		WorldManifold wm = new WorldManifold();
		contact.getWorldManifold(wm);
		
		if(bodies.containsKey(A))
			bodies.get(A).moreVel(A);
		else{
			bodies.put(A, new PauseInfo(A));
			appendFreind(A);
		}

		if(bodies.containsKey(B))
			bodies.get(B).moreVel(B);
		else{
			bodies.put(B, new PauseInfo(B));
			appendFreind(B);
		}
		float max = 0;
		for(float f : impulse.normalImpulses)	max = Math.max(max, f);
		int init = Math.round(max/100);
		
		System.out.println("imp:"+max);		
		timeleft = Math.max(timeleft,  init);
	}
	
	public void setTime(int time){
		this.timeleft = time;
	}

	public boolean time(){
		if(timeleft == 0) unpause();
		else if(timeleft > 0){
			timeleft--;
			Iterator<Entry<Body,PauseInfo>> x = bodies.entrySet().iterator();
			while(x.hasNext()){
			    Map.Entry<Body,PauseInfo> entry = x.next();
				entry.getValue().remainPause();
			}
		}
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
		bodies.get(body).moreVel(body);
	}
	
	private class PauseInfo{
		ArrayList<Vec2> LinVels;
		ArrayList<Float> AngVels;
		float gravscale;
		boolean paused = true;
		Body body;
		
		
		public PauseInfo(Body body){
			this.body = body;
			AngVels = new ArrayList<Float>();
			LinVels = new ArrayList<Vec2>();
			 AngVels.add(body.getAngularVelocity());
			LinVels.add(body.getLinearVelocity().clone());
			this.gravscale = body.getGravityScale();
			
		}
		
		public void remainPause(){
			body.setGravityScale(0);
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2());
		}
		
		public void moreVel(Body body){
			LinVels.add(body.getLinearVelocity().clone());
			AngVels.add(body.getAngularVelocity());
		}
		
		public boolean unpause(Body body){
			if(paused){
				body.setGravityScale(gravscale);
				Vec2 finallinVel = new Vec2();
				for(Vec2 lvel : LinVels){
					finallinVel.add(lvel);
				}
				Float finalangVel = 0f;
				for(Float avel : AngVels){
					finalangVel += avel;
				}
				System.out.println("final lin vel:"+finallinVel.toString());
				body.setLinearVelocity(finallinVel);
				body.setAngularVelocity(finalangVel);
				paused = false;
			}
			BodyUserData d = (BodyUserData)body.getUserData();
			d.paused = null;
			body.setUserData(d);
			return true; // used for delayed rapid impacts
		}
		
		
	}
}
