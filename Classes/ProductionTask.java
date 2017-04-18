/**
* Class : ProductionTask
* Extend : TimerTask
* Desc : Class to add ressource units to the
*        amount of a given Ressource object
*        repeating it endless waiting a given
*        amount of ms between each ressource
*        modification
**/

import java.util.TimerTask;

public class ProductionTask extends TimerTask
{
  private Ressource prod;

  public ProductionTask(Ressource res)
  {
    prod = res;
  }

  public void run()
  {
    prod.addRessource((prod.getAmount() / 2) + 1);
  }
}
