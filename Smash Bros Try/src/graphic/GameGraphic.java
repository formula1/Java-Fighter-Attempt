package graphic;

import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;

public abstract class GameGraphic {
	public int layer;
	public GameGraphic(){
		layer = 0;
	}
	public GameGraphic(int layer){
		this.layer = layer;
	}
	
	public abstract void draw(Graphics2D g);

}
