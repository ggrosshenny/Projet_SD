
/**
 * Class : CoordinateurImpl
 * Desc : Implementation of the class Coordinateur
 **/

 import java.rmi.*;
 import java.net.MalformedURLException;
 import java.rmi.server.UnicastRemoteObject ;

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

   public CoordinateurImpl(String[] args)
    throws RemoteException
   {
	   {
      this.finished = false;

			this.nb_players = Integer.parseInt(args[2]);
			this.nb_producers = Integer.parseInt(args[3]);
      this.nb_playersReady = 0;
      this.barrier = false;

			this.Players = new String[nb_players];
			this.Producers = new String[nb_producers][nb_producers];

			Producteur tempProd = null;
      int i;
      int j = 0;
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
          Thread.sleep(1000);
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


   public synchronized boolean endGame(String idJoueur){

		IJoueur tempJoueur;
		Producteur tempProd;
		if(finished == false){
			finished = true;
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
