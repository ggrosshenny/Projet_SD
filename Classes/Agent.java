/**
 * Class : Agent
 * Desc : Implementation of the class Agent
 **/

 public class Agent
 {
   // Attributes
   protected int id;
   protected int type;
   protected char[] addresse;
   protected char[] coordinateur;


   // Constructor
   public Agent(int id0, int type0, char[] addr, char[] coord)
   {
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
     {
       return type;
     }


     /**
      * Method : getAddr
      * Param : void
      * Des : Give the addresse of the agent
      * Return : char[], addresse of the agent
      **/
      public char[] getAddr()
      {
        return addresse;
      }


      /**
       * Method : getCoord
       * Param : void
       * Des : Give the Coordinateur object
       * Return : object, coordinateur
       **/
       public Object getCoord()
       {
         return coordinateur;
       }



















 }
