/**
* Class : JoueurIndiv
* Extend :
* Desc : Class to modelize the actions of player (taking ressource from
*        produceur, watch everything, etc... ). Used in a thread from the
*        server part of player (i.e. this is one variant of the client part of player).
**/

import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.Random;

public class JoueurIndiv extends Thread
{

  // Attributes
  private String id;              // Id of the player who created the thread
  private Ressource[] stock;      // Stock of the player
  private String[][] prod;    
  private String[] players;    // Addresses of producers ([<type of ressource produced>][<producer>])
  private int amountToTake;       // Amount to take each time the player wants to take ressource's units from a producer
  private Random rand;            // Used to create random int
  private Coordinateur coord;     // Coordinateur object
  private boolean running;        // Boolean to know if the thread should stop or not

  // methods

  public JoueurIndiv(String id0, String coord0, int amountToTake0)
  {
    this.id = id0;
    this.amountToTake = amountToTake0;
    this.rand = new Random();
    this.running = true;
    try
    {
      this.coord = (Coordinateur)Naming.lookup(coord0);
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
    Producteur produ0;

    try
    {
      // seek for a ressource that we need
      for(i=randomRessourceType; i==randomRessourceType-1; i=(i+1)%randomRessourceType)
      {
        if(!stock[i].amountForVictoryIsReached())
        {
          rscToTake = i;
          break;
        }
      }

      // Seek for a producer of the given ressource type
      j=rand.nextInt(prod[i].length);
      produ0 = (Producteur)Naming.lookup(prod[i][j]);
      stock[i].addRessource(produ0.takeRsc());
      return produ0;
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
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
     catch (NotBoundException re) { System.out.println(re) ; }
     catch (RemoteException re) { System.out.println(re) ; }
     catch (MalformedURLException e) { System.out.println(e) ; }
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
   * Method : run
   * Param : void
   * Desc : Implement the comportement of the player for the ressource seeking,
   *        add its actions to the log and call the central server when all objectives
   *        are completed.
   * Return : void
   **/
  void run()
  {
    int i = 0;
    boolean finished = false;
    Producteur produ = null;

    while(!finished && running)
    {
      // Seeking for producer and/or taking ressources
      if((produ == null) || (produ.getAmountRsc() <= 0))
      {
        produ = takeRessourceFromNewProducer();
      }
      else
      {
        takeRessource(produ);
      }

      // Verifying if all objectives are completed
      for(i=0; i<stock.length; i++)
      {
        finished = finished && stock[i].amountForVictoryIsReached();
      }

    }
	// Informing the coordinator that every objectives have been completed
	if(finished)
	{
      this.coord.endGame(this.id, this.players, this.prod);
	}
  }
}
