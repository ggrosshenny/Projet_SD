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
   protected final String id;
   protected final int type;
   protected String coordinateur;


   // Constructor
   public Agent(String id0, int type0, String coord)
    throws RemoteException
   {
     super();
     this.id = id0;
     this.type = type0;
     this.coordinateur = coord;
   }


   // Methods

   /**
    * Method : getId
    * Param : void
    * Des : Give the Id of the agent
    * Return : String, id of the agent
    **/
    public String getId()
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
