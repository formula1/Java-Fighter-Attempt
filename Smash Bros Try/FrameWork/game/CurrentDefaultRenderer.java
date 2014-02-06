package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;



import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.PrismaticJoint;

import effects.Damage;

import thinkers.MinionEntity;

import abstracts.Game;
import assets.SinCosTable;
import assets.Vec2c;
import assets.Vec2u;
import graphic.GameGraphic;
import graphic.GameRenderer;
import graphic.GraphicManager;

public class CurrentDefaultRenderer extends GameRenderer{
	private static ArrayList<GameGraphic> imt;

	
	public CurrentDefaultRenderer() {
		sct = new SinCosTable(12);
		imt = new ArrayList<GameGraphic>();
	}

	
	int framenum = 0;
	Long tizle = 0L;
	public int frames_per_second = 0;
	
	
	public static void addGraphic(GameGraphic adding){
		imt.add(adding);
	}
	
	@Override
	public Collection<GameGraphic> getGraphics(final GraphicManager gr) {
		BS_to_Game retype = (BS_to_Game)gr.game;
		World pm = BS_to_Game.world;
		setCamera(gr);
		int bcount = pm.getBodyCount();
		int jcount = pm.getJointCount();

//		System.out.println("body count"+bcount);
		
		Joint joints = pm.getJointList();
		ArrayList<GameGraphic> ret = new ArrayList<GameGraphic>();
		
		Long this_time = System.nanoTime();
		if(this_time - tizle>= 1000*1000*1000){
			long trans = (int)(this_time - tizle);
			long f = ((long)framenum) * 1000*1000*1000;
			frames_per_second = (int)Math.round(f/trans);
			tizle = this_time;;
			framenum = 0;
		}else framenum++;
		
		
		
/*		for(int i=0;i<jcount;i++){
			ret.add(gr.new JointGraphic(joints, 2){
				public void drawGraphic(Graphics2D g, Vec2 position, Joint j) {
					// TODO Auto-generated method stub
					g.setColor(new Color(0x000077));
					if(j.getType() == JointType.PRISMATIC){
						PrismaticJoint retype = (PrismaticJoint)j;
						g.translate(retype.position.x, position.y);

						g.rotate(angle);
						g.fillOval(0, 0, 3, 3);
						g.setColor(new Color(0x5555FF));
						g.drawLine(0, 0, 2, 2);
						g.rotate(-angle);
						g.translate(-position.x, -position.y);
					}else if(j.getType() == JointType.REVOLUTE){
						
					}else if(j.getType() == JointType.WELD){
						
					}
					
				}
				
			});
		}
		*/

		ret.add(new GameGraphic(10, false){
			@Override
			public void draw(Graphics2D g) {
				g.setColor(new Color(1,1,1,.5f));
				g.fillRect(0, 0, 100, 100);
				g.setColor(new Color(0x000000));
				g.drawString("Game FPS: "+((BS_to_Game)gr.game).frames_per_second, 10, 10);
				g.drawString("Graphic FPS: "+frames_per_second, 10, 30);
				
			}
			
		});

		ret.add(new GameGraphic(10, false){
			@Override
			public void draw(Graphics2D g) {
				g.setColor(new Color(1,1,1,.3f));
				g.fillRect(0, gr.graphicWH.height-100, gr.graphicWH.width, 100);
				float init_off = gr.graphicWH.width/(((BS_to_Game)gr.game).pee.length+1);
				for(MinionEntity piss : ((BS_to_Game)gr.game).pee){
					int offset = Math.round((piss.pn+1)*init_off);

					g.setColor(new Color(0xFF7700));
					g.fillRect(offset-40, gr.graphicWH.height-90, 80, 80);
					g.setColor(new Color(0x000000));
					int rez = 0;
					if(((Damage)piss.effects.effects.get("damage")) != null)
						rez = ((Damage)piss.effects.effects.get("damage")).applieddamage;
					
					g.drawString("Player "+piss.pn+":"+rez, offset-40, gr.graphicWH.height-50);
				}
				
			}
			
		});

		
		for(Body bodies = pm.getBodyList();bodies != null;bodies = bodies.getNext()){
			ret.add(new GameGraphic(4, bodies){
				public void draw(Graphics2D g) {
					Vec2 position = ((Body)userdata).getWorldCenter();
					float angle = ((Body)userdata).getAngle();
					g.setColor(new Color(0xFF0000));
					g.translate(position.x, position.y);
					g.rotate(angle);
					g.drawLine(0, 0, 2, 2);
					g.fillOval(0, 0, 1, 1);
					g.rotate(-angle);
					g.translate(-position.x, -position.y);
				}
				
			});
			
			for(Fixture fs = bodies.getFixtureList();fs != null;fs = fs.getNext()){
				int laying = 3;
				if(fs.m_filter.categoryBits == 0 || fs.isSensor())
					laying = 1;

				ret.add(new GameGraphic(laying, fs){

					@Override
					public void draw(Graphics2D g) {
						Fixture fix = ((Fixture)userdata);
						if(fix.m_filter.categoryBits == 0)
							g.setColor(new Color(0x00FFFF));
						else if(fix.m_isSensor)
							g.setColor(new Color(0xFF0000));
						else
							g.setColor(new Color(0x0000FF));
							

						if(fix.getType() == ShapeType.CIRCLE ){
							CircleShape retype = (CircleShape)fix.getShape();
							Vec2 npos = retype.m_p.add(fix.m_body.getWorldCenter());
							g.translate(npos.x, npos.y);
							g.rotate(fix.m_body.getAngle());

							float radius = retype.m_radius;
							Vec2 currentloc;
							Vec2 nextloc;
							for(int i=0;i<sct.na;i++){
								currentloc = new Vec2c(sct.calculate(i)).asVec2();
								nextloc = new Vec2c(sct.calculate(i+1)).asVec2();
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
							g.rotate(-fix.m_body.getAngle());
							g.translate(-npos.x, -npos.y);

						}else if(fix.getType() == ShapeType.POLYGON){
							g.translate(fix.m_body.getWorldCenter().x, fix.m_body.getWorldCenter().y);
							g.rotate(fix.m_body.getAngle());
							PolygonShape retype = (PolygonShape)fix.getShape();
							Vec2[] points = retype.getVertices();
							int vcount = retype.getVertexCount();
							for(int i=0;i<vcount;i++){
								int j = (i+1)%(vcount);
								g.drawLine(
									Math.round(points[i].x), Math.round(points[i].y),
									Math.round(points[j].x), Math.round(points[j].y)
								);
							}
							g.rotate(-fix.m_body.getAngle());
							g.translate(-fix.m_body.getWorldCenter().x, -fix.m_body.getWorldCenter().y);
						}else if(fix.getType() == ShapeType.EDGE){
							EdgeShape retype = (EdgeShape)fix.getShape();
							g.drawLine(
								Math.round(retype.m_vertex1.x), Math.round(retype.m_vertex1.y),
								Math.round(retype.m_vertex2.x), Math.round(retype.m_vertex2.y)
							);
							
						}

					}
					
				});
//				if(fs.getNext() == null) System.out.println(ret.size());
			}
			/*
			 * Do I put the Graphic Information inside of the User Object?
			 * 
			 */
		}
		
		ret.addAll(imt);
		imt = new ArrayList<GameGraphic>();

		return ret;
	}

	public void setCamera(GraphicManager gr){
		gr.curCam =((BS_to_Game)gr.game).level.cameraSetting(gr.curCam);

	}


}
