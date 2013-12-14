package low_level_abstract;


import org.jbox2d.dynamics.contacts.Contact;

public interface CollisionManager {
	
	public abstract void preSolve(Contact contact, boolean is_fix_a);
	public abstract void beginContact(Contact contact, boolean is_fix_a);
	public abstract void endContact(Contact contact, boolean is_fix_a);
	public abstract void postSolve(Contact contact, boolean is_fix_a);
	


}
