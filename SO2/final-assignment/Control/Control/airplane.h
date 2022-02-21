#ifndef AIRPLANE_H
#define AIRPLANE_H

#include <tchar.h>
#include <windows.h>
#include <stdlib.h>
#include <string.h>

#include "utils.h"
#include "structs.h"

// Função que cria uma estrutura avião
BOOL initAirplane(pAirplane this, pAirport airportsArray, unsigned int airportsSize, unsigned int* airplanesSize, unsigned int capacity, unsigned int velocity, TCHAR* srcAirport);

// Função que inicializa o array de aviões com valores default
void initAirplanesArray(pAirplane airplanesArray, unsigned int maxAirplanes);

// Copia avião src para avião dest
void copyAirplane(pAirplane dest, airplane src);

// Mostra info do array de aviões
void printAirplanesArray(pAirplane airplanesArray, unsigned int airplanesArraySize);

void printAirplane(airplane thisAirplane);

// Remove avião do array de avioes
BOOL removeAirplaneFromArray(pAirplane airplaneArray, unsigned int* airplanesArraySize, unsigned int id, HANDLE* hThreadPingArray);

#endif