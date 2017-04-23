#!/bin/sh

if [ $# -ne 2 ]
then
	echo 'usage : $0 <idJoueur> <nbTypeOfRsc>'
	exit 1
fi

filename='log_player_'
logfilename=${filename}$1".bat"
nbRsc=$2

if [ -f $logfilename ]
then
	outputfile=${filename}$1".png"
	echo "set term png; set output '"${outputfile}"'; plot for [IDX=0:"$nbRsc"] '"${logfilename}"' index IDX u 1:2 with lines lt IDX+1" | gnuplot
	see ${outputfile}
else
	echo 'File not found'
	exit 1
fi
exit 0

