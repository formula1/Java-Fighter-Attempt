package game;


import helpers.BodyUserData;
import helpers.Box2dHelper;
import helpers.Ownership;

import java.util.ArrayList;

import levels.MyFirstLevel;
import low_level_abstract.CollisionManager;
import low_level_abstract.Logic_Entity;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

import clash.Clash;

import characters.PlayerPushFeetEntity;

import pause.PauseInstance;
import thinkers.MinionEntity;


import abstracts.Game;
import abstracts.GameLevel;

public class BS_to_Game extends Game{
	
	public MinionEntity[] pee;
	public GameLevel level;
	public Box2dHelper help;
	public static int current_frame;
	public static ArrayList<Logic_Entity> le;
	public static ArrayList<Clash> c;
	public ArrayList<PauseInstance> pauses;
	
	
	public BS_to_Game() {
		le = new ArrayList<Logic_Entity>();
		c = new ArrayList<Clash>();
		pauses = new ArrayList<PauseInstance>();
	}

	@Override
	public void worldInit() {
		// TODO Auto-generated method stub
		
		/*
		 * Each player is a boxer for the time being
		 * two rectangles for each arm
		 * A circle at the end for a fist
		 * A body
		 * A head
		 * Feet (which I'm thinking is just going to be a cericle of some sort
		 * 
		 * More importantly is how do I build each body?
		 * And how do I want combat to feel
		 * 
		 * 
		 * important to note.
		 * With my current Helper System for collision filtering
		 * 1 is defined for terrain
		 * 2 is defined for generic badguys (they don't contact eachother but do contact opponent
		 * 
		 * 
		 * 
		 */
		
		world.setGravity(new Vec2(0.0f, -10.0f));
		new Box2dHelper(world, players.length,1,.3f);
		
		level = new MyFirstLevel(this);
		level.getBodies(world);
		
		pee = new MinionEntity[players.length];
		for(int i=0;i<players.length;i++){
			pee[i] = new PlayerPushFeetEntity(i,new Vec2(i*10+5f, 16f));
		}
		


	}

	public void beginContact(Contact contact) {
		Ownership fixadata = (Ownership)contact.m_fixtureA.getUserData();
		Ownership fixbdata = (Ownership)contact.m_fixtureB.getUserData();
		
        /*
        if(contact.isTouching() && fixadata != null && fixbdata != null){
                if(fixadata.stored_info != null && fixbdata.stored_info != null){
                        String sa = (String)fixadata.stored_info;
                        String sb = (String)fixbdata.stored_info;
                        
                        if(sa.startsWith("attack") && sb.startsWith("attack")){
                                c.add(new Clash(adata.owner,bdata.owner, contact, new PauseInstance(contact, true)));
                                contact.setEnabled(false);
                                return;
                        }
                }
        }
        */

		if(fixadata != null && (fixbdata == null || fixbdata.player_number != -2)){
			((CollisionManager)fixadata.manager).beginContact(contact, true);
		}
		if(fixbdata != null && (fixadata == null || fixadata.player_number != -2)){
			((CollisionManager)fixbdata.manager).beginContact(contact, false);
		}
	}

	public void endContact(Contact contact) {
		Ownership fixadata = (Ownership)contact.m_fixtureA.getUserData();
		Ownership fixbdata = (Ownership)contact.m_fixtureB.getUserData();
		BodyUserData adata = (BodyUserData)contact.getFixtureA().getBody().getUserData();
		BodyUserData bdata = (BodyUserData)contact.getFixtureB().getBody().getUserData();
		
		if(adata.paused != null
		&& bdata.paused != null
		&& bdata.paused == adata.paused){
			if(adata.paused.deconstruct(contact)) pauses.remove(bdata.paused);
			
		}
		
		if(fixadata != null && (fixbdata == null || fixbdata.player_number != -2)){
			((CollisionManager)fixadata.manager).endContact(contact, true);
		}
		if(fixbdata != null && (fixadata == null || fixadata.player_number != -2)){
			((CollisionManager)fixbdata.manager).endContact(contact, false);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Ownership fixadata = (Ownership)contact.m_fixtureA.getUserData();
		Ownership fixbdata = (Ownership)contact.m_fixtureB.getUserData();
		
		BodyUserData adata = (BodyUserData)contact.getFixtureA().getBody().getUserData();
		BodyUserData bdata = (BodyUserData)contact.getFixtureB().getBody().getUserData();

		if(adata.paused != null && bdata.paused != null && contact.isTouching()) contact.setEnabled(false);
		
		if(fixadata != null && (fixbdata == null || fixbdata.player_number != -2)){
			((CollisionManager)fixadata.manager).preSolve(contact, true);
		}
		if(fixbdata != null && (fixadata == null || fixadata.player_number != -2)){
			((CollisionManager)fixbdata.manager).preSolve(contact, false);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Ownership fixadata = (Ownership)contact.m_fixtureA.getUserData();
		Ownership fixbdata = (Ownership)contact.m_fixtureB.getUserData();
		BodyUserData adata = (BodyUserData)contact.getFixtureA().getBody().getUserData();
		BodyUserData bdata = (BodyUserData)contact.getFixtureB().getBody().getUserData();

		/*
		 * For my purposes
		 * 
		 * 
		 * 
		 * 
		 */
		
		float max = 0;
		for(float f : impulse.normalImpulses)	max = Math.max(max, f);
		int init = Math.round(max/100);

		
		if(init > 0 && contact.isTouching() &&	!contact.getFixtureA().isSensor() 	&& !contact.getFixtureB().isSensor()
		&&	contact.getFixtureA().getBody().getType() == BodyType.DYNAMIC	&& contact.getFixtureB().getBody().getType() == BodyType.DYNAMIC
		){
			if(adata.paused != null)adata.paused.appendFreinds(contact,impulse);
			else if(bdata.paused != null) bdata.paused.appendFreinds(contact,impulse);
			else pauses.add(new PauseInstance(contact, impulse));
		}
		
		
		if(fixadata != null && (fixbdata == null || fixbdata.player_number != -2)){
			((CollisionManager)fixadata.manager).postSolve(contact, true);
		}
		if(fixbdata != null && (fixadata == null || fixadata.player_number != -2)){
			((CollisionManager)fixbdata.manager).postSolve(contact, false);
		}
	}

	public void playerEvent(int player_number, String input, Float value) {
		players[player_number].put(input, value);
	}

	

	@Override
	public void playerEnter(int playernumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerLeave(int playernumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerAction(int playernumber, String button, float amount) {
		// TODO Auto-generated method stub
		
	}

	private int framenum =0;
	Long last_time = 0L;
	public int frames_per_second = 0;

	public void time(long time) {
		current_frame++;
		last_time += time;
		if(last_time>= 1000){
			frames_per_second = Math.round(1000*framenum/last_time);
			last_time = 0L;
			framenum = 0;
		}else framenum++;
		
		level.time(current_frame);
		
		for(int i=0;i<c.size();i++){
			c.get(i).time(current_frame);
			if(c.get(i).startframe == -1){c.remove(i); i--;}
		}
		
		for(int i=0;i<pauses.size();i++){
			pauses.get(i).time();
		}
		
		for(Logic_Entity l : le){
			l.time(current_frame);
		}
	}

	@Override
	public void deleteAssociated(Object o) {
		// TODO Auto-generated method stub
		//delete 
		BodyUserData retype = (BodyUserData)o;
		if(retype.owner != null){
			int pn = retype.owner.pn;
			le.remove(retype.owner);
			if(pn >= 0){
				pee[pn] = null;
				pee[pn] =	new PlayerPushFeetEntity(pn,new Vec2(pn*10+5f, 16f));
			}
		}
	}

}