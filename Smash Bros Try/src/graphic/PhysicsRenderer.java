package graphic;

import java.util.Collection;

import org.jbox2d.dynamics.Body;

import abstracts.GameRenderer;
import abstracts.GameRenderer.BodyGraphic;
import abstracts.GameRenderer.CollisionGraphic;
import abstracts.GameRenderer.FixtureGraphic;
import abstracts.GameRenderer.UIGraphic;
import abstracts.GameRenderer.VelocityGraphic;
import abstracts.PhysicsManager;
import assets.SinCosTable;

public abstract class PhysicsRenderer {
	protected PhysicsManager pm;
	protected SinCosTable sct;

	public PhysicsRenderer(PhysicsManager pm){
		this.pm = pm;
		sct = new SinCosTable(13);
	}
	
	public abstract Collection<GameGraphic> getGraphics(GameRenderer gr);
}
