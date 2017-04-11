/**
 * Class : Agent
 * Desc : Implementation of the class Agent
 **/

/**
 * Types des différents agents :
 * 0 - Producteur
 * 1 - Joueur
 **/

 import java.rmi.server.UnicastRemoteObject ;
 import java.rmi.RemoteException ;
 import java.net.InetAddress.* ;
 import java.net.* ;

 public class Agent extends UnicastRemoteObject
 {
   // Attributes
   protected final int id;
   protected final int type;
   protected String addresse;
   protected String coordinateur;


   // Constructor
   public Agent(int id0, int type0, String addr, String coord)
    throws RemoteException
   {
     super();
     this.id = id0;
     this.type = type0;
     this.addresse = addr;
     this.coordinateur = coord;
   }


   // Methods

   /**
    * Method : getId
    * Param : void
    * Des : Give the Id of the agent
    * Return : int, id of the agent
    **/
    public int getId()
      throws RemoteException
    {
      return id;
    }


    /**
     * Method : getType
     * Param : void
     * Des : Give the type of the agent
     * Return : int, type of the agent
     **/
     public int getType()
      throws RemoteException
     {
       return type;
     }


     /**
      * Method : getAddr
      * Param : void
      * Des : Give the addresse of the agent
      * Return : char[], addresse of the agent
      **/
      public String getAddr()
        throws RemoteException
      {
        return addresse;
      }


      /**
       * Method : getCoord
       * Param : void
       * Des : Give the Coordinateur addresse
       **/
       public String getCoord()
        throws RemoteException
       {
         return coordinateur;
       }



















 }
