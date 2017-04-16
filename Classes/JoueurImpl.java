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
  protected Ressource[] stock;
  protected boolean isTurnByTurn;
  protected boolean isWatcher;
  protected Joueur[] watchers;

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

  // TO DO
  /*
  Methode permettant à l'IA d'executer ses recherches de ressources, ses vols, etc...
      utilisant un timertask (avec un timer si la partie n'est pas tour par tour, un wait sinon)
  Methode isStolen -> si le joeuur n'est pas en observation, il donne de ses
                      ressources au voleur, sinon il lui demonte sa gueule
  */


}
