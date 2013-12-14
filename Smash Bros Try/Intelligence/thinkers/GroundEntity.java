package thinkers;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import ai.FeetHelper;

public abstract class GroundEntity extends MinionEntity{
	FeetHelper feet;
	
	public GroundEntity(int pn, Vec2 position) {
		super(pn, position);
		feet = new FeetHelper(this,1);
	}

}
