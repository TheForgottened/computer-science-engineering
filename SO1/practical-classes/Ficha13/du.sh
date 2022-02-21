#!/bin/bash

# Script que explica utilização do debug
# Mostar ser invocado da seginte forma: "bash - xv script.sh“
# Mostar ser invocado da seginte forma: "./script.sh" e descomentar a seguinte linha

# Ativar o debug
#set –xv

# constantes
LOG_DIR="/var/log“

# Entrar no diretório
cd $LOG_DIR

# mostrar o espaço usado pelos ficheiros
for i in "*.log"; do
  du -sh $i
done

