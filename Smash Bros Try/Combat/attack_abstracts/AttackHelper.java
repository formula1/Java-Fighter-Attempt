package attack_abstracts;

import helpers.Box2dHelper;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

public final class AttackHelper {
	
	public static float density = 1f;
	public static float friction = 0f;
	
	
	private final static float EIGHTH = (float)Math.PI/4f;
	private final static float QUARTER = (float)Math.PI/2f;
	
	public static Shape[] get8WayShapes(Shape facingrightshape){
		Shape[] dir8 = new Shape[8];
		dir8[0] = facingrightshape;
		if(facingrightshape.getType() == ShapeType.POLYGON){
			PolygonShape retype = (PolygonShape)facingrightshape;
			Vec2[] verts = new Vec2[retype.getVertexCount()];
			verts = retype.getVertices();
			float[] angs = new float[verts.length];
			float[] lengths = new float[verts.length];
			for(int i=0;i<verts.length;i++){
				angs[i] = (float)Math.atan2(verts[i].y,verts[i].x);
				lengths[i] = verts[i].length();
			}
			
			for(int i=1;i<8;i++){
				PolygonShape temp = new PolygonShape();
				for(int j=0;j<verts.length;j++){
					verts[j] = new Vec2((float)Math.cos(angs[j]+EIGHTH*i),(float)Math.sin(angs[j]+EIGHTH*i)).mul(lengths[j]);
				}
				temp.set(verts, verts.length);
				dir8[i] = temp;
			}
		}else if(facingrightshape.getType() == ShapeType.CIRCLE){
			CircleShape retype = (CircleShape)facingrightshape;
			Vec2 midpoint = retype.m_p;
			float radius = retype.getRadius();
			float initang = (float)Math.atan2(midpoint.y, midpoint.x);
			float dis = midpoint.length();
			
			for(int i=1;i<8;i++){
				CircleShape temp = new CircleShape();
				temp.setRadius(radius);
				temp.m_p.set(new Vec2((float)Math.cos(initang+EIGHTH*i),(float)Math.sin(initang+EIGHTH*i)).mul(dis));
				dir8[i] = temp;
			}
			
		}
		return dir8;
		
	}
	
	public static Shape[] get4WayShapes(Shape facingrightshape){
		Shape[] dir4 = new Shape[4];
		dir4[0] = facingrightshape;
		if(facingrightshape.getType() == ShapeType.POLYGON){
			PolygonShape retype = (PolygonShape)facingrightshape;
			Vec2[] verts = new Vec2[retype.getVertexCount()];
			verts = retype.getVertices();
			float[] angs = new float[verts.length];
			float[] lengths = new float[verts.length];
			for(int i=0;i<verts.length;i++){
				angs[i] = (float)Math.atan2(verts[i].y,verts[i].x);
				lengths[i] = verts[i].length();
			}
			
			for(int i=1;i<8;i++){
				PolygonShape temp = new PolygonShape();
				for(int j=0;j<verts.length;j++){
					verts[j] = new Vec2((float)Math.cos(angs[j]+QUARTER*i),(float)Math.sin(angs[j]+QUARTER*i)).mul(lengths[j]);
				}
				temp.set(verts, verts.length);
				dir4[i] = temp;
			}
		}else if(facingrightshape.getType() == ShapeType.CIRCLE){
			CircleShape retype = (CircleShape)facingrightshape;
			Vec2 midpoint = retype.m_p;
			float radius = retype.getRadius();
			float initang = (float)Math.atan2(midpoint.y, midpoint.x);
			float dis = midpoint.length();
			
			for(int i=1;i<8;i++){
				CircleShape temp = new CircleShape();
				temp.setRadius(radius);
				temp.m_p.set(new Vec2((float)Math.cos(initang+QUARTER*i),(float)Math.sin(initang+QUARTER*i)).mul(dis));
				dir4[i] = temp;
			}
			
		}
		return dir4;
	}
	
	
	public void applyImpact(Contact contact, Vec2 impact){
		//I need impact
	}

}
