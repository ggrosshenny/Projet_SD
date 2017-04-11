import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.random;
        
public class Coordinateur {
        
    public Coordinateur() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
		
		if (args.length != 4)
    {
		System.out.println("Usage : java Client <machine du Serveur> <port du rmiregistry> <nb_Joueurs> <nb_Producteurs") ;
		System.exit(0) ;
    }
        
        try {
            Coordinateur obj = new Coordinateur();
            Agent stub = (Agent) UnicastRemoteObject.exportObject(obj, 0);
            
            int nb_joueurs = args[3];
            int nb_producteurs = args[4];
            Agent[] agents;
            Ressource[] objectifs;
			Ressource[] objectifs_joueur;
            Random rand = new Random();
            int randNbRessources = rand.nextInt(nb_producteurs + 1);
			int randRessourceID_prod;
			
			for(int i = 0; i < randNbRessources; i++){
				objectifs[i] = new Ressource(i, 0, 100);
			}
            
            for(int i = 0; i < nb_joueurs + nb_producteurs; i++){
					if(i <= nb_producteurs){
						/* Déterminer aléatoirement quelle ressource lui attribuer (fuck eduroam) */
						if(i <= randNbRessources){ // On s'assure qu'il y ait au moins 1 producteur pour chaque ressource
							agents[i] = new Producteur(i, 0, /*addr*/, /*coord*/, objectifs[i] );
						}
						else{
							randRessourceID_prod = rand.nextInt(randNbRessources);
							agents[i] = new Producteur(i, 0, /*addr*/, /*coord*/, objectifs[randRessourceID_prod] );
						}
					}
					else{
						for(int j = 0; j < randNbRessources; j++){
							objectifs_joueur[j] = objectifs[j].copy();
						}
						agents[i] = new JoueurImpl(i, 1, /*addr*/, /*coord*/, objectifs_joueurs, nb_joueurs);
					}						
			}

            // Bind the remote object's stub in the registry
           Naming.rebind( "rmi://localhost:" + args[0] + "/JoueurImpl" ,stub) ;
           System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
