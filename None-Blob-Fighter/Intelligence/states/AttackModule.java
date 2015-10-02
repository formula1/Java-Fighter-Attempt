package states;

import helpers.BodyUserData;
import helpers.Box2dHelper;
import helpers.Ownership;
import helpers.StateFixData;

import java.util.HashMap;

import low_level_abstract.CollisionManager;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import attack_abstracts.AttackHelper;

import state.StateEntity;
import state.StateModule;

public abstract class AttackModule extends StateModule{
	int max_hits_per_body;
	HashMap<Body,Integer> hit_per_body; 

	public AttackModule(StateEntity tobe) {
		super(tobe);
		max_hits_per_body = 1;
		hit_per_body = new HashMap<Body,Integer>();
	}

	@Override
	public String[] getStateActions() {
		return new String[]{"attack-"+getAttackName()};
	}

	protected abstract String getAttackName();
	
	public FixtureDef[] getFixtures(StateEntity Minion) {
		Shape[] shapes = getShapes();
		FixtureDef[] fixtures = new FixtureDef[shapes.length];
		System.out.println(shapes.length+" sdsdfs ");
		for(int i=0;i<shapes.length;i++){
			fixtures[i] = new FixtureDef();
			fixtures[i].shape = shapes[i];
			fixtures[i].friction = AttackHelper.friction;
			fixtures[i].density =  AttackHelper.density;
			fixtures[i].filter.categoryBits =0;
			fixtures[i].filter.maskBits = 0;
			fixtures[i].isSensor = true;
			// TODO take care of alliances
			fixtures[i].userData = new StateFixData(null, new GACM(), getAttackName());
//			boxing.userData = new Ownership(owner.pn,this, owner, "attack|"+getAttackLabel()+"-"+i, this);
		}
		
		
		return fixtures;
	}
	
	public abstract Shape[] getShapes();
	
	public boolean canApplyEffects(String statename) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canStore(String statename) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	private class GACM implements CollisionManager{

		@Override
		public void preSolve(Contact contact, boolean is_fix_a) {
			// TODO Auto-generated method stub
			
		}

		public void beginContact(Contact contact,
				boolean is_fix_a) {
			Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
			Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;

			if(contact.isTouching()){
				Integer ref = hit_per_body.get(n_bod.getBody());
				
				if(ref != null && ref >= max_hits_per_body){
					contact.setEnabled(false);
					return;
				}
				int hit = (ref != null)?ref+1:1;
				hit_per_body.put(n_bod.getBody(), hit);
				
				
				
				if(n_bod.getUserData() != null){
					BodyUserData other = (BodyUserData)n_bod.getBody().getUserData();
					hitPlayer(other.owner,contact, is_fix_a, (String)fo.stored_info);
				}else	hitObject(n_bod,contact, is_fix_a, (String)fo.stored_info);
			}
		}

		public void endContact(Contact contact,
				boolean is_fix_a) {
			Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
			Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
			// TODO Auto-generated method stub
			if(hit_per_body.containsKey(n_bod.getBody()) && hit_per_body.get(n_bod.getBody()) >= max_hits_per_body)
				hit_per_body.remove(n_bod.getBody());
			else
				contact.setEnabled(false);
		}

		@Override
		public void postSolve(Contact contact, boolean is_fix_a) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

}
