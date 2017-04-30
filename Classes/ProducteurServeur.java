import java.net.* ;
import java.rmi.* ;

public class ProducteurServeur
{
  public static void main(String[] args)
  {
    if(args.length != 8)
    {
      System.out.println("Usage : producteurServeur port id coord prodType prodAmount prodAmoutForVictory timeBeforeProduction isTbT");
      return;
    }
    try
    {
      boolean isTurnByTurn = false;
      if(Integer.parseInt(args[7]) == 0)
      {
        isTurnByTurn = true;
      }

      ProducteurImpl localProducer = new ProducteurImpl(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), isTurnByTurn);
      Naming.rebind("rmi://localhost:" + args[0] + "/" + args[1], localProducer);
      System.out.println(args[1] + " of type : " + args[3] + " is ready...");
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
