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
		/*
		int nb_players = Integer.parseInt(args[2]);
		int nb_producers = Integer.parseInt(args[3]);
		String[] Joueurs = new String[nb_players];
		String[][] Producteurs = new String[nb_producers][nb_producers];
		*/
        try {
			/*
            int i;
            int j = 0;
            for(i = 0; i < nb_players; i++){
				Joueurs[i] = "rmi://" + args[0] + ":" + args[1] + "/" + args[4+i];
			}
			for(i = 0; i < nb_producers; i++){
				try {
					System.out.println("avant : " + "rmi://" + args[0] + ":" + args[1] + "/" + args[4+nb_players+i]);
					tempProd = (Producteur) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[4+nb_players+i]);
					System.out.println("avant : " + Producteurs[tempProd.getTypeOfRsc()].length + " , " );
					while(Producteurs[tempProd.getTypeOfRsc()][j] != null){
						j++;
					}
					Producteurs[tempProd.getTypeOfRsc()][j] = "rmi://" + args[0] + ":" + args[1] + "/" + args[4+nb_players+i];
					System.out.println("Apres");
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }

			}
			*/
			
			CoordinateurImpl Coord = new CoordinateurImpl(args);
			Naming.rebind(args[args.length - 1], Coord);
			
			int i, j;
			
			for(i = 0; i < Coord.Players.length; i++){
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
