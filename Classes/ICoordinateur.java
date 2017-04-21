
/**
 * interface ICoordinateur
 * Interface for CoordinateurImple
 **/

 import java.rmi.Remote ;
 import java.rmi.RemoteException ;

public interface ICoordinateur extends Remote
{
  public boolean endGame(String idJoueur)
    throws RemoteException;

  public void PlayerReady()
    throws RemoteException;

  public boolean playerStart()
    throws RemoteException;
}
