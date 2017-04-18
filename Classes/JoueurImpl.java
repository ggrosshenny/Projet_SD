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

  // Methods
  public JoueurImpl(int id0, int type0, String addr, String coord, Ressource[] objectives, int nbJoueurs)
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
  }

  /**
   * Method : iStolen
   * Param : int, rsc - the kind of ressource that is stolen
   * Param : int, rsc - the amount of ressource that is stolen
   * Desc : Method that modelize the moment when the palyer is stolen by someone
   * Return : int, the stolen amount of the specified ressource
   **/
   // TO DO

  // TO DO
  /*
  Methode permettant à l'IA d'executer ses recherches de ressources, ses vols, etc...
      utilisant un timertask (avec un timer si la partie n'est pas tour par tour, un wait sinon)
      -> classes JoueurIndiv et JoueurCoop
  Methode isStolen -> si le joeuur n'est pas en observation, il donne de ses
                      ressources au voleur, sinon il lui demonte sa gueule
  */


}
