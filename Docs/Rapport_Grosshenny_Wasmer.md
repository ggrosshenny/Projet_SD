title: Rapport du projet de Système Distribué date: 2017-03-20 author: Guillaume Grosshenny && Audric Wasmer ...

\newpage

\tableofcontents

\newpage
Mode d'emploi

Pour démarrer la partie, il faut utiliser lancer le script run.sh:

    sh run [port] [nbProd] [nbJoueurs] [nbTypeOfRsc] [AmountAtStart] [AmountToTakeForVictory] [TimeBeforeProduction] [isTurnByTurn] [nbJoueursCoop] [nbJoueursIndiv] [nbJoueursHumains]

A la fin de la partie, il faut executer le script stop.sh.

Pour créer le graphique des ressources d'un joueur ou d'un producteur donné, il faut lancer le script graph.sh:

    Général : sh graph.sh (-pr || -pl) [idGame] [idJoueur] [nbTypeOfRsc]
    Producteur : sh graph.sh -pr [idGame] [idJoueur]
    Joueur : sh graph.sh -pl [idGame] [idJoueur] [nbTypeOfRsc]

Exemple de test:

    sh run.sh 0 4 4 3 50 25 2000 0 2 2 0
    sh stop.sh
    sh graph.sh -pl 0 2 3

Description du projet
Description

Le projet consiste à implementer un jeu d'interactions entre des agents dans le cadre de la théorie des jeux. Les joueurs doivent remplir des objectifs (ici, chaque joueur doit avoir un nombre donné d'unités de toutes les ressources existantes dans la partie) pour remporter la victoire. Cependant, si aucun joueur n'a atteint ces objectifs après un délai donné alors que tous les producteurs ne peuvent plus proposer de ressources, la partie se termine sans vainqueur. Pour ce faire, différents agents font partie du jeu :

    Les joueurs qui doivent atteindre les objectifs donnés:
        Des joueurs individualistes (qui auront tendance à vouloir empêcher les autres joueurs d'atteindre l'objectif donné)
        Des joueurs coopératifs (qui auront tendance à vouloir atteindre les objectifs sans spécialement gener les autres joueurs)
        Des joueurs humains
    Les producteurs qui doivent produire des ressources au fil du temps
    Le coordinateur qui initialise la partie et la termine

Le jeu peut se dérouler sous deux modes différents, en continu ou en tour par tour. Dans le cas de la présence d'un joueur humain dans la partie, elle est automatiquement en tour par tour.
Objectifs

L'objectif de cette simulation est de pouvoir effectuer des statistiques sur l'efficacité des comportements de chaque joueur à accomplir une même mission dans des conditions pouvant varier.
Description technique
Technologies utilisées

Les technologies utilisées sont les suivantes :

    Langage : JAVA
    Technologie de communication entre les objets distants : RMI
    Script de lancement et d'arrêt du programme : sh
    Technologie utilisée pour la visualisation à postériori : gnuplot

Architecture du projet

Diagramme_classes
Explicitation des règles
Règles du jeu

Les règles sont les suivantes :

    Pour les joueurs:
        Un joueur doit obtenir un certain nombre d'unités de ressource pour chaque ressource de la partie
        Un joueur peut voler un autre joueur, cependant s'il se fait attraper, il subit une penalité de temps
        Un joueur peut se défendre des vols en s'observant soit-même afin d'attraper les voleurs
        Un joueur peut observer un producteur donné afin de connaitre ses stocks
        Un joueur peut observer la partie et connaître les stocks des autres joueurs
    Pour les producteurs:
        Un producteur donne des ressources aux joueurs qui les demande
        Un producteur produit des ressources au fil du temps suivant la loi: $(n/2 +1)$
        Un producteur ne produit pas de ressources indéfiniment, elle sont épuisables.
    Pour les fins de partie:
        Une partie se termine lorsqu'un joueur a obtenu le nombre d'unités demandé pour chaque ressource existante
        Une partie se termine lorsque tous les producteurs sont vides et qu'aucun joueur n'arrive à atteindre les objectifs avant un certain temps.

Explication des choix

Nous avons choisi d'autoriser les joueurs à se voler entre eux pour rendre les parties plus intéressantes et diminue l'avantage que peut avoir un joueur en commençant la partie. La partie se termine lorsque les producteurs sont tous vides pour éviter les cas où la partie ne termine pas ou au bout d'un temps trop long (les joueurs peuvent se voler entre eux, mais la partie peut ne pas se terminer).