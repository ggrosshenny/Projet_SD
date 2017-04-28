#!/bin/sh


# Vérification du nombre d'arguments
# option -pl : logfile d'un joueur
# option -pr : logfile d'un producteur
if [ $# -ne 4 ]
then
	echo 'usage : '$0' [-pr || -pl] <idGame> <idJoueur> <nbTypeOfRsc>'
	exit 1
fi

# On récupère le nom du fichier de logs à modéliser
if [ $1 = -pr ]
then
	filename='../logs/logs_game_'${2}'/log_producer_'
else
	if [ $1 = -pl ]
	then
		filename='../logs/logs_game_'${2}'/log_player_'
	else
		echo 'usage : '$0' [pr || pl] <idGame> <idJoueur> <nbTypeOfRsc>'
		exit 1
	fi
fi
logfilename=${filename}$3".bat"
nbRsc=$4

# Si le fichier de logs existe, alors pour chaque bloc de données de ce fichier on trace une courbe dans un fichier .png grâce à gnuplot
if [ -f $logfilename ]
then
	outputfile=${filename}$3".png"
	echo "set term png; set output '"${outputfile}"'; set title 'player "$3" logs'; set key outside; plot for [IDX=0:"$nbRsc"-1] '"${logfilename}"' index IDX u 1:2 title columnheader(1) with lines lt IDX" | gnuplot
	see ${outputfile} &
else
	echo 'Logfile not found'
	exit 1
fi
exit 0

