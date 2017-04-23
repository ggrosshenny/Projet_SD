#!/bin/sh

if [ $# -ne 2 ]
then
	echo "usage : $0 <idJoueur> <nbTypeOfRsc>"
	exit 1
fi

filename = "log_file_"
logfilename = ${filename}$1
nbRsc = $2

if [ -f $logfilename ]
then
	plot for [IDX=0:nbRsc] $logfilename u 1:nbRsc with lines lt IDX+1
else
	echo "File not found"
	exit 1
fi


