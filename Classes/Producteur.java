import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface Producteur extends Remote
{
  public int takeRsc()
    throws RemoteException;

  public void CreateRsc()
    throws RemoteException;

}
