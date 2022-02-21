#ifndef STRUCTS_H
#define STRUCTS_H

#include <windows.h>
#include <tchar.h>

#include "utils.h"

typedef struct OLDMAINTHREAD_STRUCT oldMainThreadStruct, * pOldMainThreadStruct; // Struct para representar a antiga interface
typedef struct SHAREDDATA_STRUCT sharedDataStruct, * pSharedDataStruct; // Struct usada na GUI


typedef struct COORDINATES_STRUCT coordinatesStruct, * pCoordinatesStruct;

typedef struct PASSENGER_STRUCT passenger, * pPassenger;
typedef struct AIRPORT_STRUCT airport, * pAirport;
typedef struct AIRPLANE_STRUCT airplane, * pAirplane;

typedef struct COORDINATES_STRUCT coordinatesStruct, * pCoordinatesStruct;
typedef struct SHARED_COORDINATES_STRUCT sharedCoordinatesStruct, * pSharedCoordinatesStruct;

typedef struct PASSENGERTHREAD_STRUCT passengerThreadStruct, * pPassengerThreadStruct;

typedef struct NAMEDPIPE_STRUCT namedPipeStruct, * pNamedPipeStruct;
typedef struct PIPETHREAD_STRUCT pipeThreadStruct, * pPipeThreadStruct;

typedef struct DATATHREAD_STRUCT dataThreadStruct, * pDataThreadStruct; // Struct para a Thread do Consumidor
typedef struct SHAREDMEM_STRUCT sharedMemoryStruct, * pSharedMemoryStruct; // Struct para a Memória Partilhada
typedef struct COORDTHREAD_STRUCT coordThreadStruct, * pCoordThreadStruct; // Struct para a Thread de Coordenadas
typedef struct PINGTHREAD_STRUCT pingThreadStruct, * pPingThreadStruct; // Struct para a Thread dos Pings

struct OLDMAINTHREAD_STRUCT {
	unsigned int* maxAirplanes, * maxAirports;
	unsigned int* nrAirports, * nrAirplanes, * nrPassengers;

	BOOL* stop;

	pAirport airportsArray;
	pAirplane airplanesArray;
	pPassenger passengersArray;

	pAirport airportsSharedArray;

	HANDLE hEventActivateSuspend;
	HANDLE hSetupFinishedEvent;

	HWND hMainWnd;

	LPCRITICAL_SECTION criticalSectionAirplanes;
	LPCRITICAL_SECTION criticalSectionAirports;
	LPCRITICAL_SECTION criticalSectionPassengers;
	LPCRITICAL_SECTION criticalSectionBool;
};

struct SHAREDDATA_STRUCT {
	HWND hMainWnd;

	HDC memDC;
	HDC bitAirportDC, bitAirplaneDC;
	BITMAP bitAirport, bitAirplane;
	HBITMAP hBitmapDB;

	LPCRITICAL_SECTION criticalSectionPaint;

	HANDLE hOldMainThread;

	unsigned int* maxAirplanes, * maxAirports;
	unsigned int* nrAirports, * nrAirplanes, * nrPassengers;

	BOOL* stop, *fMouseTracking;

	pAirport airportsArray;
	pAirplane airplanesArray;
	pPassenger passengersArray;

	pAirport airportsSharedArray;

	HANDLE hEventActivateSuspend;

	LPCRITICAL_SECTION criticalSectionAirplanes;
	LPCRITICAL_SECTION criticalSectionAirports;
	LPCRITICAL_SECTION criticalSectionPassengers;
	LPCRITICAL_SECTION criticalSectionBool;
};

struct COORDINATES_STRUCT {
	int x, y;
};

struct SHARED_COORDINATES_STRUCT {
	int x, y;
	unsigned int maxAirplanes;
};

struct PASSENGERTHREAD_STRUCT {
	DWORD thisPassengerId;
	pNamedPipeStruct namedPipe;

	pAirport airportsArray;
	unsigned int* nrAirports;

	pAirplane airplanesArray;
	unsigned int* nrAirplanes;

	pPassenger passengersArray;
	unsigned int* nrPassengers;

	HANDLE* hThreadPassengersArray;

	LPCRITICAL_SECTION criticalSectionAirports, criticalSectionPassengers, criticalSectionBool, criticalSectionAirplanes;
	BOOL* stop;
};

struct PASSENGER_STRUCT {
	TCHAR name[STR_SIZE];
	DWORD id, airplaneId;

	TCHAR destAirport[STR_SIZE], srcAirport[STR_SIZE];

	unsigned int waitToBoard;

	BOOL stopped;
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

// Struct para a Memória Partilhada
struct SHAREDMEM_STRUCT {
	int nProducers;
	int writeIndex; // Próxima posição de escrita
	int readIndex; // Próxima posição de leitura

	airplane buffer[CIRCULAR_BUFFER_SIZE]; // Buffer circular em si (array de estruturas)
};

// Struct para a Thread do Consumidor
struct DATATHREAD_STRUCT {
	pSharedMemoryStruct sharedMemory; // Ponteiro para a memória patilhada

	pAirplane airplanesArray; // Array de aviões
	HANDLE* hThreadPingArray; // Array de handles para as threads de ping
	unsigned int* nrAirplanes, * maxAirplanes;

	unsigned int* nrPassengers;

	HWND hMainWnd;

	HANDLE hSemaphoreWrite; // Semáforo de escrita
	HANDLE hSemaphoreRead; // Semáforo de leitura
	LPCRITICAL_SECTION criticalSectionAirplanes, criticalSectionBool, criticalSectionPings, criticalSectionPassengers;

	BOOL* stop;
};

// Struct para a thread das coordenadas
struct COORDTHREAD_STRUCT {
	pSharedCoordinatesStruct sharedMemory;
	pAirplane airplanesArray;
	unsigned int size;

	HANDLE hMutex;
	LPCRITICAL_SECTION criticalSectionAirplanes, criticalSectionBool;

	BOOL* stop;
};

// Struct para a thread dos pings
struct PINGTHREAD_STRUCT {
	DWORD thisAirplaneId;
	pAirplane airplanesArray;

	HWND hMainWnd;

	HANDLE* hThreadPingArray;

	unsigned int* nrAirplanes;
	LPCRITICAL_SECTION criticalSectionBool, criticalSectionAirplanes, criticalSectionPings;
	BOOL* stop;
};

struct NAMEDPIPE_STRUCT {
	HANDLE hPipe; // handle do pipe

	OVERLAPPED overlap;

	BOOL active; //representa se a instancia do named pipe esta ou nao ativa, se ja tem um cliente ou nao
};

struct PIPETHREAD_STRUCT {
	pAirport airportsArray;
	unsigned int* nrAirports;

	pAirplane airplanesArray;
	unsigned int* nrAirplanes;

	pPassenger passengersArray;
	unsigned int* nrPassengers;

	LPCRITICAL_SECTION criticalSectionBool, criticalSectionPipe, criticalSectionAirports, criticalSectionPassengers, criticalSectionAirplanes;
	BOOL* stop;
};

#endif /* STRUCTS_H */