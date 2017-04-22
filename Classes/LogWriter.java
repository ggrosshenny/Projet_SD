
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogWriter {
	
	//Attributes
	private ArrayList<String>[][][] players_logs;		// String array containing the logs of every player
	private ArrayList<String>[][] producers_logs;	// String array containing the logs of every producers
	private int timeBetweenTicks;
	
	
	//Constructor
	public LogWriter ( ArrayList<String>[][][] playersLogs, ArrayList<String>[][] prodLogs, int timer){
	
			players_logs = playersLogs;
			producers_logs = prodLogs;
		
	}
	
	public void writeLogFiles(){
		
		PrintWriter logfile;
		
		// For each player create a new log file
		for(int i = 0; i < players_logs.length; i++) {			
			try {				
				logfile = new PrintWriter("log_player_"+i, "UTF-8");
				// For each ressource create a block
				for(int j = 0; j < players_logs[i].length; j++){
					logfile.println("# block rsc " + j + " :");
					
					for(int k = 0; k < players_logs[i][j].length; k++){
						// Write a new line in the block with as first column the time elapsed since the beginning
						// of the game and as second column the ressource amount owned by the player at this time
						logfile.println((timeBetweenTicks + timeBetweenTicks*k) + "		" + players_logs[i][j][k]);
					}
					// Gnuplot requires 2 blank lines separating each blocks
					logfile.println("");
					logfile.println("");
				}
			}
			catch (IOException e) { System.out.println(e); }
		}
		
		// For each producer create a new log file
		for(int i = 0; i < producers_logs.length; i++){
			try {
				logfile = new PrintWriter("log_producer_"+i, "UTF-8");
				// Write a new line with as first colum the time elapsed since the beginning of the game and
				// as second column the ressource amount owned by the producer at this time
				for(int j = 0; j < producers_logs[i].length; j++){
					logfile.println((timeBetweenTicks + timeBetweenTicks*j) + "		" + producers_logs[i][j]);
				}
			}
			catch (IOException e) { System.out.println(e); }
		}		
	}
}
