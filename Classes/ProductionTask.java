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
  private ICoordinateur coord;

  public ProductionTask(Ressource res, String coord0)
  {
    prod = res;
    try
    {
      coord = (ICoordinateur)Naming.lookup(coord0);
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }

  public void run()
  {
    if((prod.getAmount() / 2) > 0)
    {
      prod.addRessource((prod.getAmount() / 2) + 1);
    }
    else
    {
      // TO DO
      // Ajouter le fait qu'on puisse perdre la partie avec tous les producteurs vides.
    }
  }
}
