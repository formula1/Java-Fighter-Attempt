package helpers;

import low_level_abstract.CollisionManager;

public class StateFixData {
	public Alliance alliance;
	public CollisionManager manager;
	public String fixname;
	
	public StateFixData(CollisionManager manager){
		this.manager = manager;
	}

	public StateFixData(Alliance alliance, CollisionManager manager){
		this.manager = manager;
		this.alliance = alliance;
	}

	public StateFixData(CollisionManager manager, String name){
		this.manager = manager;
		this.fixname = name;
	}

	
	public StateFixData(Alliance alliance, CollisionManager manager, String name){
		this.manager = manager;
		this.alliance = alliance;
		this.fixname = name;
	}
}
