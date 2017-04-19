import java.net.* ;
import java.rmi.* ;

public class Joueur extends UnicastRemoteObject
{

  public static void main(String[] args)
  {
    if(args.length != 6)
    {
      System.out.println("Usage : Joueur machine port id type coord isCoop isTbT") ;
      return;
    }
    try
    {
      boolean isCoop = false;
      boolean isTbT = false;

      if(Integer.parseInt(args[5]) == 0)
      {
        isCoop = true;
      }
      if(Integer.parseInt(args[6]) == 0)
      {
        isTbT = true;
      }
      JoueurImpl joueur = new JoueurImpl(args[2], Integer.parseInt(args[3]), args[4], isCoop, isTbT);
      Naming.rebind("rmi://" + args[0] + ":" + args[1] + "/Prod_" + args[1], localProducer);
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
