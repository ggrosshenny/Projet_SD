import java.net.* ;
import java.rmi.* ;

public class ProducteurServeur
{
  public static void main(String[] args)
  {
    if(args.length != 6)
    {
      System.out.println("Usage : producteurServeur machine id addr coord prod timeBeforeProduction") ;
      return;
    }
    try
    {
      ProducteurImpl localProducer = new ProducteurImpl(Integer.parseInt(args[1]), args[2], args[3], null, 0);
      String name = new String("rmi://" + args[0] + "/Prod_" + args[1]);
      Naming.rebind(name, localProducer);
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
