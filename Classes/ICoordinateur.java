
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

  public void endOfTurn(String id)
    throws RemoteException;

  public void ProducerEmpty()
    throws RemoteException;

  public void setLog(String id, int[] Stock)
    throws RemoteException;
    
  public void setLogProd(String id, int Stock)
    throws RemoteException;

  public int[][] observeSystem()
    throws RemoteException;

}
