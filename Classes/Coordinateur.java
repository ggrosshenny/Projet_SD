import java.rmi.*;
import java.util.Random;
import java.net.MalformedURLException;
        
public class Coordinateur {
        
    public Coordinateur() {}

	public static void endGame(String idJoueur, String[] Joueurs, String[][] Producteurs){
		
		JoueurImpl tempJoueur;
		ProducteurImpl tempProd;
		
		for(int i = 0; i < Joueurs.length; i++){
			
			try {			
				tempJoueur = (JoueurImpl)Naming.lookup(Joueurs[i]);
				tempJoueur.gameIsOver(idJoueur);
			} 
			catch (NotBoundException re) { System.out.println(re) ; }
			catch (RemoteException re) { System.out.println(re) ; }
			catch (MalformedURLException e) { System.out.println(e) ; }
			
		}
		for(int i = 0; i < Producteurs.length; i++){
			for(int j = 0; j < Producteurs[i].length; j++){
				
				try {
					tempProd = (ProducteurImpl)Naming.lookup(Producteurs[i][j]);
					tempProd.gameIsOver(idJoueur);
				} 
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
			}
		}
	}
        
    public static void main(String args[]) {
		
		if (args.length < 6)
		{
			System.out.println("Usage : java Coordinateur <machine du Serveur> <port du rmiregistry> <nombre_joueurs> <nombre_producteurs> <liste_Joueurs> <liste_Producteurs> ") ;
			System.exit(0) ;
		}
		
		String[] Joueurs = null;
		String[][] Producteurs = null;
		JoueurImpl tempJoueur = null;
		ProducteurImpl tempProd = null;	
		int nb_players = Integer.parseInt(args[2]);
		int nb_producers = Integer.parseInt(args[3]);
		
        
        try {
            int i;
            for(i = 0; i < nb_players; i++){
				Joueurs[i] = args[4+i];
			}
			for(i = 0; i < nb_producers; i++){
				try {
					tempProd = (ProducteurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[5+i]);
					Producteurs[tempProd.getTypeOfRsc()][Producteurs[tempProd.getTypeOfRsc()].length] = args[5+i];
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
				
			}
			
			for(i = 0; i < Joueurs.length; i++){
				try {
					tempJoueur = (JoueurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + Joueurs[i]);
					tempJoueur.begin(Joueurs, Producteurs);
				}
				catch (NotBoundException re) { System.out.println(re) ; }
				catch (RemoteException re) { System.out.println(re) ; }
				catch (MalformedURLException e) { System.out.println(e) ; }
			}
			for(i = 0; i < Producteurs.length; i++){
				for(int j = 0; j < Producteurs[i].length; j++){
					
					try {
						tempProd = (ProducteurImpl)Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + Producteurs[i][j]);
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
