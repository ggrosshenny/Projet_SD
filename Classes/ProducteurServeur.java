import java.net.* ;
import java.rmi.* ;

public class ProducteurServeur
{
  public static void main(String[] args)
  {
    if(args.length != 7)
    {
      System.out.println("Usage : producteurServeur port id coord prodType prodAmount prodAmoutForVictory timeBeforeProduction");
      return;
    }
    try
    {
      //                                                 id        coord   rsc     timeBeforeProduction
      ProducteurImpl localProducer = new ProducteurImpl(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
      Naming.rebind("rmi://localhost:" + args[0] + "/" + args[1], localProducer);
      System.out.println(args[1] + "of type : " + args[3] + " is ready...");
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
