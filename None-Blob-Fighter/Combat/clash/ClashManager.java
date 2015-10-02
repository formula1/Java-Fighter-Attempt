package clash;

import java.util.ArrayList;


import org.jbox2d.common.Vec2;


import pause.PauseInstance;
import thinkers.MinionEntity;

import attack_abstracts.AttackBox;


public class ClashManager{
	Vec2 origvel;
	public int action;
	public float threshhold;
	public MinionEntity owner;
	public ArrayList<Clash> clashes;
	public AttackBox attack;
	public String fixname;
	
	public ClashManager(MinionEntity owner, float strength, float threshhold){
		clashes = new ArrayList<Clash>();
		this.owner = owner;
		this.threshhold = threshhold;
	}
	
	public void enterClash(Clash clash, AttackBox m, String fixname){
		clashes.add(clash);
		action = (Integer)owner.effects.callEffects("clashaction", 0);
		origvel = owner.main.getLinearVelocity().mul(1);
		this.attack = m;
		this.fixname = fixname;
		attack.attacksOff();
	}

	public float getInit(Vec2 contact){
		float ret = (Integer)owner.effects.callEffects("clashinit", action)/clashes.size()+calculateVelPercent(contact)/2;
		action = 0;
		return ret;
	}
	
	public float getPush(Vec2 contact, int iteration){
		float ret = Math.round(Math.pow((Integer)owner.effects.callEffects("clashpush", action), iteration))/clashes.size()+calculateVelPercent(contact)/2;
		action = 0;
		return ret;
	}
	
	public void loseAll(){
		while(clashes.size() > 0){
			clashes.get(0).setLoser(this);
		}
	}
	
	
	public void loseClash(ClashManager Winner, Clash clash){
		clashes.remove(clash);
		owner.setStun(100);
		
		float angle = (float)Math.atan2((Winner.owner.getCenter().y - owner.getCenter().y), (Winner.owner.getCenter().x - owner.getCenter().x));
		Vec2 vel = new Vec2((float)Math.cos(angle),(float)Math.sin(angle));
		owner.setVelocity(vel.mul(10), clash.contactpoint);
	}
	
	public void winClash(ClashManager Loser, Clash clash){
		clashes.remove(clash);
	}
	
	private float calculateVelPercent(Vec2 contact){
		Vec2 delta_cc = owner.getCenter().add(contact.mul(-1));
		
		
		float center_to_contact = (float)Math.atan2(delta_cc.y, delta_cc.x);
		float velocity_angle = (float)Math.atan2(owner.main.getLinearVelocity().y,owner.main.getLinearVelocity().x);
		
		float velocity_value = owner.main.getLinearVelocity().length();
		
		float finale = velocity_value - velocity_value*(velocity_angle-center_to_contact)/(2*(float)Math.PI);
		return finale;
	}

}
