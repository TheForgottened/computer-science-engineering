#include "passenger.h"

BOOL initPassengersArray(pPassenger passengerArray, unsigned int maxPassengers) {
	for (unsigned int i = 0; i < maxPassengers; i++) {
		passengerArray[i].id = 0;
		passengerArray[i].waitToBoard = 0;

		_tcscpy_s(passengerArray[i].name, _countof(passengerArray[i].name) - 1, TEXT(" "));
		_tcscpy_s(passengerArray[i].destAirport, _countof(passengerArray[i].destAirport) - 1, TEXT(" "));
		_tcscpy_s(passengerArray[i].srcAirport, _countof(passengerArray[i].srcAirport) - 1, TEXT(" "));
	}

	return TRUE;
}

BOOL removePassengerFromArray(pPassenger passengerArray, unsigned int* nrPassengers, unsigned int id, HANDLE* hThreadPassengersArray) {
	unsigned int i;

	for (i = 0; i < *nrPassengers; i++) {
		if (passengerArray[i].id == id) {
			copyPassenger(&passengerArray[i], passengerArray[(*nrPassengers) - 1]);
			hThreadPassengersArray[i] = hThreadPassengersArray[(*nrPassengers) - 1];

			(*nrPassengers)--;

			return TRUE;
		}
	}

	return FALSE;
}


void copyPassenger(pPassenger dest, passenger src) {
	dest->id = src.id;
	dest->waitToBoard = src.waitToBoard;

	_tcscpy_s(dest->name, _countof(dest->name) - 1, src.name);
	_tcscpy_s(dest->destAirport, _countof(dest->destAirport) - 1, src.destAirport);
	_tcscpy_s(dest->srcAirport, _countof(dest->srcAirport) - 1, src.srcAirport);
}

DWORD getPassengerID() {
	static DWORD id = 1;

	return id++;
}


void printPassenger(passenger passenger){
	_tprintf(TEXT("nome : %s\n"), passenger.name);
	_tprintf(TEXT("%u\n"), passenger.id);
	_tprintf(TEXT("destAirport : %s\n"), passenger.destAirport);
	_tprintf(TEXT("srcAirport : %s\n"), passenger.srcAirport);
	_tprintf(TEXT("waitToBoard : %u\n"), passenger.waitToBoard);
}