// Class JoueurCommon


import java.util.Random;
import java.rmi.* ;
import java.net.MalformedURLException ;

public class JoueurCommon
{
  // Attributes
  protected String id;                      // Id of the player who created the thread
  protected Ressource[] stock;              // Stock of the player
  protected String[][] prod;                // Addresses of producers ([<type of ressource produced>][<producer>])
  protected String[] players;               // Addresses of all players in the game
  protected int[][] observationResult;          // tab to stock the observation of the system
  protected int amountToTake;               // Amount to take each time the player wants to take ressource's units from a producer
  protected int stealPercentage;            // percentage of chance to steal
  protected Random rand;                    // Used to create random int
  protected ICoordinateur coord;            // Coordinateur object
  protected JoueurImpl playerServ;          // Server part of the player
  protected String coordAddr;               // Addr of coordinateur
  protected boolean running;                // Boolean to know if the thread should stop or not
  protected boolean isPunnished;            // Boolean to know if the player was caught during a steal or not
  protected boolean wasUnderProtection;     // Boolean to know if we were protecting from stealing or not
  protected int isWatchingTime;             // int to know if the player is in observation mode

  // Methods


  public JoueurCommon(JoueurImpl playerServ0, String id0, String coord0, int amountToTake0)
  {
    this.id = id0;
    this.amountToTake = amountToTake0;
    this.rand = new Random();
    this.running = true;
    this.coordAddr = coord0;
    this.playerServ = playerServ0;
    this.isPunnished = false;
    this.isWatchingTime = 0;
    this.wasUnderProtection = false;
    this.stealPercentage = 5;

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
  public Producteur takeRessourceFromNewProducer(int rscToTake)
  {
    int i = 0;
    int j = 0;
    Producteur produ0 = null;

    if(!running)
    {
      return produ0;
    }

    try
    {
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
  * Method : setStealPrecentage
  * Param : int, newPercentage - new percentage
  * Desc : set the new percentage
  * Return : void
  **/
  public synchronized void setStealPercentage(int newPercentage)
  {
    this.stealPercentage = newPercentage;
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
    this.observationResult = new int[this.players.length][this.stock.length];
  }


  /**
   * Method : takeRessource
   * Param : void
   * Desc : Take ressource from the already targeted producer
   * Return :
   **/
   public void takeRessource(Producteur produ)
   {
     if(!running)
     {
       return;
     }
     try
     {
       stock[produ.getTypeOfRsc()].addRessource(produ.takeRsc());
     }
     catch (RemoteException re) { System.out.println(re) ; }
   }


   /**
   * Method : chooseRscType
   * Param : void
   * Desc : return a random ressource type.
   * Return : int, the ressource type selected
   **/
   public int chooseRscType()
   {
     int i=0;
     int j=0;
     int rscToTake = 0;
     int randomRessourceType = this.rand.nextInt(this.stock.length);

     // Choose a random ressource that is not already aquired
     for(i=0; i<stock.length; i++)
     {
       j = (randomRessourceType+i)%stock.length;
       if((stock[j] != null) && (!stock[j].amountForVictoryIsReached()))
       {
         if(rscToTake == 0)
         {
           rscToTake = j;
         }
       }
     }

     return rscToTake;
   }


  /**
  * Method : takeRscFromPlayer
  * Param : void
  * Desc : Steal ressource from a player
  * Return : void
  **/
  public void takeRscFromPlayer(String playerId, int rscType)
  {
    int i=0;
    IJoueur tempPlayer;

    if(!running)
    {
      return;
    }

    if((playerId == null) || (playerId.length() == 0))
    {
      i = this.rand.nextInt(this.players.length);
    }
    else
    {
      i = Integer.parseInt(playerId.substring(playerId.length()-1, playerId.length()));
      i--;
    }

    try
    {
      tempPlayer = (IJoueur) Naming.lookup(this.players[i]);
      stock[rscType].addRessource(tempPlayer.steal(this.id, rscType, 6));
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
   * Method : isProtectingFromStealing
   * Param : int - nbIteration, number of duration of the protection.
   * Desc : set the player in the defense state
   * Return : void
   **/
   public void isProtectingFromStealing(int nbIteration)
   {
     if(!wasUnderProtection)
     {
       this.isWatchingTime = nbIteration;
       this.wasUnderProtection = true;
       playerServ.protectFromStealing(true);
       System.out.println("Je me protège des vols !");
     }
   }


   /**
   * Method : observeProducer
   * Param : int, ressourceType - wanted type of the ressource.
   * Desc : observe a producer of a given ressource type.
   * Return : int, the amount of ressource of the producer
   **/
   public int observeProducer(int rscToTake)
   {
     int i = 0;
     int j = 0;
     int amountOfRscOfProd = 0;
     Producteur produ0 = null;

     // Try to find what we want
     try
     {
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

       amountOfRscOfProd = produ0.getAmountRsc();
       System.out.println("Le producteur : " + produ0.getID() + " a un total de " + amountOfRscOfProd + " de ressource de type : " + produ0.getTypeOfRsc());
     }
     catch (NotBoundException re) { System.out.println(re) ; }
     catch (RemoteException re) { System.out.println(re) ; }
     catch (MalformedURLException e) { System.out.println(e) ; }
     return amountOfRscOfProd;
   }


   /**
   * Method : observeAllPlayers
   * Param : void
   * Desc : call the method observeSystem of the coordinator and get the return
   *        into systemObservation tab.
   * Return : void
   **/
   public void observeAllPlayers()
   {
     try
     {
      observationResult = this.coord.observeSystem();
     }
     catch (RemoteException re) { System.out.println(re) ; }
   }


   /**
   * Method : getRscTypeWithMaxAmount
   * Param : int[][] tab2D
   * Desc : return the ressource type with the maximum amount in the tab.
   * Return : int ressource type
   **/
   public int getRscTypeWithMaxAmount()
   {
     int i = 0;
     int j = 0;
     int maxRsc = 0;
     int max = 0;
     for(i=0; i<this.players.length; i++)
     {
       for(j=0; j<this.stock.length; j++)
       {
         if(max < observationResult[i][j])
         {
           max = observationResult[i][j];
           maxRsc = j;
         }
       }
     }
     return maxRsc;
   }


   /**
   * Method : sendMessage
   * Param : String, msg - message to print on the thread
   * Desc : Print the message msg on the thread output
   * Return : void
   **/
   public void sendMessage(String msg)
   {
     System.out.println(msg);
   }

}
