import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface Producteur extends Remote
{
  public void begin()
    throws RemoteException;

  public void turnStart()
    throws RemoteException;

  public int takeRsc()
    throws RemoteException;

  public int getTypeOfRsc()
    throws RemoteException;

  public String getID()
    throws RemoteException;

  public int getAmountRsc()
    throws RemoteException;

  public void gameIsOver(String message)
    throws RemoteException;

  public Ressource copyRsc()
    throws RemoteException;
}
