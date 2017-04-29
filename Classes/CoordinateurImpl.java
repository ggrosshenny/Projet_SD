
/**
 * Class : CoordinateurImpl
 * Desc : Implementation of the class Coordinateur
 **/

 import java.rmi.*;
 import java.net.MalformedURLException;
 import java.rmi.server.UnicastRemoteObject ;
 import java.util.ArrayList;

 public class CoordinateurImpl extends UnicastRemoteObject implements ICoordinateur
 {
   // Attributes
   public String[] Players;
   public String[][] Producers;

   public int nb_players;
   public int nb_producers;

   private boolean finished;
   private int nb_agentReady;
   private int nb_producersEmpty;
   private boolean barrier;
   private int nb_TypeRsc;
   private int CurrentPlayerPLaying;
   private ArrayList<ArrayList<ArrayList<String>>> gameLog;
   private ArrayList<ArrayList<String>> gameLogProducers;
   private int[][] systemObservation;
   private boolean isTbT;


   public CoordinateurImpl(String[] args)
    throws RemoteException
   {
	   {
       Producteur tempProd = null;
       int i;
       int j = 0;
      this.finished = false;

			this.nb_players = Integer.parseInt(args[2]);
			this.nb_producers = Integer.parseInt(args[3]);
      this.nb_TypeRsc = Integer.parseInt(args[args.length-1]);
      this.systemObservation = new int[this.nb_players][this.nb_TypeRsc];
      if(Integer.parseInt(args[args.length-2]) == 0)
      {
        this.isTbT = true;
      }
      else
      {
        this.isTbT = false;
      }
      this.nb_agentReady = 0;
      this.CurrentPlayerPLaying = 0;
      this.barrier = false;
			this.Players = new String[nb_players];
			this.Producers = new String[nb_TypeRsc][nb_producers];


      // Creation of players log ArrayList
      this.gameLog = new ArrayList<ArrayList<ArrayList<String>>>(); // ArrayList 3D for the game logs
      ArrayList<ArrayList<String>> playerLog;                       // Logs of each player
      ArrayList<String> logPerRsc;                                  // Logs of each ressource for each player
      for(i=0; i<this.nb_players; i++)
      {
        playerLog = new ArrayList<ArrayList<String>>(); // Create logs of Player_i
        for(j=0; j<this.nb_TypeRsc; j++)
        {
          logPerRsc = new ArrayList<String>(); // Create logs of Ressource_j for Player_i
          playerLog.add(logPerRsc);
        }
        this.gameLog.add(playerLog);
      }

      // Creation of producers log ArrayList (idem)
      this.gameLogProducers = new ArrayList<ArrayList<String>>();
      ArrayList<String> logPerRscProducer;
	  for(i = 0; i < this.nb_producers; i++)
	  {
		logPerRscProducer = new ArrayList<String>();
		this.gameLogProducers.add(logPerRscProducer);
	  }

      j=0;
      for(i = 0; i < this.nb_players; i++){
			  this.Players[i] = "rmi://" + args[0] + ":" + args[1] + "/" + args[4 + i];
			}
			for(i = 0; i < this.nb_producers; i++){
				try {
					//System.out.println("avant : " + "rmi://" + args[0] + ":" + args[1] + "/" + args[4+nb_players+i]);
					tempProd = (Producteur) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[4 + this.nb_players + i]);
					//System.out.println("avant : " + Producteurs[tempProd.getTypeOfRsc()].length + " , " );
					while(this.Producers[tempProd.getTypeOfRsc()][j] != null){
						j++;
					}
					this.Producers[tempProd.getTypeOfRsc()][j] = "rmi://" + args[0] + ":" + args[1] + "/" + args[4 + this.nb_players + i];
					//System.out.println("Apres");
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }

			}
	   }
   }


   /**
   * Method : AgentReady
   * Param : void
   * Desc : Called by all agent at the beginning of the game and launch the game
   *        when all agents are called the method.
   * Return : void
   **/
   public synchronized void AgentReady()
   {
     IJoueur tempPlayer;
     Producteur tempProd;
     int i = 0;
     int j = 0;

     // Increment the number of agent that are ready
     this.nb_agentReady++;

     // If all agents are ready
     if(this.nb_agentReady == this.nb_players)
     {

       for(i = 0; i < this.Producers.length; i++) // Tell to all producers that game is ended
       {
 				for(j = 0; j < this.Producers[i].length; j++)
         {
 					try
           {
 						if(Producers[i][j] != null)
             {
 							tempProd = (Producteur)Naming.lookup(this.Producers[i][j]);
 						 	tempProd.begin();
 						}
 					}
 					catch (NotBoundException re) { System.out.println(re) ; }
 					catch (RemoteException re) { System.out.println(re) ; }
 					catch (MalformedURLException e) { System.out.println(e) ; }
 				}
 			}

       barrier = true;
       try
       {
         tempPlayer = (IJoueur)Naming.lookup(Players[0]);
         tempPlayer.turnStart();
       }
       catch (NotBoundException re) { System.out.println(re) ; }
       catch (RemoteException re) { System.out.println(re) ; }
       catch (MalformedURLException e) { System.out.println(e) ; }
     }
   }


  public synchronized boolean playerStart()
  {
    return barrier;
  }


  /**
  * Method : endOfTurn
  * Param : String, id - Id of the player sending the message
  * Desc : When receiving a message from a Player who end his turn, signal to
  *        the next one to begin his turn.
  * Return : void
  **/
  public synchronized void endOfTurn(String id)
  {
    String nextPlayerAddr;
    IJoueur tempPlayer;
    // Getting player number
    int playerNumber = Integer.parseInt(id.substring(id.length()-1, id.length()));;

    // Verifying the number of the player to identify the next player to notify
    if((playerNumber-1) == CurrentPlayerPLaying)
    {
      CurrentPlayerPLaying = (CurrentPlayerPLaying + 1)%this.nb_players;
      nextPlayerAddr = Players[CurrentPlayerPLaying];
      try
      {
        tempPlayer = (IJoueur)Naming.lookup(nextPlayerAddr);
        tempPlayer.turnStart();
      }
      catch (NotBoundException re) { System.out.println(re) ; }
      catch (RemoteException re) { System.out.println(re) ; }
      catch (MalformedURLException e) { System.out.println(e) ; }
    }
  }



  /**
   * Method : ProducerEmpty
   * Param : void
   * Desc : This method is called oach time a producer is empty. If all producers had called this
   *        method, then the coordinateur verify that no player has completed all its
   *        objectives and notify to every agent that the game is ended.
   * Return : void
   **/
  public synchronized void ProducerEmpty()
  {
    IJoueur tempJoueur;
		Producteur tempProd;
    String message;
    int i;
    int test = 0;

    this.nb_producersEmpty++;

    if((this.nb_producersEmpty == this.nb_producers) && (!finished))
    {
      System.out.println("All producers are empty !");
      for(i=0; i<Players.length; i++)
      {
        try
        {
					tempJoueur = (IJoueur)Naming.lookup(this.Players[i]);
					tempJoueur.changeStealPercentage(101);
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
      }

      // Wait for a player notification
      try {
          Thread.sleep(200);
      } catch(InterruptedException ex) {
          Thread.currentThread().interrupt();
      }

      // If after this time a player won, cancel the action.
      if(finished)
      {
        return;
      }

      // Else notify everyone that the game is ended and the reason why.
      finished = true;
      message = new String("All producers are empty. The game end without winner.");
      for(i = 0; i < this.Players.length; i++) // tell to all players that game is ended
      {
				try
        {
					tempJoueur = (IJoueur)Naming.lookup(this.Players[i]);
					tempJoueur.gameIsOver(message, "none");
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }

			}
			for(i = 0; i < this.Producers.length; i++) // Tell to all producers that game is ended
      {
				for(int j = 0; j < this.Producers[i].length; j++)
        {
					try
          {
						if(Producers[i][j] != null)
            {
							tempProd = (Producteur)Naming.lookup(this.Producers[i][j]);
						 	tempProd.gameIsOver(message);
						}
					}
					catch (NotBoundException re) { System.out.println(re) ; }
					catch (RemoteException re) { System.out.println(re) ; }
					catch (MalformedURLException e) { System.out.println(e) ; }
				}
			}
      // Write logs in files
      LogWriter logEntity;
      if(this.isTbT)
      {
        logEntity = new LogWriter(this.gameLog, this.gameLogProducers, 1);
      }
      else
      {
        logEntity = new LogWriter(this.gameLog, this.gameLogProducers, 10);
      }
			logEntity.writeLogFiles();
    }
  }


  /**
   * Method : setLog
   * Param : String, id - the id of the player calling the Method
   * Param : int[], stock - the stock status of the player
   * Desc : Stock the log on the arrayList 3D
   * Return : void
   **/
   public synchronized void setLog(String id, int[] stock)
   {
     int i = 0;
     String valRsc;
     int idPlayer = Integer.parseInt(id.substring(id.length()-1, id.length()));
     for(i=0; i<stock.length; i++)
     {
       valRsc = Integer.toString(stock[i]);
       this.gameLog.get(idPlayer-1).get(i).add(valRsc);
     }
   }

   /**
   * Method : setLog
   * Param : String, id - the id of the producer calling the Method
   * Param : int, stock - the stock status of the producer
   * Desc : Stock the log on the arrayList 2D
   * Return : void
   **/
   public synchronized void setLogProd(String id, int stock)
   {
     String valRsc;
     int idProducer = Integer.parseInt(id.substring(id.length()-1, id.length()));
     valRsc = Integer.toString(stock);
     this.gameLogProducers.get(idProducer-1).add(valRsc);
   }


  /**
  * Method : observeSystem
  * Param : void
  * Desc : Return the last logs value for the given ressource type
  * Return : int[playerId][ressourceValue]
  **/
  public synchronized int[][] observeSystem()
  {
    int i = 0;
    int j = 0;
    int index = 0;

    // transfert of las logs of the given ressource type
    for(i=0; i<this.nb_players; i++)
    {
      for(j=0; j<this.nb_TypeRsc; j++)
      {
        // Get the last value of logs of each players for each ressource type
        if((index = this.gameLog.get(i).get(j).size()-1) < 0)
        {
          systemObservation[i][j] = 0;
        }
        else
        {
          systemObservation[i][j] = Integer.parseInt(this.gameLog.get(i).get(j).get(index));
        }
      }
    }
    return systemObservation;
  }


	/**
	 * Method : endGame
	 * Param : String, idJoueur - the id of the player who just won the game & called the method
	 * Desc : Messaging every players & producers that the game is over, then starts the generation of the log files.
	 * Return : boolean
	 **/
   public synchronized boolean endGame(String idJoueur){

		IJoueur tempJoueur;
		Producteur tempProd;
		if(finished == false){
			finished = true;
      System.out.println("La partie est finie.");
      String message = new String("The game is over, the winner is : " + idJoueur);
			for(int i = 0; i < this.Players.length; i++){

				try {
					tempJoueur = (IJoueur)Naming.lookup(this.Players[i]);
					tempJoueur.gameIsOver(message, idJoueur);
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }

			}
			for(int i = 0; i < this.Producers.length; i++){
				for(int j = 0; j < this.Producers[i].length; j++){

					try {
						if(Producers[i][j] != null){
							tempProd = (Producteur)Naming.lookup(this.Producers[i][j]);
						 	tempProd.gameIsOver(message);
						}
					}
					catch (NotBoundException re) { System.out.println(re) ; }
					catch (RemoteException re) { System.out.println(re) ; }
					catch (MalformedURLException e) { System.out.println(e) ; }
				}
			}

      // Write logs in files
      LogWriter logEntity;
      if(this.isTbT)
      {
        logEntity = new LogWriter(this.gameLog, this.gameLogProducers, 1);
      }
      else
      {
        logEntity = new LogWriter(this.gameLog, this.gameLogProducers, 10);
      }
			logEntity.writeLogFiles();
			return true;
		}
		return false;
	}

}
