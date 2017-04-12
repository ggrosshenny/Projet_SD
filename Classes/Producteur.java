import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface Producteur extends Remote
{
  public int takeRsc()
    throws RemoteException;

  public int getTypeOfRsc()
    throws RemoteException;

  public int getAmountRsc()
    throws RemoteException;

}
