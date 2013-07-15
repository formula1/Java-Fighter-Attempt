package abstracts;

import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;

import assets.WH;

public abstract class Engine {

	/*

	The point of this is to make sure every environment can deliver the necessary foundation
	for the rest of the program to run


	*/	
	public abstract void setupControllers();
	
	public abstract void setupRenderer();
	
	public abstract WH getGraphicBounds();
	public abstract Graphics2D getDrawGraphics();
	public abstract void graphicDoneCallback();

	public abstract void setupSound();

}
