package abstracts.entites;

import java.util.Collection;
import java.util.HashMap;

import abstracts.GameRenderer.BodyGraphic;
import abstracts.GameRenderer.FixtureGraphic;

public class GameElement {

	
	public HashMap<String,BodyGraphic> bodygraphics;
	public HashMap<String,FixtureGraphic> fixturegraphics;
	
	public GameElement(){
		
	}
	
	public boolean hasBodyGraphics(){
		return (bodygraphics.size() > 0);
	}
	public boolean hasFixtureGraphics(){
		return (fixturegraphics.size() > 0);		
	}
	
	public Collection<BodyGraphic> getBodyGraphics(){
		return bodygraphics.values();
	}

	public Collection<FixtureGraphic> getFixtureGraphics(){
		return fixturegraphics.values();
	}

}
