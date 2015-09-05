package logic.game;

public class Schedule {
	
private String[][] schedule;
	
	public Schedule(int players, int cards) {
		int counter;
		
		int rounds = cards / players;
		
		schedule = new String[players][rounds]; 
		
		// Starting / Ending phases
		schedule[0][0] = "start_riddler";
		schedule[0][rounds - 1] = "end";
		for (int i = 1; i < players; i++) {
			schedule[i][0] = "start";
			schedule[i][rounds - 1] = "end"; 
		}
		
		// Normal phase
		for (int i = 0; i < players; i++) {
			// (0 * n + p) (1 * n + p) (2 * n + p) (3 * n + p)... 
			counter = 0;
			
			if (i == 0) counter = 1;
			
			for (int j = 1; j < rounds; j++) {
				if ( j == counter * players + i ) {
					schedule[i][j] = "riddler";
					counter++;
				}
				else {
					schedule[i][j] = "guessing";
				}
			}
		}
	}
	
	public String[][] getSchedule() {
		return schedule;
	}
}