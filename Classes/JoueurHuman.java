// Class joueurHuman

import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.Scanner;

public class JoueurHuman extends JoueurCommon implements Runnable
{
  // Attributes
  int nbTurnToWait;
  int[] stockStatus;
  Scanner inputReader;
  String input;
  Producteur produ;


  // Methods


  public JoueurHuman(JoueurImpl playerServ0, String id0, String coord0, int amountToTake0)
  {
    super(playerServ0, id0, coord0, amountToTake0);
    this.nbTurnToWait = 3;
    this.inputReader = new Scanner(System.in);
    this.input = null;
    this.produ = null;
  }


  /**
  * Method : takeRessourceCmd
  * Param : void
  * Desc : execute the take ressource command by seeking a producer for the given ressource
  * Return : void
  **/
  public void takeRessourceCmd()
  {
    int inputRscType = 0;
    int nb_attempt = 3;

    // ----------------------------
    // Asking for a ressource type
    System.out.println("Veuillez entrer le type de ressource souhaitée.");
    input = inputReader.nextLine();

    // Verifying input data
    while(((input.length() > 1) || ((Integer.parseInt(input) > this.stock.length-1) || (Integer.parseInt(input) < 0))) && nb_attempt > 0)
    {
      System.out.println("Veuillez rentrer un chiffre compris entre 0 et " + (this.stock.length-1) + " svp. " + nb_attempt + " tentatives restantes.");
      input = inputReader.nextLine();
      nb_attempt--;
    }

    if(nb_attempt > 0)
    {
      inputRscType = Integer.parseInt(input);
    }
    else
    {
      System.out.println("Vous avez rentré une donnée incorrecte. Un type de ressource aléatoire sera sélectionné.");
      inputRscType = chooseRscType();
    }

    try
    {
      // Seeking for producer and/or taking ressources
      if((produ == null) || (produ.getAmountRsc() < amountToTake) || (stock[produ.getTypeOfRsc()].amountForVictoryIsReached()))
      {
        produ = takeRessourceFromNewProducer(inputRscType);
      }
      else
      {
        takeRessource(produ);
      }
      // Reset of protection ability
      this.wasUnderProtection = false;
      // Control message
      if(produ != null)
      {
        System.out.println("Valeur de rsc " + " : " + stock[produ.getTypeOfRsc()].getAmount() + "/" + stock[produ.getTypeOfRsc()].getAmountForVictory() + " (" + produ.getID() + " : " + produ.getTypeOfRsc() + ")");
      }
    }
    catch (RemoteException re) { System.out.println(re) ; }

  }



  /**
  * Method : stealCmd
  * Param : void
  * Desc : Steal the given ressource of the given player.
  * Return : void
  **/
  public void stealCmd()
  {
    int inputRscType = 0;
    int inputPlayerId = 0;
    int nb_attempt = 3;
    String playerID = new String("");

    // ----------------------------
    // Asking for a player id
    System.out.println("Veuillez entrer le numero d'un joueur (Il faut entrer le numero et non l'id complet)");
    input = inputReader.nextLine();

    // Verifying input data
    while(((input.length() > 1) || (Integer.parseInt(input) > this.players.length) || (Integer.parseInt(input) < 1)) && (nb_attempt > 0))
    {
      System.out.println("Veuillez rentrer un chiffre compris entre 1 et " + this.players.length + " svp. " + nb_attempt + " tentatives restantes.");
      input = inputReader.nextLine();
      nb_attempt--;
    }

    if(nb_attempt > 0) // The input is valid
    {
      inputPlayerId = Integer.parseInt(input);
    }
    else
    {
      System.out.println("Vous avez rentré une donnée incorrecte. Un joueur aléatoire sera sélectionné.");
      inputPlayerId = this.rand.nextInt(this.players.length);
    }
    playerID = "Player_" + Integer.toString(inputPlayerId);

    // ----------------------------
    // Asking for a ressource type
    System.out.println("Veuillez entrer le type de ressource souhaitée.");
    input = inputReader.nextLine();

    // Verifying input data
    while(((input.length() > 1) || ((Integer.parseInt(input) > this.stock.length-1) || (Integer.parseInt(input) < 0))) && nb_attempt > 0)
    {
      System.out.println("Veuillez rentrer un chiffre compris entre 0 et " + (this.stock.length-1) + " svp. " + nb_attempt + " tentatives restantes.");
      input = inputReader.nextLine();
      nb_attempt--;
    }

    // Verifying input data
    if(nb_attempt > 0)
    {
      inputRscType = Integer.parseInt(input);
    }
    else
    {
      System.out.println("Vous avez rentré une donnée incorrecte. Un type de ressource aléatoire sera sélectionné.");
      inputRscType = chooseRscType();
    }

    // ----------------------------
    // Time to steal !
    // Reset of protection ability
    this.wasUnderProtection = false;
    takeRscFromPlayer(playerID, inputRscType);
  }


  /**
  * Method : observeCmd
  * Param : void
  * Desc : Observe the stock of all players.
  * Return : void
  **/
  public void observeCmd()
  {
    int bestRscScore = 0;
    int i = 0;
    int j = 0;
    observeAllPlayers();
    bestRscScore = getRscTypeWithMaxAmount();

    System.out.println("J'ai regardé tout le systeme : ");
    for(i=0; i<this.players.length; i++)
    {
      for(j=0; j<this.stock.length; j++)
      {
        System.out.println("le joueur " + (i+1) + " a " + observationResult[i][j] + " d'unités de la ressource : " + j);
      }
    }
    System.out.println("La ressource ayant le plus d'unités est : " + bestRscScore);
  }


  /**
  * Method : protectCmd
  * Param : void
  * Desc : protecting from stealing
  * Return : void
  **/
  public void protectCmd()
  {
    isProtectingFromStealing(3);
  }


  /**
  * method : stalkCmd
  * param : void
  * Desc : execute the stalk command
  * Return : void
  **/
  public void stalkCmd()
  {
    int inputRscType = 0;
    int nb_attempt = 3;

    // ----------------------------
    // Asking for a ressource type
    System.out.println("Veuillez entrer le type de ressource souhaitée.");
    input = inputReader.nextLine();

    // Verifying input data
    while(input.length() > 1 && nb_attempt > 0)
    {
      System.out.println("Veuillez rentrer un chiffre svp. " + nb_attempt + " tentatives restantes.");
      input = inputReader.nextLine();
    }

    if(nb_attempt > 0)
    {
      inputRscType = Integer.parseInt(input);
    }
    else
    {
      System.out.println("Vous avez rentré une donnée incorrecte. Un type de ressource aléatoire sera sélectionné.");
      inputRscType = chooseRscType();
    }

    observeProducer(inputRscType);
  }


  /**
   * Method : run
   * Param : void
   * Desc : Implement the comportement of the player for the ressource seeking,
   *        add its actions to the log and call the central server when all objectives
   *        are completed.
   * Return : void
   **/
  public void run()
  {
    int i = 0;
    int j = 0;
    int rscToTake = 0;
    int lastRscTaken = 0;
    int temp = 0;
    int nbTry = 3;
    boolean finished = false;
    boolean wrongCmd = true;
    Producteur produ = null;
    this.stockStatus = new int[stock.length];

    // Waiting on starting blocks !
    try
    {
      coord.AgentReady(); // Say to the coordinator that we are ready
    }
    catch (RemoteException re) { System.out.println(re) ; }

    // Control message
    System.out.println("La partie commence ! Les commandes possibles sont : ");
    System.out.println("  * get ressource - prendre une ressource d'un producteur");
    System.out.println("  * steal - voler une ressource d'un type donné à un joueur donné");
    System.out.println("  * Observe - observer les stocks des joueurs adverses");
    System.out.println("  * protect - se proteger des vols des autres joeuurs (coûte 3 tours !)");
    System.out.println("  * stalk - observer un producteur d'une ressource donnée");

    while(!finished && running)
    {
      synchronized(playerServ)
      {
        try
        {
          playerServ.wait();
        }
        catch (InterruptedException ie) {System.out.println(ie) ;}
      }

      // Verify if we are allowed to play
      if(isPunnished)
      {
        // Wait until we can play again
        if(nbTurnToWait > 0)
        {
          System.out.println("Vous ne pouvez pas jouer. Vous avez été pénalisé pour avoir été attrapé en essayant de voler un joueur. Il vous reste : " + nbTurnToWait + " à attendre.");
          nbTurnToWait--;
        }
        else
        {
          isPunnished = false;
        }
      }

      // Verify if we are not observing to protect ourself from stealing
      if(isWatchingTime > 0) // If we are protecting from steal
      {
        this.isWatchingTime--;
        if(isWatchingTime == 0)
        {
          playerServ.protectFromStealing(false);
        }
        else
        {
          System.out.println("Vous vous protegé du vol, vous ne pouvez donc pas chercher de ressource.");
        }
      }

      // If we are not punnished or observing, we can play
      if(!isPunnished && isWatchingTime == 0)
      {
        System.out.println("Voici votre stock : ");
        for(i=0; i<stock.length; i++)
        {
          if(stock[i] != null)
          {
            System.out.println("  ressource " + i + " : " + stock[i].getAmount());
          }
        }
        System.out.println("C'est à vous de jouer ! Veuillez entrer une commande svp.");

        // Treatement of the input command
        while((wrongCmd) && (nbTry > 0))
        {
          // Get the input command
          input = inputReader.nextLine();
          switch(input)
          {
            case "get ressource":
              takeRessourceCmd();
              wrongCmd = false;
              break;
            case "steal":
              stealCmd();
              wrongCmd = false;
              break;
            case "observe":
              observeCmd();
              wrongCmd = false;
              break;
            case "protect":
              protectCmd();
              wrongCmd = false;
              break;
            case "stalk":
              stalkCmd();
              wrongCmd = false;
              break;
            default:
              System.out.println("Veuillez rentrer une commande valide ! Il vous reste " + nbTry + " iterations. Voici les commandes :");
              System.out.println("  * get ressource - prendre une ressource d'un producteur");
              System.out.println("  * steal - voler une ressource d'un type donné à un joueur donné");
              System.out.println("  * Observe - observer les stocks des joueurs adverses");
              System.out.println("  * protect - se proteger des vols des autres joeuurs (coûte 3 tours !)");
              System.out.println("  * stalk - observer un producteur d'une ressource donnée");
              nbTry--;
              break;
          }
        }
        nbTry = 3;
        wrongCmd = true;
      }

      // Verifying if all objectives are completed
      finished = true;
      for(i=0; i<stock.length; i++)
      {
        if(stock[i] != null)
        {
          finished = finished && stock[i].amountForVictoryIsReached();
        }
      }

      // Signal to coordinator to say that the end is finished
      if(!finished)
      {
        try
        {
          this.coord.endOfTurn(this.id);
        }
        catch (RemoteException re) { System.out.println(re) ; }
      }

      // Write the logs with coordinator
      for(i=0; i<stock.length; i++)
      {
        stockStatus[i] = stock[i].getAmount();
      }
      try
      {
        this.coord.setLog(this.id, this.stockStatus);
      }
      catch (RemoteException re) { System.out.println(re) ; }
    }

    // If we had completed all objectives
    if(finished)
    {
      try
      {
        finished = this.coord.endGame(this.id);
      }
      catch (RemoteException re) { System.out.println(re) ; }
    }

    // While to verify if we are winner or if we have to wait for another winner message.
    // This activ waiting is not really bad, because its duration will not be longer than a few amount of ms
    while(!finished && running)
    {
    }

    System.out.println("Le resultat de votre partie est : ");
    for(i=0; i<stock.length; i++)
    {
      if(stock[i] != null)
      {
        System.out.println("  ressource " + i + " : " + stock[i].getAmount());
      }
    }

  }
}
