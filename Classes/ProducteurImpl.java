/**
 * Class : Producteur
 * Desc : Implementation of the class Producteur
 **/

 import java.rmi.server.UnicastRemoteObject ;
 import java.rmi.*;
 import java.net.MalformedURLException;
 import java.rmi.RemoteException ;
 import java.util.Timer;
 import java.util.TimerTask;
 import java.awt.Toolkit;

 public class ProducteurImpl extends Agent implements Producteur
 {
   // Attributes
   private Ressource production;              // Ressource produced by the producer
   private int amountToTake;                  // Amount that the player will take when soliciting the producer
   private int timeBeforeProduction;          // Time in ms before ressource production
   private ProductionTaskTbT productionTask;  // Production task object for turn by turn game
   private Thread ProductionThread;           // Thread to manage the production turn by turn
   public Toolkit toolkitProd;                // Toolkit used by production TimerTask
   public Timer timerProd;                    // Timer used to shedule the production task
   public Toolkit toolkitLog;                 // Toolkit used by log TimerTask
   public Timer timerLog;                     // Timer used to shedule the log task
   private boolean isTurnByTurn;              // Boolean to know is the game is turn bu turn

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
   public ProducteurImpl(String id0, String coord, int typeRsc, int amountRsc, int amountForVictRsc,int timeBeforeProduction0, boolean isTbT0)
      throws RemoteException
   {
     super(id0, 0, coord);

     this.production = new Ressource(typeRsc, amountRsc, amountForVictRsc);
     this.amountToTake = 3;
     this.timeBeforeProduction = timeBeforeProduction0;
     if(!isTbT0)
     {
       this.toolkitProd = Toolkit.getDefaultToolkit();
       this.timerProd = new Timer();
       this.toolkitLog = Toolkit.getDefaultToolkit();
       this.timerLog = new Timer();
     }
     this.isTurnByTurn = isTbT0;
   }


  /**
  * Method : begin
  * Param : void
  * Desc : start the tasks of the producer (log task and production task)
  * Return : void
  **/
  public synchronized void begin()
  {
    // initialisation of log system
    if(isTurnByTurn)
    {
      System.out.println("Le jeu est tour par tour");
      productionTask = new ProductionTaskTbT(this.production, this.coordinateur, this.id, this);
      ProductionThread = new Thread(new Thread(productionTask, this.id + "_threadProducer"));
      ProductionThread.start();
    }
    else
    {
      toolkitLog = Toolkit.getDefaultToolkit();
      timerLog = new Timer();
      // Starting log system that will give the producer status each 10 ms
      timerLog.schedule(new setLogProducerTask(this.production, this.coordinateur, this.id),
                     0,        //initial delay
                     10);      //subsequent rate
      timerProd.schedule(new ProductionTask(this.production, this.coordinateur),
                 	   0,        //initial delay
                 	   this.timeBeforeProduction);  //subsequent rate
    }
  }


  /**
  * Method : printMsg
  * Param : String msg
  * Desc : print a message on standard output.
  * Return : void
  **/
  public void printMsg(String msg)
  {
    System.out.println(msg);
  }

  /**
  * Method : turnStart
  * Param : void
  * Desc : Signal received from coordinator. Start the turn of the producer production task.
  * Return : void
  **/
  public synchronized void turnStart()
  {
    if( (isTurnByTurn) && (ProductionThread.isAlive()))
    {
      synchronized(this)
      {
        this.notify();
      }
    }
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
       return 0;
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
   * Method : getID
   * Param : void
   * Desc : return the id of the producer
   * Return : String, id of the producer
   **/
  public String getID()
  {
    return this.id;
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
  public boolean isEmpty()
  {
   return (this.production.getAmount() == 0);
  }

  /**
   * Method : copyRsc
   * Param : void
   * Desc : return a copy of the producer's ressource
   * Return : Ressource, the copy of the ressource
   **/
   public Ressource copyRsc()
   {
     return this.production.copy();
   }


   /**
   * Method : gameIsOver
   * Param : int, winnerID - id of the winner
   * Desc : Stop the client thread and shut down rmi server cleanly
   * Return : void
   **/
   public synchronized void gameIsOver(String message)
   {
     System.out.println(message);
     if(isTurnByTurn)
     {
       if(this.ProductionThread.isAlive())
       {
         this.productionTask.stopProduction();
       }
     }
     else
     {
       timerProd.cancel();
       timerProd.purge();
       timerLog.cancel();
       timerLog.purge();
     }
     System.out.println("J'ai finis la production et il me reste : " + this.production.getAmount() + " of ressource " + this.production.getType());
   }

 }
