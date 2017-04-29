#!/bin/sh


# Vérification du nombre d'arguments
# option -pl : logfile d'un joueur
# option -pr : logfile d'un producteur

if [ $# -lt 3 ]
then
	echo 'usage : '$0' [-pr || -pl] <idGame> <idJoueur> <nbTypeOfRsc>'
	exit 1
fi

# On récupère le nom du fichier de logs à modéliser
if [ $1 = -pr ]
then
	if [ $# -ne 3 ]
	then
		echo 'usage : '$0' -pr <idGame> <idJoueur>'
		exit 1
	fi
	filename='../logs/logs_game_'${2}'/log_producer_'
	title='producer '${3}' logs'
	nbRsc=1
else
	if [ $1 = -pl ]
	then
	    if [ $# -ne 4 ]
		then
			echo 'usage : '$0' -pl <idGame> <idJoueur> <nbTypeOfRsc>'
			exit 1
		fi
		filename='../logs/logs_game_'${2}'/log_player_'
		title='player '${3}' logs'
		nbRsc=$4
	else
		echo 'usage : '$0' [pr || pl] <idGame> <idJoueur> <nbTypeOfRsc>'
		exit 1
	fi
fi
logfilename=${filename}$3".bat"


# Si le fichier de logs existe, alors pour chaque bloc de données de ce fichier on trace une courbe dans un fichier .png grâce à gnuplot
if [ -f $logfilename ]
then
	outputfile=${filename}$3".png"
	echo "set term png; set output '"${outputfile}"'; set title '"${title}"'; set key outside; plot for [IDX=0:"$nbRsc"-1] '"${logfilename}"' index IDX u 1:2 title columnheader(1) with lines lt IDX" | gnuplot
	see ${outputfile} &
else
	echo 'Logfile not found'
	exit 1
fi
exit 0

