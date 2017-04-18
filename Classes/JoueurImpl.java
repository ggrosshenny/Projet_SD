/**
 * Class JoueurImpl
 * Extends class UnicastRemoteObject
 * Implements interface Joueur
 * Is the server part of each player. Implements the methods that can be called by
 *    other players. Thoses methods are always available.
 **/

import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;


public class JoueurImpl extends UnicastRemoteObject implements Joueur
{
  // Attributes
  private Ressource[] stock;      // Stock of the player
  private String[][] prod;        // Addresses of producers ([<type of ressource produced>][<producer>])
  private boolean isTurnByTurn;   // Boolean to know if we are in tur by turn mod
  private boolean isCoop;         // Boolean to know if the player is cooperative or not
  private boolean isWatcher;      // Boolean to know if the player is watching fo other player's actions or not
  private Joueur[] watchers;      // List of players watching for ohter player's actions
  private Thread playerClient;    // Thread used by the player to take ressources or watch other player's actions

  // Methods
  public JoueurImpl(int id0, int type0, String addr, String coord, Ressource[] objectives, int nbJoueurs, String[][] prod0, boolean isCoop0, boolean isTbT)
  {
    int i=0;

    super(id0, type0, addr, coord);
    this.isWatcher = false;
    this.watchers = new Joueur[nbJoueurs];
    this.stock = new Ressource[objectives.length()];

    for(i=0; i<objectives.length(); i++)
    {
      stock[i] = objectives[i];
      stock[i].totalAmount = 0;
    }

    this.prod = prod0;
    this.isCoop = isCoop0;
    this.isTurnByTurn = isTbT;

    // Create the client part of the player (p2p)
    // Create the player object with the right behaiour
    if(!isTurnByTurn)
    {
      if(isCoop) // Cooperative player without turn waiting
      {
        JoueurCoop joueur = new JoueurCoop(coord, stock, prod, 3);
      }
      if(!isCoop) // Non-cooperative player without turn waiting
      {
        //JoueurIndiv joueur = new JoueurIndiv(coord, stock, prod, 3);
      }
    }
    else
    {
      if(isCoop) // Cooperative player without turn waiting
      {
        // TO DO
      }
      if(!isCoop) // Non-cooperative player without turn waiting
      {
        // TO DO
      }
    }
    // Creating thread for the client part of the player
    playerClient = new Thread(joueur, addr + "_threadClient");
  }


  /**
   * Method : begin
   * Param : void
   * Desc : Start the thread
   * Return : void
   **/
  public void begin()
  {
   playerClient.start();
  }


  /**
  * Method : gameIsOver
  * Param : int, winnerID - id of the winner
  * Desc : Stop the client thread and shut down rmi server cleanly
  * Return : void
  **/
  public void gameIsOver(int winnerID)
  {
    System.out.println("The game is over. The winner is player " + winnerID);

  }


  /**
   * Method : steal
   * Param : int, rscType - the kind of ressource that is stolen
   * Param : int, amount - the amount of ressource that is stolen
   * Desc : Method that modelize the moment when the palyer is stolen by someone
   * Return : int, the stolen amount of the specified ressource
   **/
   // TO DO


}
