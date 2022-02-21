#include "logic.h"

#include "airport.h"

BOOL caeroporto(TCHAR* name, int coordX, int coordY, pAirport airportsArray, pAirport airportsSharedArray, unsigned int *maxAirports, unsigned int *nrAirports) {
	HANDLE hSharedAirportsMutex;
	
	// Verifica se chegamos ao limite máximo de aeroportos
	if (*maxAirports == *nrAirports) return FALSE;

	// Verifica se é uma string vazia
	if (_tcsclen(name) == 0) return FALSE;

	// Verifica se tem espaços no nome
	if (stringHasSpaces(name)) return FALSE;

	// Verifica se as coordenadas são validas
	if (coordX > CORD_MAX_X || coordY > CORD_MAX_Y || coordX < 0 || coordY < 0) return FALSE;

	// Verifica nome e coordenadas do aeroporto (se o aeroporto ja existe e se nao está perto de outro aeroporto)
	if (!isAirportValid(airportsArray, *nrAirports, name, coordX, coordY)) return FALSE;

	// Inicia um aeroporto no array de aeroportos
	initAirport(
		&airportsArray[*nrAirports],
		nrAirports,
		name,
		coordX,
		coordY
	);

	hSharedAirportsMutex = OpenMutex(
		MUTEX_ALL_ACCESS,
		FALSE,
		MUTEX_AIRPORTS_NAME
	);

	if (hSharedAirportsMutex == NULL) return FALSE;

	WaitForSingleObject(hSharedAirportsMutex, INFINITE);
	airportsSharedArray[*nrAirports - 1] = airportsArray[*nrAirports - 1];
	ReleaseMutex(hSharedAirportsMutex);

	return TRUE;
}