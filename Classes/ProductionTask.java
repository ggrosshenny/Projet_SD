/**
* Class : ProductionTask
* Extend : TimerTask
* Desc : Class to add ressource units to the
*        amount of a given Ressource object
*        repeating it endless waiting a given
*        amount of ms between each ressource
*        modification
**/

import java.rmi.*;
import java.util.TimerTask;
import java.net.MalformedURLException;

public class ProductionTask extends TimerTask
{
  private Ressource prod;
  private String coord;
  private ICoordinateur coordObject;

  public ProductionTask(Ressource res, String coord0)
  {
    prod = res;
    this.coord = coord0;
  }

  public void run()
  {
    if((prod.getAmount() / 2) > 0)
    {
      prod.addRessource((prod.getAmount() / 2) + 1);
    }
    else
    {
      try
      {
        coordObject = (ICoordinateur)Naming.lookup(coord);
        coordObject.ProducerEmpty();
      }
      catch (NotBoundException re) { System.out.println(re) ; }
      catch (RemoteException re) { System.out.println(re) ; }
      catch (MalformedURLException e) { System.out.println(e) ; }

    }
  }
}
