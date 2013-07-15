package abstracts;


import graphic.GameGraphic;
import graphic.PhysicsRenderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.jbox2d.collision.Collision;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.ManifoldPoint;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;



import abstracts.entites.GameEntity;
import assets.WH;

public class GameRenderer implements Runnable{
/*
 * 
 * Takes in as inputs
 * -Physics
 * -Game
 * 
 * 
 * Layers are important for UI on top of objects and backgrounds
 * 
 * 
 * Uses either a preloaded graphic as an instance graphic or a generated one
 * Either way, they need to be treated the same in terms of adding and removing
 * 
 * Binding + offset
 * 
 */
	boolean running;
	private Camera curCam;
	int time = 0;
	Long lastAnim = 0L;

	Engine main;
	Game game;
	
	boolean default_render = false;
	PhysicsRenderer dr;
	
	Graphics2D g;
	WH graphicWH;
	
	ArrayList<GameGraphic> currentGraphics;
	
	
	
	public GameRenderer(Engine main, Game game){
		this.main = main;
		this.game = game;
		graphicWH = new WH(main.getDrawGraphics().getClipBounds().width,main.getDrawGraphics().getClipBounds().height);
		this.curCam = new Camera(new Vec2(0,0),new Vec2(1,1),0f);
	}
	
	public GameRenderer(Engine main, Game game, PhysicsRenderer dr){
		this.main = main;
		this.game = game;
		default_render = true;
		this.dr = dr;
		graphicWH = main.getGraphicBounds();
		this.curCam = new Camera(new Vec2(graphicWH.width/2,graphicWH.height/2),new Vec2(2,2),.75f);
	}

	
	public void setCamera(Vec2 scale,Float angle, Vec2 pos){
		curCam = new Camera(pos, scale, angle);
	}
	public void tweenCamera(Vec2 scale,Float angle, Vec2 pos, int time){
		curCam.setTween(scale,angle,pos,time);
	}
	public void tweenCamera(float scale,Float angle, Vec2 pos, int time){
		curCam.setTween(scale,angle,pos,time);
	}
	
	public void cameraAnimation(){
		if(curCam.anim){
			curCam.tween(((float)(System.currentTimeMillis()-lastAnim)/(float)time));
		}	
	}
	
	
	
	public void run(){
		running = true;
		cameraAnimation();
		while(running){
			display();
			try { Thread.sleep(30); } catch (Exception e) {}
			lastAnim = System.currentTimeMillis();
		}
	}
	
	public void display(){
		g = (Graphics2D) main.getDrawGraphics();
		g.setColor(Color.black);
		g.setClip(0, 0, graphicWH.width, graphicWH.height);
		g.fillRect(0,0,graphicWH.width,graphicWH.height);
		currentGraphics = new ArrayList<GameGraphic>();
		
		g.translate(0,graphicWH.height/2);
		g.scale(1,-1);
		g.translate(0,-graphicWH.height/2);

		g.translate(curCam.position.x, curCam.position.y);
		g.scale(curCam.scale.x, curCam.scale.y);
		g.rotate(curCam.angle);

		
		if(default_render){
			currentGraphics.addAll(dr.getGraphics(this));
			if(game.hasUI()){
				currentGraphics.addAll(game.getUI());
			}
		}
		int currentLayer = 0;
		int nextLayer = -1;
		int currentG = 0;
		while(currentGraphics.size() > 0){
			if(currentGraphics.get(currentG).layer == currentLayer){
				currentGraphics.get(currentG).draw(g);
				currentGraphics.remove(currentG);
			}else{
				currentG++;
			}
			if(currentG >= currentGraphics.size()){
				currentLayer = nextLayer;
				nextLayer = -1;
				currentG = 0;
				continue;
			}
			if(nextLayer == -1 || nextLayer > currentGraphics.get(currentG).layer){
				nextLayer = currentGraphics.get(currentG).layer;
			}
		}
		g.dispose();
		main.graphicDoneCallback();
	}	

	public class Camera{
		public Vec2 scale;
		public float angle;
		public Vec2 position;
		private Camera tweenCamera;
		private int tweenTime;
		boolean anim=false;
		
		public Camera(Vec2 pos, Vec2 scale, Float ang){
			if(scale != null) this.scale = scale;
			if(ang != null) this.angle = ang;
			if(pos != null) this.position = pos;
		}
		
		public void tween(float div){
			this.scale.x += tweenCamera.scale.x/div;
			this.scale.y += tweenCamera.scale.y/div;
			this.angle += tweenCamera.angle/div;
			this.position.x += tweenCamera.position.x/div;
			this.position.y += tweenCamera.position.y/div;
			if(this.equals(tweenCamera)){
				anim=false;
			}
		}
		
		
		public void setTween(Vec2 scale,Float angle, Vec2 pos, int time){
			tweenCamera = new Camera(
					new Vec2(pos.x - position.x, pos.y - position.y),
					new Vec2(scale.x - this.scale.x, scale.y-this.scale.y), 
					angle - this.angle
			);
			this.tweenTime = time;
			anim = true;
		}
		public void setTween(float scale,Float angle, Vec2 pos, int time){
			tweenCamera = new Camera(
					new Vec2(pos.x - position.x, pos.y - position.y),
					new Vec2(scale - this.scale.x, scale-this.scale.y), 
					angle - this.angle
			);
			this.tweenTime = time;
			anim = false;
		}
	}
	
	public abstract class BodyGraphic extends GameGraphic{
		private Body body;
		public BodyGraphic(Body b, int layer){
			super(layer);
			this.body = b;
		}
		public void draw(Graphics2D g){
			Vec2 pos = new Vec2(
					body.getPosition().x,
					body.getPosition().y
			);
			drawGraphic(g, pos, body.getAngle());
		}
		public abstract void drawGraphic(Graphics2D g, Vec2 position, float angle);

	}
	
	public abstract class FixtureGraphic extends GameGraphic{
		protected Fixture fix;
		public FixtureGraphic(Fixture f, int layer){
			super(layer);
			this.fix = f;
		}
		public void draw(Graphics2D g){
			Vec2 bpos = new Vec2(
					fix.getBody().getPosition().x,
					fix.getBody().getPosition().y
			);
			if(fix.getType() == ShapeType.CIRCLE ){
				CircleShape retype = (CircleShape)fix.getShape();
				bpos.x += retype.m_p.x;
				bpos.y += retype.m_p.y;
			}else if(fix.getType() == ShapeType.POLYGON){
				PolygonShape retype = (PolygonShape)fix.getShape();
				Vec2 avg = new Vec2();
				for(int i=0;i<retype.m_vertices.length;i++){
					avg.x += retype.m_vertices[i].x/retype.m_vertices.length;
					avg.y += retype.m_vertices[i].y/retype.m_vertices.length;
				}
//				bpos.x += avg.x*curCam.scale.y+curCam.position.y;
//				bpos.y += avg.y*curCam.scale.y+curCam.position.y;
			}
			
			drawGraphic(g, bpos, fix.getBody().getAngle());
		}
		public abstract void drawGraphic(Graphics2D g, Vec2 position, float angle);

	}
	public abstract class CollisionGraphic extends GameGraphic{
		private Contact col;
		
		public CollisionGraphic(Contact c, int layer){
			super(layer);
			col = c;
		}
		
		public void draw(Graphics2D g){
			Manifold m = col.getManifold();
			ManifoldPoint[] mp = m.points;
			Vec2 avg = new Vec2();
			for(int i=0;i<mp.length;i++){
				avg.x += mp[i].localPoint.x/mp.length;
				avg.y += mp[i].localPoint.y/mp.length;				
			}
			
			drawCollisionGraphic(g, avg, 
					col.getFixtureA().m_body.getLinearVelocity(), 
					col.getFixtureB().m_body.getLinearVelocity()
			);
		}
		public abstract void drawCollisionGraphic(
				Graphics2D g,
				Vec2 average_location,
				Vec2 Velocities_of_A,
				Vec2 Velocities_of_B
			);
	}

	public abstract class VelocityGraphic extends GameGraphic{
		private Body body;
		public VelocityGraphic(Body b, int layer){
			super(layer);
			body = b;
		}
		public void draw(Graphics2D g){
			drawVelocity(g, body.getLinearVelocity());
		}
		
		public abstract void drawVelocity(Graphics2D g, Vec2 velocities);

	}

	public abstract class UIGraphic extends GameGraphic{
		boolean change_with_camera;
		public void draw(Graphics2D g) {
			drawUI(g, curCam.position, curCam.angle, curCam.scale);
		}

		public abstract void drawUI(Graphics2D g, Vec2 cam_position, float camera_rotation, Vec2 camera_scale);
	}
	


	
}
