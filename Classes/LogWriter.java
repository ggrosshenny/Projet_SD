
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.*;
import java.io.File;

/**
* Class : LogWriter
* Desc : Generate logfiles for each player & producer of the game from ArrayLists given by the coordinator
**/
public class LogWriter {

	//Attributes
	private ArrayList<ArrayList<ArrayList<String>>> players_logs;		// String array containing the logs of every player
	private ArrayList<ArrayList<String>> producers_logs;				// String array containing the logs of every producers
	
	private int[] playersRanking;
	private int[] rscAmountPerPlayer;
	private int timeBetweenTicks;


	//Constructor
	public LogWriter ( ArrayList<ArrayList<ArrayList<String>>> playersLogs, ArrayList<ArrayList<String>> prodLogs, int[] ranking, int[] totalRscAmount, int timer){

			players_logs = new ArrayList<ArrayList<ArrayList<String>>>();
			players_logs = playersLogs;
			producers_logs = new ArrayList<ArrayList<String>>();
			producers_logs = prodLogs;
			
			playersRanking = ranking;
			rscAmountPerPlayer = totalRscAmount;

			timeBetweenTicks = timer;
			System.out.println(prodLogs.size());

	}

   /**
   * Method : writeLogFiles
   * Param : void
   * Desc : This method is called to generate every logfiles related to the game when it ends.
   * Return : void
   **/
	public void writeLogFiles(){

		PrintWriter logfile;
		int idx = 0;
		boolean creationDirDone = false;

		// Search for the last game index
		File gameLogDir = new File("../logs/logs_game_" + idx);
		while(gameLogDir.exists()){
			idx++;
			gameLogDir = new File("../logs/logs_game_" + idx);
		}

		// Then try to create a new directory (index + 1)
		try{
			gameLogDir.mkdir();
			creationDirDone = true;
		}
		catch (SecurityException se) { System.out.println(se); }

		if(!creationDirDone){
			System.out.println ("Error : game logs directory cannot be created");
		}
		else{

			// For each player create a new log file
			for(int i = 0; i < players_logs.size(); i++) {
				try {
					logfile = new PrintWriter("log_player_"+(i+1)+".bat", "UTF-8");
					// For each ressource create a block
					for(int j = 0; j < players_logs.get(i).size(); j++){
						logfile.println('"' + "Ressource " + j + '"');

						for(int k = 0; k < players_logs.get(i).get(j).size(); k++){
							// Write a new line in the block with as first column the time elapsed since the beginning
							// of the game and as second column the ressource amount owned by the player at this time
							logfile.println((timeBetweenTicks + timeBetweenTicks*k) + " " + players_logs.get(i).get(j).get(k));
						}
						// Gnuplot requires 2 blank lines separating each blocks
						logfile.println("");
						logfile.println("");
					}
					// Close the logfile & move it to the logs repository
					logfile.close();
					Files.move(Paths.get("../Bin/log_player_"+(i+1)+".bat"), Paths.get("../logs/"+gameLogDir+"/log_player_"+(i+1)+".bat"));

				}
				catch (IOException e) { System.out.println(e); }
				catch (UnsupportedOperationException e) { System.out.println(e); }
			}


			// For each producer create a new log file
			System.out.println("size of producers logs : " + producers_logs.size());
			for(int i = 0; i < producers_logs.size(); i++){
				try {
					logfile = new PrintWriter("log_producer_"+(i+1)+".bat", "UTF-8");
					// Write a new line with as first colum the time elapsed since the beginning of the game and
					// as second column the ressource amount owned by the producer at this time
					logfile.println('"' + "Ressource " + producers_logs.get(i).get(0) + '"');
					for(int j = 1; j < producers_logs.get(i).size(); j++){
						logfile.println((timeBetweenTicks + timeBetweenTicks*j) + "	" + producers_logs.get(i).get(j));
					}
					logfile.close();
					Files.move(Paths.get("../Bin/log_producer_"+(i+1)+".bat"), Paths.get("../logs/"+gameLogDir+"/log_producer_"+(i+1)+".bat"));
				}
				catch (IOException e) { System.out.println(e); }
			}
			
			// Generate the leadboard log file
			try{
				logfile = new PrintWriter("players_ranking.bat", "UTF-8");
				logfile.println("Game leaderboard");
				
				for(int i = 0; i < playersRanking.length; i++){
					logfile.println("Player " + (playersRanking[i]+1) + " - " + rscAmountPerPlayer[playersRanking[i]] + " Ressources owned");
				}
				logfile.close();
				Files.move(Paths.get("../Bin/players_ranking.bat"), Paths.get("../logs/"+gameLogDir+"/players_ranking.bat"));
			}
			catch (IOException e) { System.out.println(e); }
		}
	}
}
