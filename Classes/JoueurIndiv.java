/**
* Class : JoueurIndiv
* Extend :
* Desc : Class to modelize the actions of player (taking ressource from
*        produceur, watch everything, etc... ). Used in a thread from the
*        server part of player (i.e. this is one variant of the client part of player).
**/

import java.rmi.* ;
import java.net.MalformedURLException ;

public class JoueurIndiv extends Thread
{

  // Attributes
  private Ressource[] stock;      // Stock of the player
  private String[][] prod;        // Addresses of producers ([<type of ressource produced>][<producer>])
  private int amountToTake;       // Amount to take each time the player wants to take ressource's units from a producer
  private Random rand;            // Used to create random int
  private Coordinateur coord;     // Coordinateur object

  // methods

  public JoueurIndiv(String coord0, Ressource[] stock0, String[][] prod0, int amountToTake0)
  {
    this.stock = stock0;
    this.prod = prod0;
    this.amountToTake = amountToTake0;
    this.rand = new Random();
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
    int randomRessourceType = rand.nextInt(stock.length());
    int rscToTake = 0;
    Producteur produ0;

    try
    {
      // seek for a ressource that we need
      for(i=randomRessourceType; i==randomRessourceType-1; ((i+1)%randomRessourceType))
      {
        if(!stock[i].amountForVictoryIsReached())
        {
          rscToTake = i;
          break;
        }
      }

      // Seek for a producer of the given ressource type
      j=rand.nextInt(prod[i].length());
      produ0 = (Producteur)Naming.lookup(prod[i][j]);
      stock[i] += produ0.takeRsc();
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
   takeRessource(Producteur produ)
   {
     try
     {
       stock[produ.getType] += produ.takeRsc();
     }
     catch (NotBoundException re) { System.out.println(re) ; }
     catch (RemoteException re) { System.out.println(re) ; }
     catch (MalformedURLException e) { System.out.println(e) ; }
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

    While(!finished)
    {
      // Seeking for producer and/or taking ressources
      if((produ == null) || (produ.getAmountRsc() <= 0))
      {
        produ = takeRessourceFromNewProducer(produ);
      }
      else
      {
        takeRessource(produ);
      }

      // Verifying if all objectives are completed
      for(i=0; i<stock.length(); i++)
      {
        finished = finished && stock[i].amountForVictoryIsReached;
      }

    }

    // TO DO
    // envoyer le message au coordinateur "j'ai finis fdp !"

  }


}
