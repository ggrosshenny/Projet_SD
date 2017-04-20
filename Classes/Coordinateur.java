import java.rmi.*;
import java.util.Random;
        
public class Coordinateur {
        
    public Coordinateur() {}

	public static void endGame(String idJoueur, String[] Joueurs, String[][] Producteurs){
		
		JoueurImpl tempJoueur;
		ProducteurImpl tempProd;
		
		for(int i = 0; i < Joueurs.length; i++){
			
			tempJoueur = (JoueurImpl)Naming.lookup(Joueurs[i]);
			tempJoueur.gameIsOver(idJoueur);
		}
		for(int i = 0; i < Producteurs.length; i++){
			for(int j = 0; j < Producteurs[i].length; j++){
				
				tempProd = (ProducteurImpl)Naming.lookup(Producteurs[i][j]);
				tempProd.gameIsOver(idJoueur);
			}
		}
	}
        
    public static void main(String args[]) {
		
		if (args.length != 4)
		{
			System.out.println("Usage : java Coordinateur <machine du Serveur> <port du rmiregistry> <liste_Joueurs> <liste_Producteurs> <nombre_joueurs> <nombre_producteurs> ") ;
			System.exit(0) ;
		}
		
		String[] Joueurs;
		String[][] Producteurs;
		JoueurImpl tempJoueur;
		ProducteurImpl tempProd;	
		int nb_players = Integer.parseInt(args[4]);
		int nb_producers = Integer.parseInt(args[5]);
		
        
        try {
            int i;
            for(i = 0; i < nb_players; i++){
				Joueurs[i] = args[2][i];
			}
			for(i = 0; i < nb_producers; i++){
				tempProd = (ProducteurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[3][i]);
				Producteurs[tempProd.getTypeOfRsc()][Producteurs[tempProd.getTypeOfRsc()].length] = args[3][i];
			}
			
			for(i = 0; i < Joueurs.length; i++){
				tempJoueur = (JoueurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + Joueurs[i]);
				tempJoueur.begin(Joueurs, Producteurs);

			}
			for(i = 0; i < Producteurs.length; i++){
				for(int j = 0; j < Producteurs[i].length; j++){
					tempProd = (ProducteurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + Producteurs[i][j]);
				}
			}
			
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
