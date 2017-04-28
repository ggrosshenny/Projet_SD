// Class joueurCoopTbT

import java.rmi.* ;
import java.net.MalformedURLException ;

public class JoueurCoopTbT extends JoueurCommon implements Runnable
{
  // Attributes
  int nbTurnToWait;
  int nb_turn;
  int[] stockStatus;

  // Methods


  public JoueurCoopTbT(JoueurImpl playerServ0, String id0, String coord0, int amountToTake0)
  {
    super(playerServ0, id0, coord0, amountToTake0);
    this.nbTurnToWait = 3;
    this.nb_turn = 0;
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
    boolean finished = false;
    Producteur produ = null;
    this.stockStatus = new int[stock.length];

    // Waiting on starting blocks !
    try
    {
      coord.PlayerReady(); // Say to the coordinator that we are ready
    }
    catch (RemoteException re) { System.out.println(re) ; }

    // Control message
    System.out.println("Je commence mon travail !");

    while(!finished && running)
    {

      synchronized(playerServ)
      {
        try
        {
          playerServ.wait();
        }
        catch (InterruptedException ie) {System.out.println(ie) ;}
      }


      // Verify if we are allowed to play
      if(isPunnished)
      {
        System.out.println("Je me suis fait attrapé...");
        // Wait until we can play again
        if(nbTurnToWait > 0)
        {
          nbTurnToWait--;
        }
        else
        {
          isPunnished = false;
        }
      }

      // Verify if we are not observing to protect ourself from stealing
      if(isWatchingTime > 0) // If we are protecting from steal
      {
        this.isWatchingTime--;
        if(isWatchingTime == 0)
        {
          playerServ.protectFromStealing(false);
        }
      }

      // If we are not punnished or observing, we can play
      if(!isPunnished && isWatchingTime == 0)
      {
        // Seek the ressource type we want to take
        rscToTake = chooseRscType();

        try
        {
          if(rollTheDice(this.stealPercentage))
          {
            takeRscFromPlayer(rscToTake);
            System.out.println("J'ai volé un joueur !");
          }
          else
          {
            // Seeking for producer and/or taking ressources
            if((produ == null) || (produ.getAmountRsc() < amountToTake) || (stock[produ.getTypeOfRsc()].amountForVictoryIsReached()))
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
            }
          }
        }
        catch (RemoteException re) { System.out.println(re) ; }
      }

      // Verifying if all objectives are completed
      finished = true;
      for(i=0; i<stock.length; i++)
      {
        if(stock[i] != null)
        {
          finished = finished && stock[i].amountForVictoryIsReached();
        }
      }

      if(!finished)
      {
        try
        {
          this.coord.endOfTurn(this.id);
        }
        catch (RemoteException re) { System.out.println(re) ; }
      }

      nb_turn++;

      // Write the logs with coordinator
      for(i=0; i<stock.length; i++)
      {
        stockStatus[i] = stock[i].getAmount();
      }
      try
      {
        this.coord.setLog(this.id, this.stockStatus);
      }
      catch (RemoteException re) { System.out.println(re) ; }

    }

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