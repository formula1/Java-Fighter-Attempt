package helpers;

import pause.PauseInterface;
import thinkers.MinionEntity;
import low_level_abstract.CollisionManager;


public class Ownership{
	public int player_number;
	public CollisionManager manager;
	public PauseInterface pause;
	public Object stored_info;
	public Ownership(int player_number, CollisionManager manager, MinionEntity owner){
		this.manager = manager;
		this.player_number = player_number;
	}
	
	public Ownership(CollisionManager manager){
		this.manager = manager;
		this.player_number = -2;
	}
	
	public Ownership(int player_number, CollisionManager manager, MinionEntity owner, Object stored_info){
		this.manager = manager;
		this.player_number = player_number;
		this.stored_info = stored_info;
	}
	
	public Ownership(int player_number, CollisionManager manager, MinionEntity owner, Object stored_info, PauseInterface pause){
		this.manager = manager;
		this.player_number = player_number;
		this.stored_info = stored_info;
		this.pause = pause;
	}

}