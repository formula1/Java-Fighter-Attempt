package clash;

import game.BS_to_Game;
import game.CurrentDefaultRenderer;
import graphic.GameGraphic;
import helpers.Box2dHelper;
import helpers.Ownership;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;


import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import pause.PauseInstance;
import thinkers.MinionEntity;

import abstracts.Game;
import attack_abstracts.AttackBox;

/*
 * Set one party is set to negative and the other to positive
 * 
 * get Threshold of losing
 * 
 * one person starts off winning
 * -speed
 * 
 * 3,2,1 clash starts
 * 
 * Take in the spamming of both, multiply it by strength,
 * -spam for players only works when it goes off then on
 * 
 * Take velocity of both and add that to the balance
 * 
 * 
 * 
 * This isn't between two people though
 * This is between many people
 * Also, I want to make sure the 
 * 
 * 
 * I need to be able to allow 2 vs 1, 1 vs 1 and 1 vs 1 vs 1
 * And a more advanced chain thats 2 vs 1 vs 2 vs 2
 * 
 * Each individual has a clash parameter
 * -number of opponents
 * -So with each individual clash, the value is divided by the number of opponents
 * 
 * 
 * 
 */
	
public class Clash{
	float currentlywinning = 0;
	int iteration=0;
	public Vec2 contactpoint;
	public ClashManager A;
	public ClashManager B;
	public int startframe;
	Body clashCircle;
	PauseInstance pause;
	
	public Clash(MinionEntity A, MinionEntity B, Contact contact, PauseInstance pause){
		this.pause = pause;
		System.out.println("CLASH!!!!");
		Manifold manny = contact.getManifold();
		Vec2 avg = new Vec2();
		
		System.out.print(manny.points.length);
		
		for(int i=0;i<manny.points.length;i++){
			System.out.println("Manny "+i+": "+manny.points[i].localPoint.toString());
			avg.add(manny.points[i].localPoint.mul(1/manny.points.length));
		}
		
		contactpoint = A.getCenter().add(avg);

		this.A = A.clash;
		this.B = B.clash;
		startframe = BS_to_Game.current_frame;
		this.A.enterClash(this, (AttackBox)((Ownership)contact.m_fixtureA.getUserData()).manager, (String)((Ownership)contact.getFixtureA().getUserData()).stored_info);
		this.B.enterClash(this, (AttackBox)((Ownership)contact.m_fixtureB.getUserData()).manager, (String)((Ownership)contact.getFixtureA().getUserData()).stored_info);

		
		currentlywinning = A.clash.getInit(contactpoint) - B.clash.getInit(contactpoint);
		
		
		
	}
	
	public void applyEffects(Contact contact, String fixture_name,
			boolean is_fix_a, Ownership other){
		Fixture p_bod = (is_fix_a)?contact.m_fixtureA:contact.m_fixtureB;
		Fixture n_bod = (is_fix_a)?contact.m_fixtureB:contact.m_fixtureA;
		
		
		if(	A.owner.getCenter().add(n_bod.m_body.getWorldCenter().mul(-1)).length()
			<
			B.owner.getCenter().add(n_bod.m_body.getWorldCenter().mul(-1)).length()
		)	
			A.attack.beginContact(contact,is_fix_a);
		else
			B.attack.beginContact(contact, is_fix_a);
	}

	
	public void setWinner(ClashManager winner, ClashManager loser){
		endClash();
		loser.loseClash(winner, this); 
		winner.winClash(loser, this);
	}
	public void setLoser(ClashManager loser){
		endClash();
		ClashManager winner = (A == loser)?B:A;
		loser.loseClash(winner, this); 
		winner.winClash(loser, this);
	}

	
	
	public void time(int frame_num){
				
		if(-1*A.threshhold > currentlywinning){ 
			setWinner(B,A);
			return;
		}
		else if(B.threshhold < currentlywinning){ setWinner(A,B); return;}
		
		if((BS_to_Game.current_frame - startframe)%30 == 1){
			float a = A.getPush(contactpoint,iteration);
			float b = B.getPush(contactpoint,iteration);
			iteration++;
			System.out.println("Action A: "+a+", Action B: "+b);
			currentlywinning += a - b;
			System.out.println("currentlywinning: "+currentlywinning);
		}
		
		CurrentDefaultRenderer.addGraphic(new GameGraphic(10,true){

			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub
				g.setColor(new Color(0xFFFFFF));
				g.drawLine((int)A.owner.getCenter().x, (int)A.owner.getCenter().y,
						(int)B.owner.getCenter().x, (int)B.owner.getCenter().y);
				g.setColor(new Color(0xFF4400));
				
				Vec2 diff = A.owner.getCenter().add(B.owner.getCenter().mul(-1));
				float m = diff.y/diff.x;
				float length = diff.length();
				
				float seg = (currentlywinning+A.threshhold)*length/(B.threshhold+A.threshhold);
				
				float x = A.owner.getCenter().x + seg/(float)Math.sqrt(1+Math.pow(m, 2));
				
				float y = m*(x - A.owner.getCenter().x )+A.owner.getCenter().y;
				
//				float angle = (float)Math.atan2(diff.y, diff.x);
				
//				Vec2 offset = A.owner.getCenter().add(new Vec2((float)Math.cos(angle)*seg,(float)Math.sin(angle)*seg));

				
				g.fillOval(Math.round(x), Math.round(y), 4, 4);
				
				String ret = "0";
/*				if(currentlywinning > 0) ret = String.valueOf(currentlywinning/B.threshhold);
				else ret = String.valueOf(currentlywinning/A.threshhold);
				
				g.drawString(String.valueOf(ret), contactpoint.x, contactpoint.y);
				*/
			}
			
		});


		
	}
	
	
	public void endClash(){
//		Game.world.destroyBody(clashCircle);
		
		pause.unpause();
		System.out.println("pausetime:"+pause.time());
		startframe = -1;
		A.action = 0;
		B.action = 0;
	}

	
}
