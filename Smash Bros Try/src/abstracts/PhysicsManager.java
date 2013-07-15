package abstracts;

import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.broadphase.BroadPhaseStrategy;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.pooling.IWorldPool;

public class PhysicsManager extends World implements Runnable{

	/*
	 * The point of this class is to be the world
	 * Let the "game" simply be an interface that listens for commands and relays them
	 * not the actual thread
	 * 
	 * The actual threads are here, graphics, sound, inputs
	 * if the game wants to do mini threads, sure. But thats on its poragative
	 * 
	 */
		
		private boolean running = false;
		private float timestep = 1f/60f;
		private int sleepstep = Math.round(1000*timestep);
		private int velIterations=6;
		private int posIterations=2;
		private Game g;
		
		public PhysicsManager() {
			super(new Vec2(0,0));
			// TODO Auto-generated constructor stub
		}
		
		public void setContactListener(Game g){
			super.setContactListener(g);
			this.g = g;
		}

		
		public void empty(){
			running = false;
			Body temp = this.getBodyList();
			do{
				this.destroyBody(temp);
			}while(temp.getNext() != null);
		}
		
		
		public void run(){
			running = true;
			Long curtime;
			while(running){
				curtime = System.currentTimeMillis();
				g.time(curtime);
				step(timestep,velIterations,posIterations);
//				dt.display();
	/*			Body bodies = world.getBodyList();
				int bcount = world.getBodyCount();
				for(int i=0;i<bcount;i++){
					if(bodies.m_type == BodyType.DYNAMIC){
						Vec2 pos = bodies.getPosition();
						float angle = bodies.getAngle();
						Vec2 vels = bodies.getLinearVelocity();
						System.out.println("Position: x="+pos.x+" || y="+pos.y);
//						System.out.println("Angle: "+angle);
//						System.out.println("Velocity: x="+vels.x+" || y="+vels.y);
					}
					bodies = bodies.getNext();
				} */
				try {
					Thread.sleep(sleepstep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			
		public void end(){
			running = false;
		}	
	
}
