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
		
		IJoueur[] tab_joueurs = new IJoueur[Integer.parseInt(args[2])];
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
					if(Coord.Producers[i][j] != null)
						System.out.println("PRODUCTEURS : " + Coord.Producers[i][j]);
				}
			}

			for(i = 0; i < Coord.Players.length; i++){
				try {
					tab_joueurs[i] = (IJoueur)Naming.lookup(Coord.Players[i]);
					tab_joueurs[i].begin(Coord.Players, Coord.Producers);
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
			}
			
			for(i = 0; i < tab_joueurs.length; i++){
				tab_joueurs[i].startGame();
			}
			
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
