package effects;

import org.jbox2d.common.Vec2;

import thinkers.MinionEntity;

import effect_abstracts.Effect;


public class Damage extends Effect {
	public int applieddamage = 10;
	MinionEntity Owner;

	public Damage(MinionEntity Owner) {
		super("damage", -1);
		this.Owner = Owner;
	}
	
	public Damage(MinionEntity Owner, int amount){
		super("damage", -1);
		this.Owner = Owner;
		applieddamage = amount;
	}

	
	/*
	 * 
	 * Need to add a Stun Listener - multiplies stun
	 * Need to add a Impulse Listener - multiplies impulse
	 * 
	 * Need to add stack priority
	 * Need to be able to remove it
	 * Need to also check if the owner already has the effect so I can just stack ontop of it
	 * 
	 */
	
	
	@Override
	public Object activate(String effect, Object initialvalue) {
		float multiplier = 1f + ((float)applieddamage)/100f;
		if(effect == "recieveimpulse"){
			return ((Vec2)initialvalue).mul(multiplier);
		}else if(effect == "stun"){
			float retype = ((Integer)initialvalue).floatValue();
			return (Integer)Math.round(retype*multiplier);
		}else{
			System.out.println("error:activating damage without proper listener");
			return initialvalue;
		}
	}

	@Override
	public void append(Effect newInstance) {
		/*
		 * when new damage comes in, it should add to this one
		 * 
		 * false means that it will not create a new effect copy
		 * which I should add at some point to allow a proper "stack" configuration
		 */
		applieddamage += (Integer)Owner.effects.callEffects("damage", ((Damage)newInstance).applieddamage);
		System.out.println("damage:"+applieddamage);
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public String[] getListeners() {
		return new String[]{"recieveimpulse","stun"};
	}
	
		
}
