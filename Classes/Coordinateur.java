import java.rmi.*;
import java.util.Random;
import java.net.MalformedURLException;

public class Coordinateur {

    public static void main(String args[]) {

		if (args.length < 7)
		{
			System.out.println("Usage : java Coordinateur <machine du Serveur> <port du rmiregistry> <nombre_joueurs> <nombre_producteurs> <liste_Joueurs> <liste_Producteurs> <addr_coordinateur>") ;
			System.exit(0) ;
		}

		IJoueur tempJoueur = null;
		Producteur tempProd = null;
    try {
			CoordinateurImpl Coord = new CoordinateurImpl(args);
			Naming.rebind(args[args.length - 1], Coord);

			int i, j;

			for(i = 0; i < Coord.Players.length; i++)
      {
				System.out.println("JOUEURS : " + Coord.Players[i]);
			}
			for(i = 0; i < Coord.Producers.length; i++){
				for(j = 0; j < Coord.Producers[i].length; j++){
					System.out.println("PRODUCTEURS : " + Coord.Producers[i][j]);
				}
			}

			for(i = 0; i < Coord.Players.length; i++){
				try {
					tempJoueur = (IJoueur)Naming.lookup(Coord.Players[i]);
					tempJoueur.begin(Coord.Players, Coord.Producers);
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
			}
			for(i = 0; i < Coord.Producers.length; i++){
				for(j = 0; j < Coord.Producers[i].length; j++){

					try {
						if(Coord.Producers[i][j] != null)
							tempProd = (Producteur)Naming.lookup(Coord.Producers[i][j]);
					}
					catch (NotBoundException re) { System.out.println(re) ; }
					catch (RemoteException re) { System.out.println(re) ; }
					catch (MalformedURLException e) { System.out.println(e) ; }
				}
			}

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
