#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>
#include <io.h>

#include <stdio.h>
#include <ctype.h>

#include "utils.h"
#include "dllHandling.h"
#include "airplaneThreads.h"
#include "structs.h"

// pede os valores iniciais
BOOL askInitialValuesAndInitializeAirplane(pAirplane thisAirplane, pAirport airportsSharedArray, HANDLE hSharedAirportsMutex) {
	TCHAR command[STR_SIZE], ** commandArray = NULL, endOfLine;

	while (1) {
		_tprintf(TEXT("\nLotação: "));
		_tscanf_s(TEXT("%[^\n]%c"), command, STR_SIZE, &endOfLine, (unsigned)sizeof(TCHAR));

		if (endOfLine != '\n') {
			_ftprintf(stderr, TEXT("[ERRO] Comando demasiado grande!\n"));
			continue;
		}

		if (isStringANumber(command)) break;

		_tprintf(TEXT("[ERRO] Lotação inválida!\n"));
	}

	thisAirplane->capacity = _tstoi(command);

	while (1) {
		_tprintf(TEXT("\nVelocidade (em posições por segundo): "));
		_tscanf_s(TEXT("%[^\n]%c"), command, STR_SIZE, &endOfLine, (unsigned)sizeof(TCHAR));

		if (endOfLine != '\n') {
			_ftprintf(stderr, TEXT("[ERRO] Comando demasiado grande!\n"));
			continue;
		}

		if (isStringANumber(command)) {
			thisAirplane->velocity = _tstoi(command);

			if (thisAirplane->velocity > 0) break;
		}

		_tprintf(TEXT("[ERRO] Velocidade inválida!\n"));
	}

	thisAirplane->velocity = _tstoi(command);

	// Ver se o aeroporto existe, vai à memoria partilhada
	while (1) {
		unsigned int i;

		_tprintf(TEXT("\nAeroporto inicial: "));
		_tscanf_s(TEXT("%[^\n]%c"), command, STR_SIZE, &endOfLine, (unsigned)sizeof(TCHAR));

		if (endOfLine != '\n') {
			_ftprintf(stderr, TEXT("[ERRO] Comando demasiado grande!\n"));
			continue;
		}
		// percorre a memoria partilhada de aeroportos para ver se um certo aeroporto existe
		for (i = 0; i < airportsSharedArray[0].maxAirports; i++) {
			if (_tcsicmp(command, airportsSharedArray[i].name) == 0) {
				_tcscpy_s(thisAirplane->srcAirport, _countof(thisAirplane->srcAirport) - 1, airportsSharedArray[i].name);
				thisAirplane->currentCoordinates = airportsSharedArray[i].coordinates;
				break;
			}
		}

		if (i != airportsSharedArray[0].maxAirports) break;

		_tprintf(TEXT("[ERRO] O aeroporto não existe!\n"));
	}

	return TRUE;
}

int _tmain(int argc, TCHAR* argv[]) {
	DWORD id = GetCurrentProcessId();
	BOOL stop = FALSE, inFlight = FALSE, hasDestination = FALSE, hasArrivedAtDestination = FALSE;
	CRITICAL_SECTION criticalSectionAirplane, criticalSectionBool;
	HANDLE hThreadInterface, hThreadPing, hSemaphoreMaxAirplanes, hEventActivateSuspend;
	interfaceStruct mainInterface;
	moveAirplaneStruct mainMove;
	airplane thisAirplane;

	HANDLE hFileMapAirports, hSharedAirportsMutex;
	pAirport airportsSharedArray;

	HANDLE hWriteToCircularBufferEvent;
	HANDLE hFileMap;
	HANDLE hThreadProducer;
	dataThreadStruct mainData;

	pingThreadStruct mainPing;

	HANDLE hStopEventThread;
	stopEventThreadStruct mainStopEvent;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	initRandom();

	_tprintf(TEXT("%ld\n\n"), id);

	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);

	_tprintf(TEXT("A tentar entrar no sistema...\n"));
	hEventActivateSuspend = OpenEvent(
		SEMAPHORE_ALL_ACCESS,
		FALSE,
		EVENT_ACTIVATE_SUSPEND_NAME
	);

	if (hEventActivateSuspend == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Erro abrir o evento!\n"));
		return -1;
	}
	// Espera 2 segundos a ver se o evento está assinalado, caso passado 2 segundos nao esteja assinalado ele sai
	if (WaitForSingleObject(hEventActivateSuspend, 2000) == WAIT_TIMEOUT) {
		_ftprintf(stderr, TEXT("[ERRO] O controlador bloqueou as vagas de entrada!\n"));
		return -1;
	}

	hSemaphoreMaxAirplanes = OpenSemaphore(
		SEMAPHORE_ALL_ACCESS,
		TRUE,
		SEMAPHORE_MAX_AIRPLANES_NAME
	);

	if (hSemaphoreMaxAirplanes == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir o semáforo dos aviões!\n"));
		return -1;
	}

	if (WaitForSingleObject(hSemaphoreMaxAirplanes, 5000) == WAIT_TIMEOUT) {
		_tprintf(TEXT("\nO sistema está cheio e não pode aceitar o avião de momento, por favor tente mais tarde!"));
		return 0;
	}

	_tprintf(TEXT("Avião foi aceite!\n\n"));

	// Inclui a DLL
	if (!setupDll()) {
		_ftprintf(stderr, TEXT("[ERRO] Na inclusão da DLL!\n"));

		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionAirplane,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionBool,
		500
	);

	mainStopEvent.stop = &stop;
	mainStopEvent.criticalSectionBool = &criticalSectionBool;

	hStopEventThread = CreateThread(
		NULL,
		0,
		stopEventThread,
		&mainStopEvent,
		0,
		NULL
	);

	if (hStopEventThread == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread para o evento de stop do Control!\n"));

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);

		return -1;
	}

	hWriteToCircularBufferEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		NULL
	);

	if (hWriteToCircularBufferEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping dos aeroportos!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	hFileMapAirports = OpenFileMapping(
		FILE_MAP_ALL_ACCESS,
		FALSE,
		FILE_MAPPING_AIRPORTS_NAME
	);

	if (hFileMapAirports == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping dos aeroportos!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
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

		WaitForSingleObject(hStopEventThread, INFINITE);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	hSharedAirportsMutex = OpenMutex(
		MUTEX_ALL_ACCESS,
		FALSE,
		MUTEX_AIRPORTS_NAME
	);

	if (hSharedAirportsMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi criar o mutex dos aeroportos a partilhar!\n"), GetLastError());

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	// Inicialização das struct com valores default para evitar problemas

	stop = FALSE;
	inFlight = FALSE;
	hasDestination = FALSE;
	hasArrivedAtDestination = FALSE;

	thisAirplane.id = id;
	thisAirplane.capacity = 0;
	thisAirplane.velocity = 0;
	thisAirplane.currentPassengers = 0;
	thisAirplane.currentCoordinates.x = 0;
	thisAirplane.currentCoordinates.y = 0;
	thisAirplane.destinationCoordinates.x = 0;
	thisAirplane.destinationCoordinates.y = 0;
	_tcscpy_s(thisAirplane.srcAirport, _countof(thisAirplane.srcAirport) - 1, TEXT(""));
	_tcscpy_s(thisAirplane.destAirport, _countof(thisAirplane.destAirport) - 1, TEXT(""));
	thisAirplane.stopped = TRUE;
	thisAirplane.getOnBoard = FALSE;

	mainMove.thisAirplane = &thisAirplane;
	mainMove.hWriteToCircularBufferEvent = hWriteToCircularBufferEvent;
	mainMove.criticalSectionAirplane = &criticalSectionAirplane;
	mainMove.criticalSectionBool = &criticalSectionBool;
	mainMove.hasArrivedAtDestination = &hasArrivedAtDestination;
	mainMove.stop = &stop;

	mainInterface.airportsSharedArray = airportsSharedArray;
	mainInterface.thisAirplane = &thisAirplane;
	mainInterface.thisMove = &mainMove;
	mainInterface.hWriteToCircularBufferEvent = hWriteToCircularBufferEvent;
	mainInterface.hSharedAirportsMutex = hSharedAirportsMutex;
	mainInterface.hThreadMove = NULL;
	mainInterface.criticalSectionAirplane = &criticalSectionAirplane;
	mainInterface.criticalSectionBool = &criticalSectionBool;
	mainInterface.hasArrivedAtDestination = &hasArrivedAtDestination;
	mainInterface.stop = &stop;
	mainInterface.inFlight = &inFlight;
	mainInterface.hasDestination = &hasDestination;
	mainInterface.hasArrivedAtDestination = &hasArrivedAtDestination;

	if (!askInitialValuesAndInitializeAirplane(&thisAirplane, airportsSharedArray, hSharedAirportsMutex)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível inicializar o avião!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	// Cria semáforo de escrita
	mainData.hSemaphoreWrite = OpenSemaphore (
		SEMAPHORE_ALL_ACCESS,
		FALSE,
		SEMAPHORE_CIRCULAR_WRITE_NAME
	);

	// Cria semáforo de leitura
	mainData.hSemaphoreRead = OpenSemaphore (
		SEMAPHORE_ALL_ACCESS,
		FALSE,
		SEMAPHORE_CIRCULAR_READ_NAME
	);

	if (mainData.hSemaphoreWrite == NULL || mainData.hSemaphoreRead == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir semáforos para o buffer circular!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	mainData.hMutex = CreateMutex (
		NULL,
		FALSE,
		MUTEX_CIRCULAR_NAME
	);

	if (mainData.hMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar mutex!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	// Cria o FileMapping
	hFileMap = OpenFileMapping (
		FILE_MAP_ALL_ACCESS,
		FALSE,
		FILE_MAPPING_CIRCULAR_NAME
	);

	if (hFileMap == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir o FileMapping!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	mainData.sharedMemory = (sharedMemoryStruct*)MapViewOfFile (
		hFileMap,
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

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	mainData.thisAirplane = &thisAirplane;
	mainData.hWriteToCircularBufferEvent = hWriteToCircularBufferEvent;
	mainData.criticalSectionAirplane = &criticalSectionAirplane;
	mainData.criticalSectionBool = &criticalSectionBool;
	mainData.stop = &stop;

	hThreadProducer = CreateThread (
		NULL,
		0,
		threadProducer,
		&mainData,
		0,
		NULL
	);

	if (hThreadProducer == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossivel criar thread do produtor\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	// Escreve a primeira vez no buffer circular;
	SetEvent(hWriteToCircularBufferEvent);
	
	hThreadInterface = CreateThread (
		NULL,
		0,
		threadInterface,
		&mainInterface,
		0,
		NULL
	);

	if (hThreadInterface == NULL){
		_ftprintf(stderr, TEXT("[ERRO] Impossivel criar thread interface!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);
		WaitForSingleObject(hThreadProducer, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	mainPing.id = thisAirplane.id;
	mainPing.criticalSectionBool = &criticalSectionBool;
	mainPing.stop = &stop;

	hThreadPing = CreateThread(
		NULL,
		0,
		threadPing,
		&mainPing,
		0,
		NULL
	);

	if (hThreadPing == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossivel criar thread ping!\n"));
		
		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);
		WaitForSingleObject(hThreadProducer, INFINITE);
		WaitForSingleObject(hThreadInterface, INFINITE);

		UnmapViewOfFile(airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		setupDll();
		ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
		return -1;
	}

	WaitForSingleObject(hThreadInterface, INFINITE);
	
	EnterCriticalSection(&criticalSectionBool);
	stop = TRUE;
	LeaveCriticalSection(&criticalSectionBool);

	WaitForSingleObject(hStopEventThread, INFINITE);
	WaitForSingleObject(hThreadProducer, INFINITE);
	WaitForSingleObject(hThreadPing, INFINITE);

	UnmapViewOfFile(mainData.sharedMemory);
	UnmapViewOfFile(airportsSharedArray);

	_tprintf(TEXT("Avião terminado com sucesso!\n"));

	ReleaseSemaphore(hSemaphoreMaxAirplanes, 1, NULL);
	setupDll();
	return 0;
}