import java.net.* ;
import java.rmi.* ;

public class Joueur extends UnicastRemoteObject
{

  public static void main(String[] args)
  {
    if(args.length != 6)
    {
      System.out.println("Usage : Joueur id type addr coord objectives nbJoueurs prod isCoop isTbT") ;
      return;
    }
    try
    {
      JoueurImpl joueur = new JoueurImpl(Integer.parseInt(args[1]), args[2], args[3], null, 5000);
      String name = new String("rmi://" + args[0] + "/Prod_" + args[1]);
      Naming.rebind(name, localProducer);
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
