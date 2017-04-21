# Script de lancement du projet
# Grosshenny Guillaume & Wasmer Audric

# Verification des arguments
if [ $# -ne 3 ]
then
    echo "usage : $0 <port> <nbProd> <nbJoueurs>"
    exit 1
fi

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
for i in $ProdIDList
do
  xterm -e java ProducteurServeur $1 $i $Coordinateur 0 50 25 5000 &
done


# Création des joueurs
for i in $PlayerIDList
do
  xterm -e java Joueur localhost $1 $i 0 $Coordinateur 0 1 &
done


# Création du coordinateur de la partie
echo "Création de la partie..."
sleep 2
java Coordinateur localhost $1 $2 $3 $PlayerIDList $ProdIDList $Coordinateur &
echo "Done."

exit 0
