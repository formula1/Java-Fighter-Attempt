package graphic;

import java.util.ArrayList;
import java.util.Collection;

import org.jbox2d.dynamics.Body;

import abstracts.GameRenderer;
import abstracts.GameRenderer.BodyGraphic;
import abstracts.GameRenderer.CollisionGraphic;
import abstracts.GameRenderer.FixtureGraphic;
import abstracts.GameRenderer.UIGraphic;
import abstracts.GameRenderer.VelocityGraphic;
import abstracts.entites.GameEntity;
import abstracts.PhysicsManager;

public class DefinitiveGraphicRenderer extends PhysicsRenderer {

	public DefinitiveGraphicRenderer(PhysicsManager pm) {
		super(pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<GameGraphic> getGraphics(GameRenderer gr) {
		int bcount = pm.getBodyCount();
		Body bodies = pm.getBodyList();
		ArrayList<GameGraphic> ret = new ArrayList<GameGraphic>();
		GameEntity e;
		for(int i=0;i<bcount;i++){
			e = (GameEntity)bodies.getUserData();
			if(e.hasBodyGraphics()){
				ret.addAll(e.getBodyGraphics());
			}
			if(e.hasFixtureGraphics()){
				ret.addAll(e.getFixtureGraphics());
			}
			bodies = bodies.getNext();
			/*
			 * Do I put the Graphic Information inside of the User Object?
			 * 
			 */
		}
		return ret;
	}


}
