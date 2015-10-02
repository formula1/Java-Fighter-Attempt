package low_level_abstract;


public interface Logic_Entity extends CollisionManager{

	public abstract void time(int frame_num);

	
	public abstract void die();
	
}
