import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface Producteur extends Remote
{
  public int takeRsc()
    throws RemoteException;

  public int getTypeOfRsc()
    throws RemoteException;

  public String getID()
    throws RemoteException;

  public int getAmountRsc()
    throws RemoteException;

  public void gameIsOver(String winnerID)
    throws RemoteException;

  public Ressource copyRsc()
    throws RemoteException;
}
