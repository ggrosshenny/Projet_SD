/**
* Class : JoueurCoop
* Extend : Thread
* Desc : Class to modelize the actions of player (taking ressource from
*        produceur, watch everything, etc... ). Used in a thread from the
*        server part of player (i.e. this is one variant of the client part of player).
**/

import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.Toolkit;

public class JoueurCoop extends JoueurCommon implements Runnable
{

  // Attributes
  public Toolkit toolkit;                 // Toolkit used by TimerTask
  public Timer timer;                     // Timer used to shedule the task

  // methods

  public JoueurCoop(JoueurImpl playerServ0, String id0, String coord0, int amountToTake0)
  {
    super(playerServ0, id0, coord0, amountToTake0);

    // Initialize the timer and timerTask for log system
    this.toolkit = Toolkit.getDefaultToolkit();
    this.timer = new Timer();
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
    int rscToTake = 0;
    int lastRscTaken = 0;
    int temp = 0;
    boolean finished = false;
    Producteur produ = null;

    // Waiting on starting blocks !
    // Sorry for active waiting...
    try
    {
      coord.AgentReady(); // Say to the coordinator that we are ready
    }
    catch (RemoteException re) { System.out.println(re) ; }

    // Wait for other players
    try
    {
      while(!coord.playerStart())
      {
      }
    }
    catch (RemoteException re) { System.out.println(re) ; }

    // Starting log system that will give the stock status each 1000 ms
    timer.schedule(new setLogPlayerTask(stock, coordAddr, this.id),
                   0,        //initial delay
                   10);  //subsequent rate

    System.out.println("Je commence mon travail !");

    while(!finished && running)
    {
      // Verify if we are allowed to play
      if(isPunnished)
      {
        System.out.println("Je me suis fait attrapé...");
        // Wait until we can play again
        try {
            Thread.sleep(20);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        isPunnished = false;
      }

      if(isWatchingTime > 0) // If we are protecting from steal
      {
        this.isWatchingTime--;
        if(isWatchingTime == 0)
        {
          playerServ.protectFromStealing(false);
        }
      }
      else // If the player wants to take ressources from producers or other players
      {
        // Seek for the ressource type we want to take
        rscToTake = chooseRscType();

        try
        {
          // Observe all the system to know the kind of ressource to take
          if(rollTheDice(3))
          {
            observeAllPlayers();
            temp = getRscTypeWithMaxAmount();
            if(!stock[temp].amountForVictoryIsReached())
            {
              rscToTake = temp;
            }
          }

          //Try to steal a player
          if(rollTheDice(this.stealPercentage))
          {
            takeRscFromPlayer(null, rscToTake);
            System.out.println("J'ai volé un joueur !");
          }
          else // Or try to take ressource from a producer
          {
            // Seeking for producer and/or taking ressources
            if((produ == null) || (produ.getAmountRsc() < amountToTake) || (stock[produ.getTypeOfRsc()].amountForVictoryIsReached()) || (rscToTake != lastRscTaken))
            {
              produ = takeRessourceFromNewProducer(rscToTake);
            }
            else
            {
              takeRessource(produ);
            }
            // Reset of protection ability
            this.wasUnderProtection = false;
            // Control message
            if(produ != null)
            {
              System.out.println("Valeur de rsc " + " : " + stock[produ.getTypeOfRsc()].getAmount() + "/" + stock[produ.getTypeOfRsc()].getAmountForVictory() + " (" + produ.getID() + " : " + produ.getTypeOfRsc() + ")");
            }          }
        }
        catch (RemoteException re) { System.out.println(re) ; }
      }

      // Save the type of rsc taken
      lastRscTaken = rscToTake;

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

  }


}
