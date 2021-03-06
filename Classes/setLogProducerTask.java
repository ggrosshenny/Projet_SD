
/**
* Class : setLogProducerTask
* Extend : TimerTask
* Desc : Give informations about player actions at fixed time
**/

import java.rmi.*;
import java.util.TimerTask;
import java.net.MalformedURLException;

public class setLogProducerTask extends TimerTask
{
  private Ressource stock;
  private String id;
  private ICoordinateur coordObject;
  private boolean giveRscType;

  public setLogProducerTask(Ressource stock0, String coord0, String id0)
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
    this.giveRscType = true;
  }

  public void run()
  {
    try
    {
      if(giveRscType)
      {
        coordObject.setLogProd(this.id, this.stock.getType());
        giveRscType = false;
      }
      coordObject.setLogProd(this.id, this.stock.getAmount());
    }
    catch (RemoteException re) { System.out.println(re) ; }
  }
}
