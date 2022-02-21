#ifndef STRUCTS_H
#define STRUCTS_H

#include <windows.h>
#include <tchar.h>

#include "utils.h"

typedef struct PASSENGER_STRUCT passenger, * pPassenger; // struct referente ao passageiro
typedef struct STOPEVENTTHREAD_STRUCT stopEventThreadStruct, * pStopEventThreadStruct; // struct para a thread de espera de saida
typedef struct NAMEDPIPE_STRUCT namedPipeStruct, * pNamedPipeStruct; // struct do named pipe

struct PASSENGER_STRUCT {
	TCHAR name[STR_SIZE];
	DWORD id, airplaneId;

	TCHAR destAirport[STR_SIZE], srcAirport[STR_SIZE];

	unsigned int waitToBoard;

	BOOL stopped;
};

struct STOPEVENTTHREAD_STRUCT {
	BOOL* stop;
	LPCRITICAL_SECTION criticalSectionBool;
};

struct NAMEDPIPE_STRUCT {
	HANDLE hPipe; // handle do pipe

	OVERLAPPED overlap;
};

#endif