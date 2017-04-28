import java.net.* ;
import java.rmi.* ;

public class Joueur
{

  public static void main(String[] args)
  {
    if(args.length != 9)
    {
      System.out.println("Usage : Joueur machine port id type coord isCoop isTbT isHuman nb_typeRsc") ;
      return;
    }
    try
    {
      boolean isCoop = false;
      boolean isTbT = false;
      boolean isHuman = false;

      if(Integer.parseInt(args[5]) == 0)
      {
        isCoop = true;
      }
      if(Integer.parseInt(args[6]) == 0)
      {
        isTbT = true;
      }
      if(Integer.parseInt(args[7]) == 0)
      {
        isHuman = true;
      }
      JoueurImpl player = new JoueurImpl(args[2], Integer.parseInt(args[3]), args[4], isCoop, isTbT, isHuman,  Integer.parseInt(args[8]));
      Naming.rebind("rmi://" + args[0] + ":" + args[1] + "/" + args[2], player);
      System.out.println(args[2] + " is ready...");
    }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
