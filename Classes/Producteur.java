/**
 * Class : Producteur
 * Desc : Implementation of the class Producteur
 **/

 public class Producteur extends Agent
 {
   // Attributes
   private synchronized boolean isAvailable;
   private Ressource production;

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
   public Producteur(int id0, char[] addr, char[] coord, Ressource prod)
   {
     super(id0, 0, addr, coord);
     this.production = prod.copy();
   }


   /**
   * Method :
   * Param :
   * Desc :
   *Return :
   **/


 }
