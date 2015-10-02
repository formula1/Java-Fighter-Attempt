package ai;

public class CommandManager {
	int current_player_state;
	/*
	 * 
	 * For each command I need to check for....
	 * 
	 * -a button is pressed then letgo
	 * -a button is held
	 * -check within x ticks
	 * 
	 * 
	 */
	
	
	public void buttonListener(){
		
	}
	
	public void time(int frame_num){
		
	}
	
	private abstract class Command{
		
		public Command(int number_of_ticks){
			
		}
		
		public abstract boolean checkConditions();

		public void setState(){
			
		}
		
	}
	
}
