package attack_abstracts;

import thinkers.MinionEntity;

public abstract class AttackManager {
	/*
	 * What is the point of the attack manager?
	 * 
	 * to let you know what can happen next
	 * to let you know when no other actions can happen
	 * 
	 * 
	 * 
	 * 
	 */
	boolean inattack = false;
	int attackframelength =0;
	public AttackState currentAttack;
	MinionEntity owner;
	
	public void time(int framenum){
		if(attackframelength > 0) attackframelength--;
	}
	
	
	
	
	
}
