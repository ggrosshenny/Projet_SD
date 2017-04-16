/**
 * Class Joueur
 * Extends class Agent
 **/

import java.net.MalformedURLException ;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Joueur extends Agent
{
   public static void main(String [] args)
  {

	if (args.length != 2)
    {
		System.out.println("Usage : java Client <machine du Serveur> <port du rmiregistry> ") ;
		System.exit(0) ;
    }
    try
    {
		JoueurImpl j = (JoueurImpl) naming.lookup("rmi://" + args[0] + ":" + args[1] + "JoueurImpl");

		while(true) // Changer condition du while
    {



		}

    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
