# Script de lancement du projet
# Grosshenny Guillaume & Wasmer Audric

# Verification des arguments
if [ $# -ne 11 ]
then
    echo "usage : $0 <port> <nbProd> <nbJoueurs> <nbTypeOfRsc> <AmountAtStart> <AmountToTakeForVictory> <TimeBeforeProduction> <isTurnByTurn> <nbJoueursCoop> <nbJoueursIndiv> <nbJoueursHumains>"
    exit 1
fi

# Verification des arguments 2
if [ $2 -lt $4 ]
then
    echo "Warning, the number of different types of ressource is greater than the number of producers. Please, retry with a correct producers number or a correct type of ressource number."
    exit 1
fi

# Verification des arguments 3 : nombre de joueurs
if [ `expr ${9} + ${10} + ${11}` -ne $3 ]
then
	echo 'Warning, total number of Coop & Indiv & Human players is greater than the actual number of players passed as the third argument.'
	exit 1
fi

# Si au moins un joueur humain est annoncé, la partie passe automatique en tour-par-tour
TbT=$8
if [ ${11} -gt 0 ]
then
	TbT=1
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
idxTypeJoueur=0
for i in $PlayerIDList
do
  if [ $idxTypeJoueur -lt $8 ]
  then
	xterm -e java Joueur localhost $1 $i 0 $Coordinateur 0 $TbT $4 0 &
    idxTypeJoueur=`expr $idxTypeJoueur + 1`
  else
    if [ $idxTypeJoueur -lt `expr $9 + $10` ]
    then
      xterm -e java Joueur localhost $1 $i 0 $Coordinateur 1 $TbT $4 0 &
      idxTypeJoueur=`expr $idxTypeJoueur + 1`
    else
      xterm -e java Joueur localhost $1 $i 0 $Coordinateur 0 $TbT $4 1 &
    fi    
  fi
done


# Création du coordinateur de la partie
sleep 2
java Coordinateur localhost $1 $3 $2 $PlayerIDList $ProdIDList $Coordinateur $TbT $4 &
echo "Done."

exit 0
