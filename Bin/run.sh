# Script de lancement du projet
# Grosshenny Guillaume & Wasmer Audric

# Verification des arguments
if [ $# -ne 7 ]
then
    echo "usage : $0 <port> <nbProd> <nbJoueurs> <nbTypeOfRsc> <AmountAtStart> <AmountToTakeForVictory> <TimeBeforeProduction>"
    exit 1
fi

# Verification des arguments 2
if [ $2 -lt $4 ]
then
    echo "Warning, the number of different types of ressource is greater than the number of producers. Please, retry with a correct producers number or a correct type of ressource number."
    exit 1
fi

# Creation du rmi registry

rmiregistry &


# Creation du répertoire de log et suppression des fichiers logs exitants
if [ -e ../logs ]
then
	rm -rf ../logs/*.bat
	rm -rf ../logs/*.png
else
	mkdir ../logs
fi

echo "Creating game... Please hold on."

# Addresse du coordinateur
Coordinateur="rmi://localhost:$1/coordinateur"

# Création de la liste des ids des producteurs allant de 1 à nbProd
ProdIDList=' '

for i in $(seq -w 1 $2)
do
  ProdIDList="$ProdIDList Prod_$i"
done

echo $ProdIDList


# Création de la liste des ids des joueurs allant de 1 à nbJoueurs
PlayerIDList=' '

for i in $(seq -w 1 $3)
do
  PlayerIDList="$PlayerIDList Player_$i"
done

echo $PlayerIDList


# Création des producteurs
rscType=-1
rscTypeCtrlList=' '
for i in $ProdIDList
do
  rscType=$((($rscType+1)%$4))
  rscTypeCtrlList="$rscTypeCtrlList $rscType"
  xterm -e java ProducteurServeur $1 $i $Coordinateur $rscType $5 $6 $7 &
done

echo $rscTypeCtrlList

# Création des joueurs
for i in $PlayerIDList
do
  xterm -e java Joueur localhost $1 $i 0 $Coordinateur 0 0 $4 &
done


# Création du coordinateur de la partie
sleep 2
java Coordinateur localhost $1 $3 $2 $PlayerIDList $ProdIDList $Coordinateur 0 $4 &
echo "Done."

exit 0
