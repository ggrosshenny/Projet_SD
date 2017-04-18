/**
 * interface Joueur
 * Interface for JoueurImpl
 **/

 import java.rmi.Remote ;
 import java.rmi.RemoteException ;

public interface Joueur extends Remote
{
  public int steal(int rscType, int amount)
    throws RemoteException;

  public void gameIsOver(int winnerID)
    throws RemoteException;

  public void begin()
    throws RemoteException;
}
