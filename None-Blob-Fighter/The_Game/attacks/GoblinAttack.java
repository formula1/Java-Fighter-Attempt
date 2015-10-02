package attacks;

import java.util.HashMap;

import helpers.Box2dHelper;
import helpers.Ownership;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

import thinkers.MinionEntity;

import clash.ClashPriorityEffect;

import effects.Damage;


import attack_abstracts.AttackBox;

public class GoblinAttack extends AttackBox {


	
	public GoblinAttack(MinionEntity owner) {
		super(owner,1);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getAttackLabel() {
		// TODO Auto-generated method stub
		return "swipe";
	}

	@Override
	protected Shape[] createShapes() {

		Shape[] swordslash = new Shape[4];
		PolygonShape gBox = new PolygonShape();
		
		Vec2[] verts = new Vec2[7];
		float start;
		float radius = 4;
		Vec2 offset = new Vec2(0,0);
		
		for(int i=0;i<4;i++){
			gBox = new PolygonShape();
			start = ((float)Math.PI)*((float)i)/2f;
			
			verts[0] = new Vec2(0,0);

			for(int j=1;j<verts.length;j++){
				verts[j] = new Vec2(
								(float)Math.cos(start+Box2dHelper.TWELVTH*2+Box2dHelper.TWELVTH*j),
								(float)Math.sin(start+Box2dHelper.TWELVTH*2+Box2dHelper.TWELVTH*j)
							).mul(radius).add(offset);
			}

			
			gBox.set(verts, verts.length);
			swordslash[i] = gBox;
		}

		return swordslash;
	}

	private Vec2 getVels(String fixture_name, Fixture n_bod){
		Vec2 velocityset = new Vec2(0,0);
		
		switch(fixture_name.split("-")[1]){
			case "0": velocityset.y = n_bod.m_body.getMass()*30; break;
			case "1": velocityset.x = -1*n_bod.m_body.getMass()*30; break;
			case "2": velocityset.y = -1*n_bod.m_body.getMass()*30; break;
			case "3": velocityset.x = n_bod.m_body.getMass()*30; break;
		}

		
		return velocityset;
		
	}
	
	@Override
	public void hitPlayer(MinionEntity other_owner, Contact contact, boolean is_fix_a,String fixname) {
		// TODO Auto-generated method stub
		Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
		Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		Vec2 v = getVels(fixname,n_bod);
		
		other_owner.effects.applyEffect(new Damage(other_owner));
		other_owner.setStun(90);
		other_owner.setVelocity((Vec2)owner.effects.callEffects("applyimpulse", v), contact.getManifold().points[0].localPoint);
		System.out.println(v.toString());

		
	}

	@Override
	public void hitObject(Fixture other, Contact contact, boolean is_fix_a,String fixname) {
		// TODO Auto-generated method stub
		applyVels(other,contact,getVels(fixname,other));
	}
	
	private void applyVels(Fixture other, Contact contact, Vec2 addition){
		Vec2 negativeforce = other.m_body.getLinearVelocity().mul(other.m_body.getMass()*-1);

		other.m_body.applyLinearImpulse(negativeforce.add(addition), contact.getManifold().points[0].localPoint);
		
	}

	@Override
	public ClashPriorityEffect getClashPriority() {
		// TODO Auto-generated method stub
		return new ClashPriorityEffect(10,2);
	}

	@Override
	public int getPauseEffect(int pausetime) {
		// TODO Auto-generated method stub
		return Math.round(pausetime*1.2f);
	}

}
