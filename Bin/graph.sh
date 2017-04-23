#!/bin/sh

if [ $# -ne 2 ]
then
	echo 'usage : $0 <idJoueur> <nbTypeOfRsc>'
	exit 1
fi

filename='log_file_'
logfilename=${filename}$1
nbRsc=$2

if [ -f $logfilename ]
then
	cat << __EOF | gnuplot 
	set term canvas mousing size 500,500 
	plot <$logfilename> u 1:2 with lines lt IDX+1 
	__EOF
else
	echo 'File not found'
	exit 1
fi
exit 0

