package helpers;

import pause.PauseInstance;
import thinkers.MinionEntity;

public class BodyUserData {
	public PauseInstance paused = null;
	public MinionEntity owner = null;
	public BodyUserData(MinionEntity owner){
		this.owner = owner;
		
	}
}
