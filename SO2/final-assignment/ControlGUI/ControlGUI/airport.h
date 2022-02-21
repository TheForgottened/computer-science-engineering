#ifndef AIRPORT_H
#define AIRPORT_H

#include <tchar.h>
#include <windows.h>
#include <stdlib.h>
#include <string.h>

#include "utils.h"
#include "structs.h"

// Função que cria uma estrutura de um aeroporto
BOOL initAirport(pAirport this, unsigned int* airportsSize, TCHAR* name, int x, int y);

// Função que inicia o array de aeroportos com valores default
void initAirportsArray(pAirport airportsArray, unsigned int maxAirports);

// Mostra info do array de aeroportos
void printAirportsArray(pAirport airportsArray, unsigned int airportsArraySize);

// Verifica se é possível criar um novo aeroporto com as características específicadas (Nome nao repetido e coordenadas ´nao em cima de outro aeroporto)
BOOL isAirportValid(pAirport airpotsArray, unsigned int airportsArraySize, TCHAR* newAirportName, int newAirportX, int newAirportY);

#endif