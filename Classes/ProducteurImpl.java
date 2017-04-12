/**
 * Class : Producteur
 * Desc : Implementation of the class Producteur
 **/

 import java.rmi.server.UnicastRemoteObject ;
 import java.rmi.RemoteException ;
 import java.util.Timer;
 import java.util.TimerTask;
 import java.awt.Toolkit;

 public class ProducteurImpl extends Agent implements Producteur
 {
   // Attributes
   private Ressource production;      // Ressource produced by the producer
   private int amountToTake;          // Amount that the player will take when soliciting the producer
   private int timeBeforeProduction;  // Time in ms before ressource production
   public Toolkit toolkit;            // Toolkit used by TimerTask
   public Timer timer;                // Timer used to shedule the task

   // Methods

   /**
   * Method : Producteur
   * Param : int, id0 - id of the agent
   * param : String, addr - address of the corba object of the Producteur object
   * param : String, coord - address of the corba object of the coordinateur object
   * param : Ressource, prod - ressource of the producer
   * Desc : constructor of the Producteur class
   * return : void
   **/
   public ProducteurImpl(int id0, String addr, String coord, Ressource prod, int timeBeforeProduction0)
      throws RemoteException
   {
     super(id0, 0, addr, coord);
     if(prod == null)
     {
       prod = new Ressource(0,5,5);
     }
     this.production = prod.copy();
     this.amountToTake = 3;
     toolkit = Toolkit.getDefaultToolkit();
     timer = new Timer();
     timer.schedule(new ProductionTask(production),
	                  0,        //initial delay
	                  timeBeforeProduction0);  //subsequent rate
   }


   /**
   * Method : takeRsc
   * Param : void
   * Desc : Remove the taken amount of the ressource and return the amount taken to the player
   * Return : int, the amount taken from the producer by the player. Return -1 if the producer has nothing.
   **/
   public synchronized int takeRsc()
   {
     if(!isEmpty())
     {
       int amount = this.production.rmvRessource(this.amountToTake);
       return amount;
     }
     else
     {
       return -1;
     }
   }


  /**
  * Method : getTypeOfRsc
  * Param : void
  * Desc : Return the type of the producer's Ressource object
  * Return : int, the type of the Ressource object
  **/
  public int getTypeOfRsc()
  {
    return production.getType();
  }


  /**
   * Method : getAmountRsc
   * Param : void
   * Desc : Return the actual amount of the Ressource object
   * Return : int, return the amount of the Ressouce object
   **/
   public int getAmountRsc()
   {
     return production.getAmount();
   }


   /**
   * Method : isEmpty
   * Param : void
   * Desc : tell to the peer if the ressource of the producer is empty or not
   * Return : if the ressource is empty : true, else : false
   **/
   private boolean isEmpty()
   {
     return (this.production.getAmount() == 0);
   }


 }
