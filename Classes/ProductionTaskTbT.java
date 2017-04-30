/**
* Class : ProductionTaskTbT
* Extend : Runnable
* Desc : Class to add ressource units to the
*        amount of a given Ressource object
*        repeating it after all player turns
**/
import java.rmi.*;
import java.net.MalformedURLException;

public class ProductionTaskTbT implements Runnable
{
  private ProducteurImpl masterThread;
  private Ressource prod;
  private int maxAmount;
  private String coord;
  private String id;
  private ICoordinateur coordObject;
  private boolean running;
  private int nbTurnProduction;
  private boolean sendRscType;


  public ProductionTaskTbT(Ressource res, String coord0, String id0, ProducteurImpl masterThread0)
  {
    this.prod = res;
    this.coord = coord0;
    this.id = id0;
    this.maxAmount = prod.getAmount()*10;
    this.running = true;
    this.masterThread = masterThread0;
    this.nbTurnProduction = 6;
    this.sendRscType = true;
    try
    {
      coordObject = (ICoordinateur)Naming.lookup(this.coord);
    }
    catch (NotBoundException re) { System.err.println(re) ; }
    catch (RemoteException re) { System.err.println(re) ; }
    catch (MalformedURLException e) { System.err.println(e) ; }
  }


  /**
  * Method : stopProduction
  * Param : void
  * Desc : Set the Runnable boolean to false to stop the production task of producer
  * Return : void
  **/
  public void stopProduction()
  {
    this.running = false;
  }


  /**
  * Method : run
  * Param : void
  * Desc : Wait to be wake up and produce amounts of the given ressource
  * Return : void
  **/
  public void run()
  {
    int nbTurn = 0;
    boolean canSendEndGame = true;
    masterThread.printMsg("Je commence");
    while(running)
    {
      // Wait to be allowed to produce something
      synchronized(masterThread)
      {
        try
        {
          masterThread.wait();
        }
        catch (InterruptedException ie) {System.out.println(ie) ;}
      }

      if((nbTurn%nbTurnProduction) == 0)
      {
        // Produce something if the prod is not empty
        if(((this.prod.getAmount() / 2) > 0) && (this.prod.getAmount() < this.maxAmount))
        {
          prod.addRessource((this.prod.getAmount() / 2) + 1);
        }
        // Else, notify to the coordinator that we the prod is empty
        else if((this.prod.getAmount() <= 0) && (canSendEndGame))
        {
          try
          {
            System.out.println("la valeur de mon stock est : " + this.prod.getAmount());
            coordObject.ProducerEmpty();
            canSendEndGame = false;
          }
          catch (RemoteException re) { System.err.println(re) ; }
        }
      }

      try
      {
        System.out.println("J'envoie mes logs");
        if(sendRscType)
        {
          coordObject.setLogProd(this.id, this.prod.getType());
          sendRscType = false;
        }
        coordObject.setLogProd(this.id, this.prod.getAmount());
      }
      catch (RemoteException re) { System.err.println(re) ; }

      nbTurn++;

    }

  }



















}
