#include "airplane.h"

// Função que cria uma estrutura avião
BOOL initAirplane(pAirplane this, pAirport airportsArray, unsigned int airportsSize, unsigned int* airplanesSize, unsigned int capacity, unsigned int velocity, TCHAR* srcAirport) {
	unsigned int i;
	static unsigned int id = 1;

	for (i = 0; i < airportsSize; i++) {
		if (_tcscmp(airportsArray[i].name, srcAirport) == 0) {
			this->currentCoordinates.x = airportsArray[i].coordinates.x;
			this->currentCoordinates.y = airportsArray[i].coordinates.y;
			break;
		}
	}

	if (i == airportsSize) {
		_ftprintf(stderr, TEXT("[ERRO] Aeroporto específicado não existe!\n"));
		return FALSE;
	}


	this->id = id++;
	this->capacity = capacity;
	this->velocity = velocity;

	_tcscpy_s(this->srcAirport, _countof(this->srcAirport), srcAirport);
	resetString(this->destAirport, STR_SIZE);

	(*airplanesSize)++;

	return TRUE;
}

// Função que inicializa o array de aviões com valores default
void initAirplanesArray(pAirplane airplanesArray, unsigned int maxAirplanes) {
	unsigned int i;

	for (i = 0; i < maxAirplanes; i++) {
		airplanesArray[i].id = 0;
		airplanesArray[i].capacity = 0;
		airplanesArray[i].velocity = 0;
		airplanesArray[i].currentPassengers = 0;

		airplanesArray[i].currentCoordinates.x = -1;
		airplanesArray[i].currentCoordinates.y = -1;
		airplanesArray[i].destinationCoordinates.x = -1;
		airplanesArray[i].destinationCoordinates.y = -1;

		_tcscpy_s(airplanesArray[i].destAirport, _countof(airplanesArray[i].destAirport) - 1, TEXT(" "));
		_tcscpy_s(airplanesArray[i].srcAirport, _countof(airplanesArray[i].srcAirport) - 1, TEXT(" "));

		airplanesArray[i].stopped = TRUE;
		airplanesArray[i].getOnBoard = FALSE;
	}
}

// Copia avião src para avião dest
void copyAirplane(pAirplane dest, airplane src) {
	dest->id = src.id;
	dest->capacity = src.capacity;
	dest->velocity = src.velocity;

	dest->currentCoordinates.x = src.currentCoordinates.x;
	dest->currentCoordinates.y = src.currentCoordinates.y;

	_tcscpy_s(dest->destAirport, _countof(dest->destAirport) - 1, src.destAirport);
	_tcscpy_s(dest->srcAirport, _countof(dest->srcAirport) - 1, src.srcAirport);

	dest->stopped = src.stopped;
}

void resetAirplane(pAirplane thisAirplane) {
	thisAirplane->id = 0;
	thisAirplane->capacity = 0;
	thisAirplane->velocity = 0;
	thisAirplane->currentPassengers = 0;

	thisAirplane->currentCoordinates.x = -1;
	thisAirplane->currentCoordinates.y = -1;
	thisAirplane->destinationCoordinates.x = -1;
	thisAirplane->destinationCoordinates.y = -1;

	_tcscpy_s(thisAirplane->destAirport, _countof(thisAirplane->destAirport) - 1, TEXT(" "));
	_tcscpy_s(thisAirplane->srcAirport, _countof(thisAirplane->srcAirport) - 1, TEXT(" "));

	thisAirplane->stopped = TRUE;
	thisAirplane->getOnBoard = FALSE;
}

// Remove avião do array de avioes
BOOL removeAirplaneFromArray(pAirplane airplaneArray, unsigned int* airplanesArraySize, unsigned int id, HANDLE* hThreadPingArray) {
	unsigned int i;

	for (i = 0; i < *airplanesArraySize; i++) {
		if (airplaneArray[i].id == id) {
			copyAirplane(&airplaneArray[i], airplaneArray[(*airplanesArraySize) - 1]);
			hThreadPingArray[i] = hThreadPingArray[(*airplanesArraySize) - 1];
			resetAirplane(&airplaneArray[(*airplanesArraySize) - 1]);

			(*airplanesArraySize)--;

			return TRUE;
		}
	}

	return FALSE;
}

// Mostra info do array de aviões
void printAirplanesArray(pAirplane airplanesArray, unsigned int airplanesArraySize) {
	unsigned int i;

	for (i = 0; i < airplanesArraySize; i++) {
		_tprintf(TEXT("\nid: %u\tcapacity: %u\tvelocity: %u\n"), airplanesArray[i].id, airplanesArray[i].capacity, airplanesArray[i].velocity);
		_tprintf(TEXT("currentPassengers: %u\n"), airplanesArray[i].currentPassengers);
		_tprintf(TEXT("(%i, %i)\n"), airplanesArray[i].currentCoordinates.x, airplanesArray[i].currentCoordinates.y);
		_tprintf(TEXT("dest: %s\tsrc: %s\n"), airplanesArray[i].destAirport, airplanesArray[i].srcAirport);
		_tprintf(TEXT("stopped: %i\n===\n"), (int)airplanesArray[i].stopped);
	}
}

void printAirplane(airplane thisAirplane) {
	_tprintf(TEXT("\nid: %u\tcapacity: %u\tvelocity: %u\n"), thisAirplane.id, thisAirplane.capacity, thisAirplane.velocity);
	_tprintf(TEXT("currentPassengers: %u\n"), thisAirplane.currentPassengers);
	_tprintf(TEXT("(%i, %i)\n"), thisAirplane.currentCoordinates.x, thisAirplane.currentCoordinates.y);
	_tprintf(TEXT("dest: %s\tsrc: %s\n"), thisAirplane.destAirport, thisAirplane.srcAirport);
	_tprintf(TEXT("stopped: %i\n===\n"), (int)thisAirplane.stopped);
}