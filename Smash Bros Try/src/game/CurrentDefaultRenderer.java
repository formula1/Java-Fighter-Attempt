package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import abstracts.GameRenderer;
import abstracts.GameRenderer.BodyGraphic;
import abstracts.GameRenderer.CollisionGraphic;
import abstracts.GameRenderer.FixtureGraphic;
import abstracts.GameRenderer.UIGraphic;
import abstracts.GameRenderer.VelocityGraphic;
import abstracts.entites.GameEntity;
import abstracts.PhysicsManager;
import assets.SinCosTable;
import graphic.GameGraphic;
import graphic.PhysicsRenderer;

public class CurrentDefaultRenderer extends PhysicsRenderer{

	public CurrentDefaultRenderer(PhysicsManager pm) {
		super(pm);
		sct = new SinCosTable(12);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<GameGraphic> getGraphics(final GameRenderer gr) {
		int bcount = pm.getBodyCount();
		Body bodies = pm.getBodyList();
		ArrayList<GameGraphic> ret = new ArrayList<GameGraphic>();
		for(int i=0;i<bcount;i++){

			ret.add(gr.new BodyGraphic(bodies, 4){
				public void drawGraphic(Graphics2D g, Vec2 position, float angle) {
					// TODO Auto-generated method stub
					g.setColor(new Color(0xFF0000));
					g.translate(position.x, position.y);
					g.rotate(angle);
					g.drawLine(0, 0, 2, 2);
					g.fillOval(0, 0, 1, 1);
					g.rotate(-angle);
					g.translate(-position.x, -position.y);
				}
				
			});
			Fixture fs = bodies.getFixtureList();
			int fcount = bodies.m_fixtureCount;
			for(int j=0;j<fcount;j++){
				ret.add(gr.new FixtureGraphic(fs, 3){
					public void drawGraphic(Graphics2D g, Vec2 position, float angle) {
						g.setColor(new Color(0x0000FF));
						if(fix.getType() == ShapeType.CIRCLE ){
							CircleShape retype = (CircleShape)fix.getShape();
							float radius = retype.m_radius;
							Vec2 currentloc;
							Vec2 nextloc;
							g.translate(position.x, position.y);
							g.rotate(angle);
							for(int i=0;i<sct.na;i++){
								currentloc = sct.calculate(i);
								nextloc = sct.calculate(i+1);
								g.drawLine(
									Math.round(currentloc.x*radius),
									Math.round(currentloc.y*radius),
									Math.round(
											nextloc.x*
											radius
									),
									Math.round(nextloc.y*radius)
								);
							}
							g.drawLine(0, 0, Math.round(radius), 0);
							g.rotate(-angle);
							g.translate(-position.x, -position.y);

						}else if(fix.getType() == ShapeType.POLYGON){
							PolygonShape retype = (PolygonShape)fix.getShape();
							Vec2[] points = retype.getVertices();
							int vcount = retype.getVertexCount();
							g.translate(position.x, position.y);
							g.rotate(angle);
							for(int i=0;i<vcount;i++){
								int j = (i+1)%(vcount);
								g.drawLine(
									Math.round(points[i].x), Math.round(points[i].y),
									Math.round(points[j].x), Math.round(points[j].y)
								);
							}
							g.rotate(-angle);
							g.translate(-position.x, -position.y);
						}
					}
					
				});
				fs.getNext();
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
