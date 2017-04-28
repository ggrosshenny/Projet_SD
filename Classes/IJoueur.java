/**
 * interface Joueur
 * Interface for JoueurImpl
 **/

import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.concurrent.CyclicBarrier;

public interface IJoueur extends Remote
{
  public void gameIsOver(String message, String idWinner)
    throws RemoteException;

  public void begin(String[] Joueurs, String[][] Producteurs)
    throws RemoteException;

  public void turnStart()
    throws RemoteException;

  public void punish()
    throws RemoteException;

  public int steal(String id, int rscType, int amount)
    throws RemoteException;

  public void changeStealPercentage(int newPercentage)
    throws RemoteException;
}
