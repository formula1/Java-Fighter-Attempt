package ui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import abstracts.Engine;
import abstracts.GameRenderer;
import abstracts.PhysicsManager;

import physics.DebugPhysicsRender;

import controller.Player;

import jinput_manager.JinputPanel;

import game.BS_to_Game;
import game.BabySteps;
import game.CurrentDefaultRenderer;


public class TheMain{
	static GameRenderer dt;
	static Player[] p;
	static BS_to_Game game;
	static PhysicsManager pm;
	static GameUI ui;

	public static void main(String[] args){
		final JFrame frame = new JFrame("baby steps");
		
		JButton cont = new JButton("Continue");

		/*
		 * Give people the choice of controller
		 * Have the person setup the buttons
		 * -disable continue until its all been completed
		 * After completed, allow person to press continue
		 * On continue, everything starts
		 * 
		 * 
		 * 
		 */
		
		
		final JPanel jp = (JPanel) frame.getContentPane();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		jp.setPreferredSize(new Dimension(500,500));
//		jp.setLayout(null);
		
		JinputPanel jip = new JinputPanel(1);
		jp.add(jip);
		cont.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JinputPanel jip = (JinputPanel) jp.getComponent(0);
				p = jip.getPlayers();
				try {
					jip.end();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jp.removeAll();
				frame.setVisible(false); //you can't see me!
				frame.dispose(); //Destroy the JFrame object
				nextFrame("meh");
			}
		});
		jp.add(cont);
		
		frame.pack();
		frame.setResizable(false);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}//End Main Method
	
	public static void nextFrame(String frameName){

		ui = new GameUI();
		
		
		pm = new PhysicsManager();
		game = new BS_to_Game(pm, p);
		pm.setContactListener(game);
		dt = new GameRenderer(ui,game,new CurrentDefaultRenderer(pm));

		Thread m = new Thread(dt);
		Thread n = new Thread(pm);
		Thread[] players = new Thread[p.length];
		for(int i=0;i<p.length;i++){
			p[i].addListener(game);
			players[i] = new Thread(p[i]);
		}
		m.start();
		n.start();
		for(int i=0;i<p.length;i++)
			players[i].start();
//		dt.end();
//		bs.end();
	

	}

	
}//End Class


/*

The way they have this setup is...
1) You have various menus and etc
2) You have


Renderer
-Finding current center
	-Get all Shapes within x*zoom width and y*zoom height
	-Use renderer of games choice to render it

-The Game
	-Menus
	-Videos/Animations
	-SpriteSheets
	-Sounds
	-Scenarios
		-Initialize Scenario
		-Player actions
		-End Scenario



The Way I want to set it up
-Graphic Manager-Sends Graphics to OS Manager
-Sound Manager
-Player Manager
-OS Manager-Displays everything appropiately

The Reality
-Creating a Graphic manager slows the system down by creating the picture before hand and then redrawing it
--Also makes system more resource intensive in that I will send the instance to another thread as a copy and not
	as "the" picture. I could make it static though right? still... Maybe I'm not sure
-Java sound is difficult to use
-Jinput is difficult to use, sockets aren't that simple
-OS Manager really is just graphic display at least in terms of if its windowed or not
--Though it might be useful in terms of knowing whether its Android right?... Not sure I know enough

Regardless...
Baby Steps....

Get Box 2d displayed
-Be able to manipulate box2d
--It will change live graphicly



*/