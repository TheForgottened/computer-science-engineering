#ifndef PASSENGER_H
#define PASSENGER_H

#include <tchar.h>
#include <windows.h>

#include "structs.h"

// Inicializa as estruturas de passageiros
BOOL initPassengersArray(pPassenger passengerArray, unsigned int maxPassengers);

// Remove um passageiro do array de passageiros
BOOL removePassengerFromArray(pPassenger passengerArray, unsigned int* nrPassengers, unsigned int id, HANDLE* hThreadPassengersArray);

// Copia o passageiro do src para o dest
void copyPassenger(pPassenger dest, passenger src);

// Devolve um ID para associar ao passageiro
DWORD getPassengerID();

// Mostra um passageiro
void printPassenger(passenger passenger);

#endif /* PASSENGER_H */
