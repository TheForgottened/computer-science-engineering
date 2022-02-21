#ifndef STRUCTS_H
#define STRUCTS_H

#include <windows.h>

#include "utils.h"

typedef struct AIRPORT_STRUCT airport, * pAirport;

typedef struct AIRPLANE_STRUCT airplane, * pAirplane;
typedef struct INTERFACE_STRUCT interfaceStruct, *pInterfaceStruct;

typedef struct COORDINATES_STRUCT coordinatesStruct, * pCoordinatesStruct;
typedef struct SHARED_COORDINATES_STRUCT sharedCoordinatesStruct, * pSharedCoordinatesStruct;

typedef struct DATATHREAD_STRUCT dataThreadStruct, *pDataThreadStruct;
typedef struct SHAREDMEM_STRUCT sharedMemoryStruct, *pSharedMemoryStruct;

typedef struct PINGTHREAD_STRUCT pingThreadStruct, *pPingThreadStruct;

typedef struct MOVE_AIRPLANE_STRUCT moveAirplaneStruct, *pMoveAirplaneStruct;

typedef struct STOPEVENTTHREAD_STRUCT stopEventThreadStruct, * pStopEventThreadStruct;

struct COORDINATES_STRUCT {
	int x, y;
};

struct SHARED_COORDINATES_STRUCT {
	int x, y;
	unsigned int maxAirplanes;
};

struct AIRPORT_STRUCT {
	TCHAR name[STR_SIZE];

	coordinatesStruct coordinates;

	unsigned int maxAirports;

	unsigned int currentPassengers;
};

struct AIRPLANE_STRUCT {
	DWORD id;
	unsigned int capacity, velocity;
	unsigned int currentPassengers;
	coordinatesStruct currentCoordinates;
	coordinatesStruct destinationCoordinates;

	TCHAR destAirport[STR_SIZE], srcAirport[STR_SIZE];

	BOOL stopped, getOnBoard;
};

struct SHAREDMEM_STRUCT {
	int nProducers;
	int writeIndex; // Próxima posição de escrita
	int readIndex; // Próxima posição de leitura

	airplane buffer[CIRCULAR_BUFFER_SIZE]; // Buffer circular em si (array de estruturas)
};

struct DATATHREAD_STRUCT {
	pSharedMemoryStruct sharedMemory; // Ponteiro para a memória partilhada

	pAirplane thisAirplane;

	HANDLE hWriteToCircularBufferEvent;

	HANDLE hSemaphoreWrite; // Semáforo de escrita
	HANDLE hSemaphoreRead; // Semáforo de leitura
	HANDLE hMutex;

	LPCRITICAL_SECTION criticalSectionAirplane, criticalSectionBool;

	BOOL* stop;
};

struct INTERFACE_STRUCT {
	pAirport airportsSharedArray;
	pAirplane thisAirplane;
	pMoveAirplaneStruct thisMove;

	HANDLE hWriteToCircularBufferEvent;

	HANDLE hSharedAirportsMutex;
	HANDLE hThreadMove;

	LPCRITICAL_SECTION criticalSectionAirplane, criticalSectionBool;

	BOOL *stop, *inFlight, *hasDestination, *hasArrivedAtDestination;
};

struct MOVE_AIRPLANE_STRUCT {
	pAirplane thisAirplane;

	HANDLE hWriteToCircularBufferEvent;

	LPCRITICAL_SECTION criticalSectionAirplane, criticalSectionBool;

	BOOL* hasArrivedAtDestination; // o aviao chegou ao destino
	BOOL* stop; // o utilizador terminou o programa repentinamente
};

struct PINGTHREAD_STRUCT {
	DWORD id;

	LPCRITICAL_SECTION criticalSectionBool;

	BOOL* stop;
};

struct STOPEVENTTHREAD_STRUCT {
	BOOL* stop;
	LPCRITICAL_SECTION criticalSectionBool;
};

#endif /* STRUCTS_H */