/**
* Class : setLogPlayerTask
* Extend : TimerTask
* Desc : Give informations about player actions at fixed time
**/

import java.rmi.*;
import java.util.TimerTask;
import java.net.MalformedURLException;

public class setLogPlayerTask extends TimerTask
{
  private Ressource[] stock;
  private int[] stockStatus;
  private String id;
  private ICoordinateur coordObject;

  public setLogPlayerTask(Ressource[] stock0, String coord0, String id0)
  {
    try
    {
      coordObject = (ICoordinateur)Naming.lookup(coord0);
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }

    this.stock = stock0;
    this.id = id0;
    this.stockStatus = new int[stock.length];
  }

  public void run()
  {
    int i = 0;
    for(i=0; i<stock.length; i++)
    {
      stockStatus[i] = stock[i].getAmount();
    }
    try
    {
      coordObject.setLog(this.id, stockStatus);
    }
    catch (RemoteException re) { System.out.println(re) ; }
  }
}
