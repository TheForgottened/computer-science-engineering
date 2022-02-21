#!/bin/bash

# Script para contar o número de ficheiros num diretório
# Utilizador indica o diretório

if [ $# -ne 1 ]
then
	echo "Recebi $# argumentos."
	echo "Utilização: %0 [diretório]."
	exit -1
else
	echo "Parametros corretos. Vou contar o nr. de ficheiros."
fi

echo "No diretorio $1 existem $(ls -lA $1 | tail -n +2 | wc -l) ficheiros."


