/**
 * interface Joueur
 * Interface for JoueurImpl
 **/

import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.concurrent.CyclicBarrier;

public interface IJoueur extends Remote
{
  //public int steal(int rscType, int amount)
  //  throws RemoteException;

  public void gameIsOver(String winnerID)
    throws RemoteException;

  public void begin(String[] Joueurs, String[][] Producteurs)
    throws RemoteException;
}
