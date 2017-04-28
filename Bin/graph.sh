#!/bin/sh


# Vérification du nombre d'arguments
if [ $# -ne 2 ]
then
	echo 'usage : '$0' <idJoueur> <nbTypeOfRsc-1>'
	exit 1
fi

# On récupère le nom du fichier de logs à modéliser
filename='../logs/log_player_'
logfilename=${filename}$1".bat"
nbRsc=$2

# Si le fichier de logs existe, alors pour chaque bloc de données de ce fichier on trace une courbe dans un fichier .png grâce à gnuplot
if [ -f $logfilename ]
then
	outputfile=${filename}$1".png"
	echo "set term png; set output '"${outputfile}"'; set title 'player "$1" logs'; set key outside; plot for [IDX=0:"$nbRsc"-1] '"${logfilename}"' index IDX u 1:2 title columnheader(1) with lines lt IDX" | gnuplot
	see ${outputfile} &
else
	echo 'File not found'
	exit 1
fi
exit 0

