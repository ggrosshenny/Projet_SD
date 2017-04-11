/**
 * Class JoueurImpl
 * Extends class UnicastRemoteObject
 * Implements interface Joueur
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
  public Joueur(int id0, int type0, char[] addr, char[] coord, Ressource[] objectives, int nbJoueurs)
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
