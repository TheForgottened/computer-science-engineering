#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#include <ctype.h>

#include "utils.h"
#include "keyHandling.h"
#include "structs.h"
#include "controlThreads.h"
#include "airplane.h"
#include "airport.h"
#include "sharedCoordinates.h"

int _tmain(int argc, TCHAR* argv[]) {
	HANDLE hThreadInterface, hSemaphoreUnique, hSemaphoreMaxAirplanes, hEventActivateSuspend;
	unsigned int maxAirplanes = 0, maxAirports = 0;
	unsigned int nrAirports = 0, nrAirplanes = 0, nrPassengers = 0;
	BOOL stop = FALSE;

	// Critical sections para controlar os avioes, aeroportos, bools e pipe struct da interface

	CRITICAL_SECTION criticalSectionAirplanes;
	CRITICAL_SECTION criticalSectionAirports;
	CRITICAL_SECTION criticalSectionPassengers;
	CRITICAL_SECTION criticalSectionBool; 
	CRITICAL_SECTION criticalSectionPings;
	CRITICAL_SECTION criticalSectionPipe; 

	interfaceStruct mainInterface;

	HANDLE hFileMapAirports, hSharedAirportsMutex;
	pAirport airportsSharedArray;

	HANDLE hFileMapCircular; // handle para o filemap
	HANDLE hThreadConsumer; // handle para a thread do consumidor
	dataThreadStruct mainData; // struct da thread do consumidor

	HANDLE hFileMapCoordinates;
	HANDLE hThreadCoordinates;
	pSharedCoordinatesStruct sharedCoordinatesArray;
	coordThreadStruct mainCoordinates;

	HANDLE hThreadPipe;
	pipeThreadStruct mainPipe;

	HANDLE hStopEvent;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);

	// Semaforo que controla o nr de instancias de controladores
	hSemaphoreUnique = CreateSemaphore (
		NULL,
		1,
		2,
		SEMAPHORE_UNIQUE_CONTROL_NAME
	);
	// Se ERROR_ALREADY_EXISTS , entao nao deixa iniciar outro controlador
	if (GetLastError() == ERROR_ALREADY_EXISTS) {
		_ftprintf(stderr, TEXT("[ERRO] Já existe um control aberto!\n"));
		return -1;
	}

	hStopEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		EVENT_CONTROL_CLOSED_NAME
	);

	if (hStopEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o evento de notificação de saída!\n"));
		return -1;
	}

	InitializeCriticalSectionAndSpinCount (
		&criticalSectionAirplanes,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionAirports,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPassengers,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionBool,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPings,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPipe,
		500
	);

	// Recebe os valores maximos escritos no Registry, caso nao consiga fica com uns valores default
	if (!getMaximumValues(&maxAirplanes, &maxAirports)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível obter os valores máximos - a usar valores por default!\n"));
		maxAirplanes = DEFAULT_MAX_AIRPLANES;
		maxAirports = DEFAULT_MAX_AIRPORTS;
	}

	// Semaforo que controla o nr de instancias de avioes possiveis
	hSemaphoreMaxAirplanes = CreateSemaphore(
		NULL,
		maxAirplanes,
		maxAirplanes,
		SEMAPHORE_MAX_AIRPLANES_NAME
	);

	if (hSemaphoreMaxAirplanes == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o semáforo dos aviões!\n"));
		return -1;
	}

	// Semaforo que ativa e suspende aceitação de aviões novos (reset manual e começa com o evento assinalado)
	hEventActivateSuspend = CreateEvent(
		NULL,
		TRUE,
		TRUE, 
		EVENT_ACTIVATE_SUSPEND_NAME
	);

	if (hEventActivateSuspend == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o evento de aceitar/suspender!\n"));
		return -1;
	}


	// Cria o array de aeroportos
	pAirport airportsArray = malloc(maxAirports * sizeof(airport));

	// Cria o array de aviões
	pAirplane airplanesArray = malloc(maxAirplanes * sizeof(airplane));

	// Cria o array de handles para threads de ping dos aviões
	HANDLE* hThreadPingArray = malloc(maxAirplanes * sizeof(HANDLE));

	// Cria o array de passageiros
	pPassenger passengersArray = malloc(MAX_PASSENGERS * sizeof(passenger));

	if (airportsArray == NULL || airplanesArray == NULL || hThreadPingArray == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível alocar memória para as estruturas chave!\n"));

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Inicializa o array de aeroportos e de avioes
	initAirportsArray(airportsArray, maxAirports);
	initAirplanesArray(airplanesArray, maxAirplanes);

	mainPipe.airportsArray = airportsArray;
	mainPipe.nrAirports = &nrAirports;
	mainPipe.airplanesArray = airplanesArray;
	mainPipe.nrAirplanes = &nrAirplanes;
	mainPipe.passengersArray = passengersArray;
	mainPipe.nrPassengers = &nrPassengers;
	mainPipe.criticalSectionBool = &criticalSectionBool;
	mainPipe.criticalSectionPipe = &criticalSectionPipe;
	mainPipe.criticalSectionAirports = &criticalSectionAirports;
	mainPipe.criticalSectionPassengers = &criticalSectionPassengers;
	mainPipe.criticalSectionAirplanes = &criticalSectionAirplanes;
	mainPipe.stop = &stop;

	hThreadPipe = CreateThread(
		NULL,
		0,
		pipeThread,
		&mainPipe,
		0,
		NULL
	);

	if (hThreadPipe == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread dos pipes!\n"));

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	hFileMapAirports = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(airport) * maxAirports,
		FILE_MAPPING_AIRPORTS_NAME
	);

	if (hFileMapAirports == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping dos aeroportos!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	airportsSharedArray = (pAirport)MapViewOfFile(
		hFileMapAirports,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		0
	);

	if (airportsSharedArray == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória dos aeroportos a partilhar!\n"), GetLastError());
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	hSharedAirportsMutex = CreateMutex(
		NULL,
		FALSE,
		MUTEX_AIRPORTS_NAME
	);

	if (hSharedAirportsMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar o mutex dos aeroportos a partilhar!\n"), GetLastError());

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		UnmapViewOfFile(airportsSharedArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	for (unsigned int i = 0; i < maxAirports; i++) {
		airportsSharedArray[i] = airportsArray[i];
	}

	// Inicializa a struct para a thread interface
	mainInterface.airportsArray = airportsArray;
	mainInterface.airportsSharedArray = airportsSharedArray;
	mainInterface.airplanesArray = airplanesArray;
	mainInterface.hSharedAirportsMutex = hSharedAirportsMutex;
	mainInterface.hEventActivateSuspend = hEventActivateSuspend;
	mainInterface.nrAirports = &nrAirports;
	mainInterface.nrAirplanes = &nrAirplanes;
	mainInterface.maxAirports = &maxAirports;
	mainInterface.maxAirplanes = &maxAirplanes;
	mainInterface.criticalSectionAirplanes = &criticalSectionAirplanes;
	mainInterface.criticalSectionAirports = &criticalSectionAirports;
	mainInterface.criticalSectionBool = &criticalSectionBool;
	mainInterface.stop = &stop;

	// Cria a thread da interface
	hThreadInterface = CreateThread (
		NULL,
		0,
		threadInterface,
		&mainInterface,
		0,
		NULL
	);

	if (hThreadInterface == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread interface!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);
		
		return -1;
	}

	// Cria semáforo de escrita para o buffer circular
	mainData.hSemaphoreWrite = CreateSemaphore (
		NULL, 
		CIRCULAR_BUFFER_SIZE,
		CIRCULAR_BUFFER_SIZE, 
		SEMAPHORE_CIRCULAR_WRITE_NAME
	);

	// Cria semáforo de leitura para o buffer circular
	mainData.hSemaphoreRead = CreateSemaphore (
		NULL, 
		0, 
		CIRCULAR_BUFFER_SIZE, 
		SEMAPHORE_CIRCULAR_READ_NAME
	);
	
	// Caso dê erro na criação dos semaforos, alteramos o stop para TRUE e aguardamos que a ThreadInterface termine
	if (mainData.hSemaphoreWrite == NULL || mainData.hSemaphoreRead == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar semáforos para o buffer circular!\n"), GetLastError());
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}
	
	// Cria o FileMapping do buffer circular
	hFileMapCircular = CreateFileMapping (
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(sharedMemoryStruct),
		FILE_MAPPING_CIRCULAR_NAME
	);

	if (hFileMapCircular == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	mainData.sharedMemory = (sharedMemoryStruct*)MapViewOfFile (
		hFileMapCircular, 
		FILE_MAP_ALL_ACCESS, 
		0, 
		0, 
		0
	);

	if (mainData.sharedMemory == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Preenchemos a estrutura de apoio ao buffer circular
	mainData.airplanesArray = airplanesArray;
	mainData.hThreadPingArray = hThreadPingArray;
	mainData.nrAirplanes = &nrAirplanes;
	mainData.maxAirplanes = &maxAirplanes;
	mainData.nrPassengers = &nrPassengers;
	mainData.criticalSectionAirplanes = &criticalSectionAirplanes;
	mainData.criticalSectionBool = &criticalSectionBool;
	mainData.criticalSectionPings = &criticalSectionPings;
	mainData.criticalSectionPassengers = &criticalSectionPassengers;
	mainData.stop = &stop;

	// Preenchemos a estrutura da memoria partilhada
	mainData.sharedMemory->nProducers = 0;
	mainData.sharedMemory->writeIndex = 0;
	mainData.sharedMemory->readIndex = 0;
	
	// Lança a Thread do consumidor
	hThreadConsumer = CreateThread (
		NULL, 
		0, 
		consumerThread, 
		&mainData, 
		0, 
		NULL
	);

	if (hThreadConsumer == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir a thread consumidor!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Cria o FileMapping das coordenadas
	hFileMapCoordinates = CreateFileMapping (
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(sharedCoordinatesStruct) * maxAirplanes,
		FILE_MAPPING_COORDINATES_NAME
	);

	if (hFileMapCoordinates == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping das Coordenadas!\n"))
;		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	sharedCoordinatesArray = (pSharedCoordinatesStruct)MapViewOfFile (
		hFileMapCoordinates, 
		FILE_MAP_ALL_ACCESS, 
		0, 
		0, 
		0
	);

	if (sharedCoordinatesArray == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);
		
		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Preenchemos a estrutura da thread de coordenadas
	mainCoordinates.sharedMemory = sharedCoordinatesArray;
	mainCoordinates.airplanesArray = airplanesArray;
	mainCoordinates.size = maxAirplanes;
	mainCoordinates.criticalSectionAirplanes = &criticalSectionAirplanes;
	mainCoordinates.criticalSectionBool = &criticalSectionBool;
	mainCoordinates.stop = &stop;

	// Criamos o mutex
	mainCoordinates.hMutex = CreateMutex(
		NULL,
		FALSE,
		MUTEX_COORDINATES_NAME
	);

	if (mainCoordinates.hMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);
		
		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);
		UnmapViewOfFile(sharedCoordinatesArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// Lançamos a thread das coordenadas
	hThreadCoordinates = CreateThread (
		NULL,
		0,
		coordinatesThread,
		&mainCoordinates,
		0,
		NULL
	);

	if (hThreadCoordinates == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread das coordenadas!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);
		WaitForMultipleObjects(nrAirplanes, hThreadPingArray, TRUE, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);
		UnmapViewOfFile(sharedCoordinatesArray);

		free(airportsArray);
		free(airplanesArray);
		free(hThreadPingArray);
		free(passengersArray);

		return -1;
	}

	// No fim, esperamos por todas as threads
	// E fazemos unmap dos file maps criados anteriormente
	WaitForSingleObject(hThreadInterface, INFINITE);
	
	EnterCriticalSection(&criticalSectionBool);
	stop = TRUE;
	LeaveCriticalSection(&criticalSectionBool);

	WaitForSingleObject(hThreadPipe, INFINITE);
	WaitForSingleObject(hThreadConsumer, INFINITE);
	WaitForSingleObject(hThreadCoordinates, INFINITE);
	WaitForMultipleObjects(nrAirplanes, hThreadPingArray, TRUE, INFINITE);

	UnmapViewOfFile(mainData.sharedMemory);
	UnmapViewOfFile(sharedCoordinatesArray);
	UnmapViewOfFile(airportsSharedArray);

	_tprintf(TEXT("Programa finalizado com sucesso!\n"));

	free(airportsArray);
	free(airplanesArray);
	free(hThreadPingArray);
	free(passengersArray);

	SetEvent(hStopEvent);

	// Handles são fechados automaticamente no final do programa

	return 0;
};