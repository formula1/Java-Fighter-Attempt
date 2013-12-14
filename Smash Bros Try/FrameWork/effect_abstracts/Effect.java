package effect_abstracts;



public abstract class Effect {
	String tag;
	EffectManager manager;
	int frame;
	
	
	
	public Effect(String tag, int framelength){
		this.tag = tag;
		frame = framelength;
	}
	
	public abstract Object activate(String effect, Object initialvalue);
	
	public abstract void append(Effect newInstance);
	
	public void kill(){
		manager.kill(this);
	}
	
	public abstract String[] getListeners();
	public abstract int getPriority();
}