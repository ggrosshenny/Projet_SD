import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.random;
        
public class Coordinateur {
        
    public Coordinateur() {}

	public static void endGame(int idJoueur){
		for(int i = 0; i < Joueurs.length(); i++){
			
			Joueurs[i].gameIsOver(idJoueur);
		}
		for(int i = 0; i < Producteurs.length(); i++){
			for(int j = 0; j < Producteurs[i].length(); j++){
				Producteurs[i][j].gameIsOver(idJoueur);
	}
        
    public static void main(String args[]) {
		
		if (args.length != 4)
		{
			System.out.println("Usage : java Coordinateur <machine du Serveur> <port du rmiregistry> <liste_Joueurs> <liste_Producteurs") ;
			System.exit(0) ;
		}
		
		Joueur[] Joueurs;
		ProducteurServeur[][] Producteurs;
		
        
        try {
            int i;
            for(i = 0; i < args[2].length(); i++){
				Joueurs[i] = naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[2][i]);
			}
			for(i = 0; i < args[3].length(); i++){
				Producteurs[args[3][i].production.getType()][Producteurs[args[3][i].production.getType()].length()] = naming.lookup("rmi://" + args[0] + ":" + args[1] + "/" + args[3][i]);
			}
			
			for(i = 0; i < Joueurs.length(); i++){
				Joueurs[i].SendLists(Joueurs, Producteurs);
			}
			for(i = 0; i < Producteurs.length(); i++){
				for(int j = 0; j < Producteurs[i].length(); j++){
					Producteurs[i][j].SendLists(Joueurs, Producteurs);
				}
			}
			
           System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
