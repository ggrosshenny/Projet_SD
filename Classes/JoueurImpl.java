/**
 * Class JoueurImpl
 * Extends class UnicastRemoteObject
 * Implements interface Joueur
 * Is the server part of each player. Implements the methods that can be called by
 *    other players. Thoses methods are always available.
 **/

import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;
import java.net.* ;
import java.rmi.* ;
import java.util.concurrent.CyclicBarrier;


public class JoueurImpl extends Agent implements IJoueur
{
  // Attributes
  private Ressource[] stock;      // Stock of the player
  private String[] players;       // Addresses of all players
  private boolean isTurnByTurn;   // Boolean to know if we are in tur by turn mod
  private boolean isCoop;         // Boolean to know if the player is cooperative or not
  private boolean isWatcher;      // Boolean to know if the player is watching fo other player's actions or not
  private boolean isHuman;        // Boolean to know if the player is human
  private JoueurCommon player;    // Player object of the thread
  private Thread playerClient;    // Thread used by the player to take ressources or watch other player's actions
  private int nb_TypeRsc;         // Number of different types of ressource

  // Methods
  public JoueurImpl(String id0, int type0, String coord, boolean isCoop0, boolean isTbT, boolean isHuman0, int nb_TypeRsc0)
    throws RemoteException
  {
    super(id0, type0, coord);

    int i=0;

    this.isWatcher = false;

    if(isHuman0)
    {
      this.isHuman = isHuman0;
      this.isCoop = false;
      this.isTurnByTurn = true;
    }
    else
    {
      this.isCoop = isCoop0;
      this.isTurnByTurn = isTbT;
    }
    this.nb_TypeRsc = nb_TypeRsc0;

    System.out.println("Joueur créé !");
  }


  /**
   * Method : begin
   * Param : void
   * Desc : Start the thread
   * Return : void
   **/
  public void begin(String[] Joueurs, String[][] Producteurs)
  {
    int i = 0;
    Producteur tempProducer;
    Runnable playerRunnable = null;
    this.stock = new Ressource[this.nb_TypeRsc];
    this.players = Joueurs;
    try
    {
      for(i=0; i<this.nb_TypeRsc; i++)
      {
        if(Producteurs[i][0] != null)
        {
          tempProducer = (Producteur)Naming.lookup(Producteurs[i][0]);
          this.stock[i] = tempProducer.copyRsc();
          this.stock[i].setAmount(0);
        }
      }
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }

    // Create the player client object
    // Create the client part of the player (p2p)
    // Create the player object with the right behaiour
    if(isHuman)
    {
      player = new JoueurHuman(this, id, coordinateur, 3);
      playerRunnable = (JoueurHuman)player;
      player.setProducteursAndPlayersAddresses(Joueurs, Producteurs);
      player.setStock(this.stock);
    }
    else
    {
      if(!isTurnByTurn)
      {
        if(isCoop) // Cooperative player without turn waiting
        {
          player = new JoueurCoop(this, id, coordinateur, 3);
          playerRunnable = (JoueurCoop)player;
          player.setProducteursAndPlayersAddresses(Joueurs, Producteurs);
          player.setStock(this.stock);
        }
        if(!isCoop) // Non-cooperative player without turn waiting
        {
          player = new JoueurIndiv(this, id, coordinateur, 3);
          playerRunnable = (JoueurIndiv)player;
          player.setProducteursAndPlayersAddresses(Joueurs, Producteurs);
          player.setStock(this.stock);
        }
      }
      else
      {
        if(isCoop) // Cooperative player with turn waiting
        {
          player = new JoueurCoopTbT(this, id, coordinateur, 3);
          playerRunnable = (JoueurCoopTbT)player;
          player.setProducteursAndPlayersAddresses(Joueurs, Producteurs);
          player.setStock(this.stock);
        }
        if(!isCoop) // Non-cooperative player with turn waiting
        {
          player = new JoueurIndivTbT(this, id, coordinateur, 3);
          playerRunnable = (JoueurIndivTbT)player;
          player.setProducteursAndPlayersAddresses(Joueurs, Producteurs);
          player.setStock(this.stock);
        }
      }
    }

    // Creating thread for the client part of the player
    playerClient = new Thread(playerRunnable, id + "_threadClient");
    playerClient.start();
  }


  /**
  * Method : turnStart
  * Param : void
  * Desc : Signal received from coordinator. Start the turn of the player client part.
  * Return : void
  **/
  public void turnStart()
  {
    if(playerClient.isAlive() && isTurnByTurn)
    {
      synchronized(this)
      {
        this.notify();
      }
    }
  }


  /**
  * Method : gameIsOver
  * Param : int, winnerID - id of the winner
  * Desc : Stop the client thread and shut down rmi server cleanly
  * Return : void
  **/
  public void gameIsOver(String message, String idWinner)
  {
    int i = 0;
    System.out.println(message);
    if(idWinner != this.id)
    {
      if(playerClient.isAlive())
      {
        player.stopClient();
        // Print the summary of the game
        System.out.println("J'ai pu collecter : ");
        for(i=0; i<stock.length; i++)
        {
          if(stock[i] != null)
          {
            System.out.println("  ressource " + i + " : " + stock[i].getAmount());
          }
        }
      }
    }
  }


  /**
   * Method : steal
   * Param : int, rscType - the kind of ressource that is stolen
   * Param : int, amount - the amount of ressource that is stolen
   * Desc : Method that modelize the moment when the player is stolen by someone
   * Return : int, the stolen amount of the specified ressource
   **/
   public synchronized int steal(String id, int rscType, int amount)
   {
     if((!isWatcher) && (!isHuman))
     {
       if(playerClient.isAlive())
       {
         player.isProtectingFromStealing(3);
         return stock[rscType].rmvRessource(amount);
       }
       return 0;
     }
     else if((!isWatcher) && (isHuman))
     {
       player.sendMessage("Vous vous êtes fait volé !");
       return stock[rscType].rmvRessource(amount);
     }
     else
     {
       if(playerClient.isAlive())
       {
         int idPlayer = Integer.parseInt(id.substring(id.length()-1, id.length()));
         IJoueur playerTryingToSteal;
         try
         {
           playerTryingToSteal = (IJoueur)Naming.lookup(players[idPlayer-1]);
           playerTryingToSteal.punish();
         }
         catch (NotBoundException re) { System.out.println(re) ; }
         catch (RemoteException re) { System.out.println(re) ; }
         catch (MalformedURLException e) { System.out.println(e) ; }
       }
       return 0;
     }
   }


   /**
   * Method : punish
   * Param : void
   * Desc : call punishement on the thread when called by another player
   * Return : void
   **/
   public void punish()
   {
     if(playerClient.isAlive())
     {
       player.punishement();
     }
   }


   /**
   * Method : changeStealPercentage
   * Param : int, newPercentage - new steal percentage
   * Desc : Change the steal percentage on the Thread
   * Return : void
   **/
   public synchronized void changeStealPercentage(int newPercentage)
   {
     player.setStealPercentage(newPercentage);
   }


   /**
   * Method : protectFromStealing
   * Param : Boolean - protectStatus, new value for the isWatcher boolean
   * Desc : Set the boolean isWatcher at the given value
   * Return : void
   **/
   public void protectFromStealing(Boolean protectStatus)
   {
     this.isWatcher = protectStatus;
   }

   /**
    * Method : getRscSum
    * Param : void
    * Desc : return the sum of the amounts of every ressources owned by the player
    * Return : int
    **/
   public int getRscSum(){
	int sum = 0;
	for (int i = 0; i < this.stock.length; i++){
	  sum+= this.stock[i].getAmount();
	}
	return sum;
  }


}
