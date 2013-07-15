package ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jbox2d.common.Vec2;

import abstracts.Engine;
import assets.WH;

public class GameUI extends Engine{
	Canvas dis;
	BufferStrategy strategy;
	JFrame frame;

	public GameUI(){
		JFrame frame = new JFrame("baby steps");
		JPanel jp = (JPanel) frame.getContentPane();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		jp.setPreferredSize(new Dimension(500,500));
		dis = new Canvas();
		dis.setBounds(0,0,500,500);
		jp.add(dis);
		dis.setIgnoreRepaint(true);
		frame.pack();
		frame.setResizable(false);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dis.createBufferStrategy(2);
		strategy = dis.getBufferStrategy();


	}
	
	@Override
	public void setupControllers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setupRenderer() {
		// TODO Auto-generated method stub
		
	}

	
	public WH getGraphicBounds(){
		return new WH(dis.getBounds().width,dis.getBounds().height);
	}
	
	@Override
	public Graphics2D getDrawGraphics() {
		return (Graphics2D)strategy.getDrawGraphics();
	}
	
	public void graphicDoneCallback(){
		strategy.show();
	}

	@Override
	public void setupSound() {
		// TODO Auto-generated method stub
		
	}
	
}
