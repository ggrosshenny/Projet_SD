/**
* Class : JoueurCoop
* Extend : Thread
* Desc : Class to modelize the actions of player (taking ressource from
*        produceur, watch everything, etc... ). Used in a thread from the
*        server part of player (i.e. this is one variant of the client part of player).
**/

import java.util.Random;
import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;

public class JoueurCoop implements Runnable
{

  // Attributes
  private String id;              // Id of the player who created the thread
  private Ressource[] stock;      // Stock of the player
  private String[][] prod;        // Addresses of producers ([<type of ressource produced>][<producer>])
  private String[] players;       // Addresses of all players in the game
  private int amountToTake;       // Amount to take each time the player wants to take ressource's units from a producer
  private Random rand;            // Used to create random int
  private ICoordinateur coord;    // Coordinateur object
  private JoueurImpl playerServ;  // Server part of the player
  private String coordAddr;       // Addr of coordinateur
  private boolean running;        // Boolean to know if the thread should stop or not
  private boolean isPunnished;    // Boolean to know if the player was caught during a steal or not
  private boolean isWatcher;      // Boolean to know if the player is in observation mode
  public Toolkit toolkit;         // Toolkit used by TimerTask
  public Timer timer;             // Timer used to shedule the task

  // methods

  public JoueurCoop(JoueurImpl playerServ0, String id0, String coord0, int amountToTake0)
  {
    this.id = id0;
    this.amountToTake = amountToTake0;
    this.rand = new Random();
    this.running = true;
    this.toolkit = Toolkit.getDefaultToolkit();
    this.timer = new Timer();
    this.coordAddr = coord0;
    this.playerServ = playerServ0;
    this.isPunnished = false;
    this.isWatcher = false;

    try
    {
      this.coord = (ICoordinateur)Naming.lookup(coord0);
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }

  /**
   * Method : takeRessourceFromNewProducer
   * Param : void
   * Desc : Choose a ressource from those that are not already completed
   *        and find a producer. Take a specified amount of ressource from the
   *        specified producer. Add the taken amount to the stock.
   * Return : void
   **/
  public Producteur takeRessourceFromNewProducer()
  {
    int i = 0;
    int j = 0;
    int randomRessourceType = rand.nextInt(stock.length);
    int rscToTake = 0;
    Producteur produ0 = null;

    try
    {
      // seek for a ressource that we need
      for(i=0; i<stock.length; i++)
      {
        j = (randomRessourceType+i)%stock.length;
        if((stock[j] != null) && (!stock[j].amountForVictoryIsReached()))
        {
          rscToTake = j;
        }
      }

      // Seek for a producer of the given ressource type
      int temp = prod[rscToTake].length-1;
      if(temp > 0)
      {
        j=rand.nextInt(temp);
      }
      else
      {
        j=0;
      }

      // Seek for an existing producer
      do {
        if(prod[rscToTake][j] != null)
        {
          produ0 = (Producteur)Naming.lookup(prod[rscToTake][j]);
        }
        j = (j+1)%prod[rscToTake].length;
      } while (produ0 == null);

      stock[rscToTake].addRessource(produ0.takeRsc());
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
    return produ0;
  }


  /**
   * Method : stopClient
   * Param : void
   * Desc : Set the running boolean to false, that will stop the thread in a stable state.
   * Return : void
   **/
  public void stopClient()
  {
   this.running = false;
  }


  /**
   * Method : setProducteursAndPlayersAddresses
   * Param : String, players - list of players addresses
   * Param : String, producers - list of producers addresses
   * Desc : Set the producers and players addresses
   * Return : void
   **/
  public void setProducteursAndPlayersAddresses(String[] players0, String[][] producers0)
  {
   this.players = players0;
   this.prod = producers0;
  }


  /**
   * Method : setStock
   * Param : Ressource[], stock - list of all ressources that the player has to complete
   * Desc : set the stock with Ressource objetcs
   * Return : void
   **/
  public void setStock(Ressource[] stock0)
  {
    this.stock = stock0;
  }


  /**
   * Method : takeRessource
   * Param : void
   * Desc : Take ressource from the already targeted producer
   * Return :
   **/
   public void takeRessource(Producteur produ)
   {
     try
     {
       stock[produ.getTypeOfRsc()].addRessource(produ.takeRsc());
     }
     catch (RemoteException re) { System.out.println(re) ; }
   }


  /**
  * Method : takeRscFromPlayer
  * Param : void
  * Desc : Steal ressource from a player
  * Return : void
  **/
  public void takeRscFromPlayer()
  {
    int i=0;
    int j=0;
    int rscToTake = 0;
    IJoueur tempPlayer;
    int randomRessourceType = this.rand.nextInt(this.stock.length);

    for(i=0; i<stock.length; i++)
    {
      j = (randomRessourceType+i)%stock.length;
      if((stock[j] != null) && (!stock[j].amountForVictoryIsReached()))
      {
        rscToTake = j;
      }
    }

    i = this.rand.nextInt(this.players.length);
    try
    {
      tempPlayer = (IJoueur) Naming.lookup(this.players[i]);
      stock[rscToTake].addRessource(tempPlayer.steal(this.id, rscToTake, 6));
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }

  }


  /**
  * Mathod : punishement
  * param : void
  * Desc : Time penalty for the player. Called when the player tried to steal a player who was in observation.
  *        Wait a specified amount of time.
  * Return : void
  **/
  public void punishement()
  {
    this.isPunnished = true;
  }


  /**
  * Method : rollTheDice
  * Param : int - percentage, chance of success
  * Desc : get a random number between 1 and 100, if the result is less or equal than
  *        the percentage, return true, else false.
  * Return : boolean
  **/
  public boolean rollTheDice(int percentage)
  {
    int res = 1 + this.rand.nextInt(100);
    return (res <= percentage);
  }


  /**
   * Method : run
   * Param : void
   * Desc : Implement the comportement of the player for the ressource seeking,
   *        add its actions to the log and call the central server when all objectives
   *        are completed.
   * Return : void
   **/
  public void run()
  {
    int i = 0;
    boolean finished = false;
    Producteur produ = null;

    // Waiting on starting blocks !
    // Sorry for active waiting...
    try
    {
      coord.PlayerReady(); // Say to the coordinator that we are ready
    }
    catch (RemoteException re) { System.out.println(re) ; }

    try
    {
      while(!coord.playerStart())
      {
      }
    }
    catch (RemoteException re) { System.out.println(re) ; }

    // Starting log system that will give the stock status each 1000 ms
    timer.schedule(new setLogPlayerTask(stock, coordAddr, 1000, this.id),
                   0,        //initial delay
                   10);  //subsequent rate

    System.out.println("Je commence mon travail !");

    while(!finished && running)
    {
      // Verify if we are allowed to play
      if(isPunnished)
      {
        // Wait until we can play again
        try {
            Thread.sleep(20);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        isPunnished = false;
      }

      try
      {
        if(rollTheDice(15))
        {
          takeRscFromPlayer();
          System.out.println("J'ai volé à un joueur !");
        }
        else
        {
          // Seeking for producer and/or taking ressources
          if((produ == null) || (produ.getAmountRsc() < amountToTake) || (stock[produ.getTypeOfRsc()].amountForVictoryIsReached()))
          {
            produ = takeRessourceFromNewProducer();
          }
          else
          {
            takeRessource(produ);
          }
          System.out.println("Valeur de rsc " + " : " + stock[produ.getTypeOfRsc()].getAmount() + "/" + stock[produ.getTypeOfRsc()].getAmountForVictory() + " (" + produ.getID() + " : " + produ.getTypeOfRsc() + ")");
        }
      }
      catch (RemoteException re) { System.out.println(re) ; }

      // Verifying if all objectives are completed
      finished = true;
      for(i=0; i<stock.length; i++)
      {
        if(stock[i] != null)
        {
          finished = finished && stock[i].amountForVictoryIsReached();
        }
      }
    }

    // Stop log system
    timer.cancel();
    timer.purge();

    // If we had completed all objectives
    if(finished)
    {
      try
      {
        finished = this.coord.endGame(this.id);
      }
      catch (RemoteException re) { System.out.println(re) ; }
    }

    // While to verify if we are winner or if we have to wait for another winner message.
    // This activ waiting is not really bad, because its duration will not be longer than a few amount of ms
    while(!finished && running)
    {
    }

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
