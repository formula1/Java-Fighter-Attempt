package clash;

import effect_abstracts.Effect;

public class ClashPriorityEffect extends Effect{
	public int initaction = 4;
	public int strength = 1;

	public ClashPriorityEffect() {
		super("isattacking", -1);
		// TODO Auto-generated constructor stub
	}
	
	public ClashPriorityEffect(int initial_pulse, int push_strength) {
		super("isattacking", -1);
		initaction = initial_pulse;
		strength = push_strength;
		// TODO Auto-generated constructor stub
	}



	@Override
	public Object activate(String effect, Object initialvalue) {
		int f = (Integer)initialvalue;
		if(effect == "clashinit"){
			f += initaction;
		}
		if(effect == "clashpush"){
			f *= strength;
		}
		return f;
	}

	@Override
	public void append(Effect newInstance) {
		// TODO Auto-generated method stub
		initaction = ((ClashPriorityEffect)newInstance).initaction;
		strength = ((ClashPriorityEffect)newInstance).strength;
	}

	@Override
	public String[] getListeners() {
		// TODO Auto-generated method stub
		return new String[]{"clashinit|clashpush"};
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 1;
	}		
}
