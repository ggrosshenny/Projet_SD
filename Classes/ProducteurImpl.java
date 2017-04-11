/**
 * Class : Producteur
 * Desc : Implementation of the class Producteur
 **/

 import java.rmi.server.UnicastRemoteObject ;
 import java.rmi.RemoteException ;

 public class ProducteurImpl extends Agent implements Producteur
 {
   // Attributes
   private Ressource production;      // Ressource produced by the producer
   private int amountToTake;          // Amount that the player will take when soliciting the producer
   private int timeBeforeProduction;  // Time in ms before ressource production
   // Methods


   /**
   * Method : Producteur
   * Param : int, id0 - id of the agent
   * param : char[], addr - address of the corba object of the Producteur object
   * param : char[], coord - address of the corba object of the coordinateur object
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
       prod = new Ressource(0,0,0);
     }
     this.production = prod.copy();
     this.amountToTake = 3;
     this.timeBeforeProduction = timeBeforeProduction0;
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
   * Method : CreateRsc
   * Param : void
   * Desc : Create ressource units after a certain amount of time (in ms)
   * Return : void
   **/
   public void CreateRsc()
   {
     if(this.production.getAmount() == 0)
     {
       this.production.addRessource((this.production.getAmount() / 2) + 1);
     }
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
