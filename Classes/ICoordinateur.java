
/**
 * interface ICoordinateur
 * Interface for CoordinateurImple
 **/

 import java.rmi.Remote ;
 import java.rmi.RemoteException ;

public interface ICoordinateur extends Remote
{
  public void endGame(String idJoueur)
    throws RemoteException;
}