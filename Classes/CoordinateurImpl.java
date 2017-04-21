
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


   public synchronized boolean endGame(String idJoueur){

		IJoueur tempJoueur;
		Producteur tempProd;
		if(finished == false){
			finished = true;
			for(int i = 0; i < this.Players.length; i++){

				try {
					tempJoueur = (IJoueur)Naming.lookup(this.Players[i]);
					tempJoueur.gameIsOver(idJoueur);
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
						 	tempProd.gameIsOver(idJoueur);
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
