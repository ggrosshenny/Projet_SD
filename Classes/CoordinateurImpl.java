
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
   private int nb_playersReady;
   private int nb_producersEmpty;
   private boolean barrier;
   private int nb_TypeRsc;
   private ArrayList<ArrayList<ArrayList<String>>> gameLog;


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
      this.nb_playersReady = 0;
      this.barrier = false;
			this.Players = new String[nb_players];
			this.Producers = new String[nb_producers][nb_producers];


      // Creation of log ArrayList
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

   public synchronized void PlayerReady()
   {
     this.nb_playersReady++;
     if(this.nb_playersReady == this.nb_players)
     {
       barrier = true;
     }
   }


  public synchronized boolean playerStart()
  {
    return barrier;
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

    this.nb_producersEmpty++;

    if((this.nb_producersEmpty == this.nb_producers) && (!finished))
    {
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
      for(int i = 0; i < this.Players.length; i++) // tell to all players that game is ended
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
			for(int i = 0; i < this.Producers.length; i++) // Tell to all producers that game is ended
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
			return true;
		}
		return false;
	}

}
