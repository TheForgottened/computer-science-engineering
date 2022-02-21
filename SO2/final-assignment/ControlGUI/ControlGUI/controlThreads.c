#include "controlThreads.h"

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
#include "passenger.h"

DWORD WINAPI oldMainThread(LPVOID param) {
	pOldMainThreadStruct thisMain = (pOldMainThreadStruct)param;

	CRITICAL_SECTION criticalSectionPings;
	CRITICAL_SECTION criticalSectionPipe;

	HANDLE hSemaphoreUnique, hSemaphoreMaxAirplanes;

	HANDLE hSharedAirportsMutex;

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
	hSemaphoreUnique = CreateSemaphore(
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

	// No caso de o evento já existir, garante que ele não está setado
	ResetEvent(hStopEvent);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPings,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPipe,
		500
	);

	// Recebe os valores maximos escritos no Registry, caso nao consiga fica com uns valores default
	if (!getMaximumValues(thisMain->maxAirplanes, thisMain->maxAirports)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível obter os valores máximos - a usar valores por default!\n"));
		*thisMain->maxAirplanes = DEFAULT_MAX_AIRPLANES;
		*thisMain->maxAirports = DEFAULT_MAX_AIRPORTS;
	}

	// Semaforo que controla o nr de instancias de avioes possiveis
	hSemaphoreMaxAirplanes = CreateSemaphore(
		NULL,
		*thisMain->maxAirplanes,
		*thisMain->maxAirplanes,
		SEMAPHORE_MAX_AIRPLANES_NAME
	);

	if (hSemaphoreMaxAirplanes == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o semáforo dos aviões!\n"));
		return -1;
	}

	// Cria o array de handles para threads de ping dos aviões
	HANDLE* hThreadPingArray = malloc(*thisMain->maxAirplanes * sizeof(HANDLE));


	if (hThreadPingArray == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível alocar memória para as estruturas chave!\n"));

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Inicializa o array de aeroportos e de avioes
	initAirportsArray(thisMain->airportsArray, *thisMain->maxAirports);
	initAirplanesArray(thisMain->airplanesArray, *thisMain->maxAirplanes);

	mainPipe.airportsArray = thisMain->airportsArray;
	mainPipe.nrAirports = thisMain->nrAirports;
	mainPipe.airplanesArray = thisMain->airplanesArray;
	mainPipe.nrAirplanes = thisMain->nrAirplanes;
	mainPipe.passengersArray = thisMain->passengersArray;
	mainPipe.nrPassengers = thisMain->nrPassengers;
	mainPipe.criticalSectionBool = thisMain->criticalSectionBool;
	mainPipe.criticalSectionPipe = &criticalSectionPipe;
	mainPipe.criticalSectionAirports = thisMain->criticalSectionAirports;
	mainPipe.criticalSectionPassengers = thisMain->criticalSectionPassengers;
	mainPipe.criticalSectionAirplanes = thisMain->criticalSectionAirplanes;
	mainPipe.stop = thisMain->stop;

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

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	hSharedAirportsMutex = CreateMutex(
		NULL,
		FALSE,
		MUTEX_AIRPORTS_NAME
	);

	if (hSharedAirportsMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar o mutex dos aeroportos a partilhar!\n"), GetLastError());

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		UnmapViewOfFile(thisMain->airportsSharedArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	for (unsigned int i = 0; i < *thisMain->maxAirports; i++) {
		thisMain->airportsSharedArray[i] = thisMain->airportsArray[i];
	}

	// Cria semáforo de escrita para o buffer circular
	mainData.hSemaphoreWrite = CreateSemaphore(
		NULL,
		CIRCULAR_BUFFER_SIZE,
		CIRCULAR_BUFFER_SIZE,
		SEMAPHORE_CIRCULAR_WRITE_NAME
	);

	// Cria semáforo de leitura para o buffer circular
	mainData.hSemaphoreRead = CreateSemaphore(
		NULL,
		0,
		CIRCULAR_BUFFER_SIZE,
		SEMAPHORE_CIRCULAR_READ_NAME
	);

	// Caso dê erro na criação dos semaforos, alteramos o stop para TRUE e aguardamos que a ThreadInterface termine
	if (mainData.hSemaphoreWrite == NULL || mainData.hSemaphoreRead == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar semáforos para o buffer circular!\n"), GetLastError());

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Cria o FileMapping do buffer circular
	hFileMapCircular = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(sharedMemoryStruct),
		FILE_MAPPING_CIRCULAR_NAME
	);

	if (hFileMapCircular == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping!\n"));

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	mainData.sharedMemory = (sharedMemoryStruct*)MapViewOfFile(
		hFileMapCircular,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		0
	);

	if (mainData.sharedMemory == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Preenchemos a estrutura de apoio ao buffer circular
	mainData.airplanesArray = thisMain->airplanesArray;
	mainData.hThreadPingArray = hThreadPingArray;
	mainData.nrAirplanes = thisMain->nrAirplanes;
	mainData.maxAirplanes = thisMain->maxAirplanes;
	mainData.nrPassengers = thisMain->nrPassengers;
	mainData.hMainWnd = thisMain->hMainWnd;
	mainData.criticalSectionAirplanes = thisMain->criticalSectionAirplanes;
	mainData.criticalSectionBool = thisMain->criticalSectionBool;
	mainData.criticalSectionPings = &criticalSectionPings;
	mainData.criticalSectionPassengers = thisMain->criticalSectionPassengers;
	mainData.stop = thisMain->stop;

	// Preenchemos a estrutura da memoria partilhada
	mainData.sharedMemory->nProducers = 0;
	mainData.sharedMemory->writeIndex = 0;
	mainData.sharedMemory->readIndex = 0;

	// Lança a Thread do consumidor
	hThreadConsumer = CreateThread(
		NULL,
		0,
		consumerThread,
		&mainData,
		0,
		NULL
	);

	if (hThreadConsumer == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir a thread consumidor!\n"));

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Cria o FileMapping das coordenadas
	hFileMapCoordinates = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(sharedCoordinatesStruct) * (*thisMain->maxAirplanes),
		FILE_MAPPING_COORDINATES_NAME
	);

	if (hFileMapCoordinates == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o FileMapping das Coordenadas!\n"));

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	sharedCoordinatesArray = (pSharedCoordinatesStruct)MapViewOfFile(
		hFileMapCoordinates,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		0
	);

	if (sharedCoordinatesArray == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Preenchemos a estrutura da thread de coordenadas
	mainCoordinates.sharedMemory = sharedCoordinatesArray;
	mainCoordinates.airplanesArray = thisMain->airplanesArray;
	mainCoordinates.size = *thisMain->maxAirplanes;
	mainCoordinates.criticalSectionAirplanes = thisMain->criticalSectionAirplanes;
	mainCoordinates.criticalSectionBool = thisMain->criticalSectionBool;
	mainCoordinates.stop = thisMain->stop;

	// Criamos o mutex
	mainCoordinates.hMutex = CreateMutex(
		NULL,
		FALSE,
		MUTEX_COORDINATES_NAME
	);

	if (mainCoordinates.hMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);
		UnmapViewOfFile(sharedCoordinatesArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	// Lançamos a thread das coordenadas
	hThreadCoordinates = CreateThread(
		NULL,
		0,
		coordinatesThread,
		&mainCoordinates,
		0,
		NULL
	);

	if (hThreadCoordinates == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread das coordenadas!\n"));

		EnterCriticalSection(thisMain->criticalSectionBool);
		*thisMain->stop = TRUE;
		LeaveCriticalSection(thisMain->criticalSectionBool);

		WaitForSingleObject(hThreadPipe, INFINITE);
		WaitForSingleObject(hThreadConsumer, INFINITE);
		WaitForMultipleObjects(*thisMain->nrAirplanes, hThreadPingArray, TRUE, INFINITE);

		UnmapViewOfFile(thisMain->airportsSharedArray);
		UnmapViewOfFile(mainData.sharedMemory);
		UnmapViewOfFile(sharedCoordinatesArray);

		free(thisMain->airportsArray);
		free(thisMain->airplanesArray);
		free(hThreadPingArray);
		free(thisMain->passengersArray);

		return -1;
	}

	SetEvent(thisMain->hSetupFinishedEvent);

	EnterCriticalSection(thisMain->criticalSectionBool);
	while (!*thisMain->stop) {
		LeaveCriticalSection(thisMain->criticalSectionBool);
		Sleep(1500);
		EnterCriticalSection(thisMain->criticalSectionBool);
	}
	LeaveCriticalSection(thisMain->criticalSectionBool);

	WaitForSingleObject(hThreadPipe, INFINITE);
	WaitForSingleObject(hThreadConsumer, INFINITE);
	WaitForSingleObject(hThreadCoordinates, INFINITE);
	WaitForMultipleObjects(*thisMain->nrAirplanes, hThreadPingArray, TRUE, INFINITE);

	UnmapViewOfFile(mainData.sharedMemory);
	UnmapViewOfFile(sharedCoordinatesArray);
	UnmapViewOfFile(thisMain->airportsSharedArray);

	_tprintf(TEXT("Programa finalizado com sucesso!\n"));

	free(thisMain->airportsArray);
	free(thisMain->airplanesArray);
	free(hThreadPingArray);
	free(thisMain->passengersArray);

	SetEvent(hStopEvent);

	// Handles são fechados automaticamente no final do programa

	return 0;
}

// Thread do Consumidor
DWORD WINAPI consumerThread(LPVOID param) {
	pDataThreadStruct data = (pDataThreadStruct)param;
	unsigned int i;
	DWORD error;
	airplane aux;
	BOOL airplaneExists = FALSE;

	HANDLE hGenericEvent;
	TCHAR genericEventName[STR_SIZE] = TEXT("");
	TCHAR tempStr1[STR_SIZE], tempStr2[STR_SIZE];

	unsigned int tempUInt;

	// Entramos na secção critica do bool
	EnterCriticalSection(data->criticalSectionBool);
	while (!*data->stop) {
		LeaveCriticalSection(data->criticalSectionBool);

		airplaneExists = FALSE;

		// Esperamos por uma posição para ler
		error = WaitForSingleObject(data->hSemaphoreRead, 1000);

		if (error == WAIT_TIMEOUT) {
			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		// Copiamos a proxima posicao de leitura do buffer circular para a nossa struct auxiliar
		CopyMemory(
			&aux,
			&data->sharedMemory->buffer[data->sharedMemory->readIndex],
			sizeof(airplane)
		);

		// Incrementa a posicao de leitura
		data->sharedMemory->readIndex++;

		// Entramos na secção critica dos avioes
		EnterCriticalSection(data->criticalSectionAirplanes);
		// Verifica se o aviao ja existe, failsafe
		for (i = 0; i < *data->nrAirplanes; i++) {
			if (aux.id == data->airplanesArray[i].id) {
				airplaneExists = TRUE;
				break;
			}
		}
		// Caso exista, atualizamos a struct local
		if (airplaneExists) {
			while (1) {
				// Enquanto estiver à espera de passageiros, assinala evento
				if (aux.getOnBoard && aux.stopped) {
					_tcscpy_s(tempStr1, _countof(tempStr1), aux.srcAirport);
					_tcscpy_s(tempStr2, _countof(tempStr2), aux.destAirport);

					toUpperString(tempStr1);
					toUpperString(tempStr2);

					_stprintf_s(genericEventName, _countof(genericEventName) - 1, TEXT("ONBOARD_%s_%s"), tempStr1, tempStr2);
					hGenericEvent = CreateEvent(
						NULL,
						TRUE,
						FALSE,
						genericEventName
					);

					if (hGenericEvent == NULL) {
						_ftprintf(stderr, TEXT("[ERRO] Um avião pediu para passageiros embarcarem, mas não foi possível fazê-lo!"));
						break;
					}

					EnterCriticalSection(data->criticalSectionPassengers);
					for (unsigned int i = 0; i < *data->nrPassengers; i++) {
						SetEvent(hGenericEvent);
						ResetEvent(hGenericEvent);
					}
					LeaveCriticalSection(data->criticalSectionPassengers);

					break;
				}

				// Se ele estiver em andamento ou for a primeira vez que para depois de ter viajado, assinala evento
				if (!aux.stopped || (aux.stopped != data->airplanesArray[i].stopped && !data->airplanesArray[i].stopped)) {
					_stprintf_s(genericEventName, _countof(genericEventName) - 1, TEXT("MOVED_%ld"), aux.id);
					hGenericEvent = CreateEvent(
						NULL,
						TRUE,
						FALSE,
						genericEventName
					);

					if (hGenericEvent == NULL) {
						_ftprintf(stderr, TEXT("[ERRO] Um avião mexeu-se mas não foi possível avisar os passageiros!"));
						break;
					}

					for (unsigned int i = 0; i < *data->nrAirplanes; i++) {
						if (data->airplanesArray[i].id == aux.id) {
							for (unsigned int j = 0; j < data->airplanesArray[i].currentPassengers; j++) {
								SetEvent(hGenericEvent);
								ResetEvent(hGenericEvent);
							}

							if (aux.stopped != data->airplanesArray[i].stopped && !data->airplanesArray[i].stopped) {
								data->airplanesArray[i].currentPassengers = 0;
							}

							break;
						}
					}

					break;
				}

				break;
			}

			tempUInt = data->airplanesArray[i].currentPassengers;

			CopyMemory(
				&data->airplanesArray[i],
				&aux,
				sizeof(airplane)
			);

			data->airplanesArray[i].currentPassengers = tempUInt;

			LeaveCriticalSection(data->criticalSectionAirplanes);

			// Caso cheguemos ao fim do buffer circular, voltamos à primeira posição do mesmo
			if (data->sharedMemory->readIndex == CIRCULAR_BUFFER_SIZE) data->sharedMemory->readIndex = 0;

			// Libertamos o semáforo de escrita
			ReleaseSemaphore(data->hSemaphoreWrite, 1, NULL);

			// Força WM_PAINT na janela principal
			RECT tempRect;
			GetClientRect(data->hMainWnd, &tempRect);

			tempRect.right -= tempRect.right - (tempRect.left + MAP_OFFSET + MAP_SIDE + MAP_OFFSET + BITMAP_SIZE);

			InvalidateRect(data->hMainWnd, &tempRect, FALSE);

			// Entramos na secção critica do bool porque saimos no inicio do while
			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		// Se não houver espaço para avioes ...
		if (*data->nrAirplanes >= *data->maxAirplanes) {
			_tprintf(TEXT("[AVISO] Um avião tentou conectar-se, mas já foi atingido o número máximo de aviões!\n"));
			LeaveCriticalSection(data->criticalSectionAirplanes);

			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		pPingThreadStruct temp = malloc(sizeof(pingThreadStruct));

		// Adicionamos o aviao à thread dos pings para ele começar a enviar pings ao controlador aereo
		temp->thisAirplaneId = aux.id;
		temp->airplanesArray = data->airplanesArray;
		temp->hMainWnd = data->hMainWnd;
		temp->hThreadPingArray = data->hThreadPingArray;
		temp->nrAirplanes = data->nrAirplanes;
		temp->criticalSectionAirplanes = data->criticalSectionAirplanes;
		temp->criticalSectionBool = data->criticalSectionBool;
		temp->criticalSectionPings = data->criticalSectionPings;
		temp->stop = data->stop;

		EnterCriticalSection(data->criticalSectionPings);
		// Lançamos a Thread dos pings
		data->hThreadPingArray[*data->nrAirplanes] = CreateThread(
			NULL,
			0,
			pingThread,
			temp,
			0,
			NULL
		);

		if (data->hThreadPingArray[*data->nrAirplanes] == NULL) {
			_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread dos pings para avião %i!\n"), aux.id);

			LeaveCriticalSection(data->criticalSectionAirplanes);
			LeaveCriticalSection(data->criticalSectionPings);

			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		LeaveCriticalSection(data->criticalSectionPings);

		// Inserimos o aviao no array de aviões
		copyAirplane(&data->airplanesArray[(*data->nrAirplanes)++], aux);
		LeaveCriticalSection(data->criticalSectionAirplanes);

		// Caso cheguemos ao fim do buffer circular, voltamos à primeira posição do mesmo
		if (data->sharedMemory->readIndex == CIRCULAR_BUFFER_SIZE) data->sharedMemory->readIndex = 0;

		// Libertamos o semáforo de escrita
		ReleaseSemaphore(data->hSemaphoreWrite, 1, NULL);

		EnterCriticalSection(data->criticalSectionBool);
	}
	LeaveCriticalSection(data->criticalSectionBool);

	return 0;
}

// Thread das coordenadas
DWORD WINAPI coordinatesThread(LPVOID param) {
	pCoordThreadStruct coordinates = (pCoordThreadStruct)param;

	// Espera que o mutex esteja livre e entra na critical section dos avioes
	WaitForSingleObject(coordinates->hMutex, INFINITE);
	EnterCriticalSection(coordinates->criticalSectionAirplanes);
	// Preenche inicialmente a memoria partilhada com informação local
	for (unsigned int i = 0; i < coordinates->size; i++) {
		coordinates->sharedMemory[i].x = coordinates->airplanesArray[i].currentCoordinates.x;
		coordinates->sharedMemory[i].y = coordinates->airplanesArray[i].currentCoordinates.y;
		coordinates->sharedMemory[i].maxAirplanes = coordinates->size;
	}
	LeaveCriticalSection(coordinates->criticalSectionAirplanes);
	ReleaseMutex(coordinates->hMutex);

	EnterCriticalSection(coordinates->criticalSectionBool);
	while (!*coordinates->stop) {
		LeaveCriticalSection(coordinates->criticalSectionBool);

		// Espera que o aviao tenha lido a memoria partilhada
		WaitForSingleObject(coordinates->hMutex, INFINITE);
		EnterCriticalSection(coordinates->criticalSectionAirplanes);

		// Atualizar a memoria partilhada com a nova informação
		for (unsigned int i = 0; i < coordinates->size; i++) {
			coordinates->sharedMemory[i].x = coordinates->airplanesArray[i].currentCoordinates.x;
			coordinates->sharedMemory[i].y = coordinates->airplanesArray[i].currentCoordinates.y;
		}

		LeaveCriticalSection(coordinates->criticalSectionAirplanes);
		// Libertamos o mutex para o aviao conseguir aceder
		ReleaseMutex(coordinates->hMutex);
		Sleep(10);

		EnterCriticalSection(coordinates->criticalSectionBool);
	}
	LeaveCriticalSection(coordinates->criticalSectionBool);

	return 0;
}

// Thread dos pings
DWORD WINAPI pingThread(LPVOID param) {
	pPingThreadStruct pingStruct = (pPingThreadStruct)param;
	TCHAR eventName[STR_SIZE];
	DWORD error;
	HANDLE hEvent;

	HANDLE hGenericEvent;
	TCHAR genericEventName[STR_SIZE];

	// Cria um evento com o id do aviao
	_stprintf_s(eventName, _countof(eventName) - 1, TEXT("EVENT_PING_%i"), pingStruct->thisAirplaneId);

	hEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		eventName
	);

	// Caso o evento dê erro
	if (hEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível iniciar o evento de ping para o avião %i!\n"), pingStruct->thisAirplaneId);

		EnterCriticalSection(pingStruct->criticalSectionBool);
		*pingStruct->stop = TRUE;
		LeaveCriticalSection(pingStruct->criticalSectionBool);

		return -1;
	}

	_stprintf_s(genericEventName, _countof(genericEventName) - 1, TEXT("MOVED_%ld"), pingStruct->thisAirplaneId);

	hGenericEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		genericEventName
	);

	if (hGenericEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível iniciar o evento genérico na thread ping para o avião %i!\n"), pingStruct->thisAirplaneId);

		EnterCriticalSection(pingStruct->criticalSectionBool);
		*pingStruct->stop = TRUE;
		LeaveCriticalSection(pingStruct->criticalSectionBool);

		return -1;
	}

	// Verifica os eventos que estao assinalados

	EnterCriticalSection(pingStruct->criticalSectionBool);
	while (!*pingStruct->stop) {
		LeaveCriticalSection(pingStruct->criticalSectionBool);

		// Espera 3 segundos pelo evento para que um aviao assinele que esta vivo
		error = WaitForSingleObject(hEvent, 3000);

		// Se passarem os 3 segundos assumimos que o aviao deixou de responder
		if (error == WAIT_TIMEOUT) {
			EnterCriticalSection(pingStruct->criticalSectionAirplanes);
			EnterCriticalSection(pingStruct->criticalSectionPings);

			for (unsigned int i = 0; i < *pingStruct->nrAirplanes; i++) {
				if (pingStruct->airplanesArray[i].id == pingStruct->thisAirplaneId) {
					for (unsigned int j = 0; j < pingStruct->airplanesArray[i].currentPassengers; j++) {
						SetEvent(hGenericEvent);
						ResetEvent(hGenericEvent);
					}

					break;
				}
			}

			// Retira o aviao do array de avioes
			if (!removeAirplaneFromArray(pingStruct->airplanesArray, pingStruct->nrAirplanes, pingStruct->thisAirplaneId, pingStruct->hThreadPingArray)) {
				_ftprintf(stderr, TEXT("[ERRO] Um avião que não existia foi tentado ser removido!\n"));
			}
			LeaveCriticalSection(pingStruct->criticalSectionAirplanes);
			LeaveCriticalSection(pingStruct->criticalSectionPings);

			// Força WM_PAINT na janela principal
			RECT tempRect;
			GetClientRect(pingStruct->hMainWnd, &tempRect);

			tempRect.right -= tempRect.right - (tempRect.left + MAP_OFFSET + MAP_SIDE + MAP_OFFSET + BITMAP_SIZE);

			InvalidateRect(pingStruct->hMainWnd, &tempRect, FALSE);

			EnterCriticalSection(pingStruct->criticalSectionBool);
			break;
		}

		EnterCriticalSection(pingStruct->criticalSectionBool);
	}
	LeaveCriticalSection(pingStruct->criticalSectionBool);

	free(pingStruct);

	return 0;
}

DWORD WINAPI pipeThread(LPVOID param) {
	pPipeThreadStruct data = (pPipeThreadStruct)param;

	BOOL continueFlag;

	namedPipeStruct hPipes[MAX_PASSENGERS];
	HANDLE hEvents[MAX_PASSENGERS];
	HANDLE hThreadPassengersArray[MAX_PASSENGERS];

	HANDLE hPipeTemp, hPipeEventTemp;

	pPassengerThreadStruct passengerThreadStructTemp;
	DWORD offset, nBytes;
	int i;

	for (unsigned int i = 0; i < MAX_PASSENGERS; i++) {
		// aqui passamos a constante FILE_FLAG_OVERLAPPED para o named pipe aceitar comunicações assincronas
		hPipeTemp = CreateNamedPipe(
			PASSENGERS_PIPE_NAME,
			PIPE_ACCESS_DUPLEX | FILE_FLAG_OVERLAPPED,
			PIPE_WAIT | PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE,
			MAX_PASSENGERS,
			sizeof(passenger),
			sizeof(passenger),
			1000,
			NULL
		);

		if (hPipeTemp == INVALID_HANDLE_VALUE) {
			_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o pipe (%i)."), i);

			EnterCriticalSection(data->criticalSectionBool);
			*data->stop = TRUE;
			LeaveCriticalSection(data->criticalSectionBool);

			return -1;
		}

		// criar evento que vai ser associado à esturtura overlaped
		// os eventos aqui tem de ter sempre reset manual e nao automático porque temos de delegar essas responsabilidades ao sistema operativo
		hPipeEventTemp = CreateEvent(
			NULL,
			TRUE,
			FALSE,
			NULL
		);

		if (hPipeEventTemp == NULL) {
			_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar evento para pipe (%i).\n"), i);

			EnterCriticalSection(data->criticalSectionBool);
			*data->stop = TRUE;
			LeaveCriticalSection(data->criticalSectionBool);

			return -1;
		}

		hPipes[i].hPipe = hPipeTemp;
		hPipes[i].active = FALSE;
		//temos de garantir que a estrutura overlap está limpa
		ZeroMemory(
			&hPipes[i].overlap,
			sizeof(hPipes[i].overlap)
		);

		//preenchemos agora o evento
		hPipes[i].overlap.hEvent = hPipeEventTemp;
		hEvents[i] = hPipeEventTemp;

		// _tprintf(TEXT("[DEBUG] Esperar ligação de um passageiro... (ConnectNamedPipe)\n"));

		// aqui passamos um ponteiro para a estrutura overlap
		ConnectNamedPipe(hPipeTemp, &hPipes[i].overlap);
	}

	EnterCriticalSection(data->criticalSectionBool);
	while (!*data->stop && *data->nrPassengers < MAX_PASSENGERS) {
		LeaveCriticalSection(data->criticalSectionBool);

		continueFlag = FALSE;

		// Permite estar bloqueado , à espera que 1 evento do array de eventos seja assinalado
		for (DWORD nrWaits = 0; nrWaits + MAXIMUM_WAIT_OBJECTS < MAX_PASSENGERS; ) {
			offset = WaitForMultipleObjects(MAXIMUM_WAIT_OBJECTS, &hEvents[nrWaits], FALSE, 10);

			// Um dos eventos estava assinalado (não deu timeout)
			if (offset != WAIT_TIMEOUT) {
				i = (nrWaits + offset) - WAIT_OBJECT_0;
				break;
			}

			// Já experimentei fazer wait a todos
			if (nrWaits + MAXIMUM_WAIT_OBJECTS == MAX_PASSENGERS - 1) {
				continueFlag = TRUE;
				break;
			}

			nrWaits += MAXIMUM_WAIT_OBJECTS;

			// No caso de o Wait ficar à espera de eventos que são lixo
			// Isto garante que todos são vistos
			// (alguns poderão vistos 2x)
			if (nrWaits + MAXIMUM_WAIT_OBJECTS >= MAX_PASSENGERS) {
				nrWaits = MAX_PASSENGERS - MAXIMUM_WAIT_OBJECTS - 1;
			}
		}

		if (continueFlag) {
			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		// se é um indice válido ...
		if (i >= 0 && i < MAX_PASSENGERS) {
			EnterCriticalSection(data->criticalSectionPipe);
			if (GetOverlappedResult(hPipes[i].hPipe, &hPipes[i].overlap, &nBytes, FALSE)) {
				// se entrarmos aqui significa que a funcao correu tudo bem
				// fazemos reset do evento porque queremos que o WaitForMultipleObject desbloqueio com base noutro evento e nao neste
				ResetEvent(hEvents[i]);

				//vamos esperar que o mutex esteja livre
				hPipes[i].active = TRUE; // dizemos que esta instancia do named pipe está ativa

				passengerThreadStructTemp = malloc(sizeof(passengerThreadStruct));

				passengerThreadStructTemp->thisPassengerId = 0;
				passengerThreadStructTemp->namedPipe = &hPipes[i];
				passengerThreadStructTemp->airportsArray = data->airportsArray;
				passengerThreadStructTemp->nrAirports = data->nrAirports;
				passengerThreadStructTemp->airplanesArray = data->airplanesArray;
				passengerThreadStructTemp->nrAirplanes = data->nrAirplanes;
				passengerThreadStructTemp->passengersArray = data->passengersArray;
				passengerThreadStructTemp->nrPassengers = data->nrPassengers;
				passengerThreadStructTemp->hThreadPassengersArray = hThreadPassengersArray;
				passengerThreadStructTemp->criticalSectionAirports = data->criticalSectionAirports;
				passengerThreadStructTemp->criticalSectionPassengers = data->criticalSectionPassengers;
				passengerThreadStructTemp->criticalSectionBool = data->criticalSectionBool;
				passengerThreadStructTemp->criticalSectionAirplanes = data->criticalSectionAirplanes;
				passengerThreadStructTemp->stop = data->stop;

				hThreadPassengersArray[*data->nrPassengers] = CreateThread(
					NULL,
					0,
					passengerThread,
					passengerThreadStructTemp,
					0,
					NULL
				);

				if (hThreadPassengersArray[*data->nrPassengers] == NULL) {
					_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar a thread %i para passageiro!\n"), GetLastError(), *data->nrPassengers);

					LeaveCriticalSection(data->criticalSectionPipe);

					EnterCriticalSection(data->criticalSectionBool);
					continue;
				}
			}
			LeaveCriticalSection(data->criticalSectionPipe);
		}

		EnterCriticalSection(data->criticalSectionBool);
	}
	LeaveCriticalSection(data->criticalSectionBool);

	EnterCriticalSection(data->criticalSectionPassengers);
	while (1) {
		if (*data->nrPassengers < MAXIMUM_WAIT_OBJECTS) {
			if (WaitForMultipleObjects(*data->nrPassengers, hThreadPassengersArray, TRUE, 1000) == WAIT_TIMEOUT) {
				LeaveCriticalSection(data->criticalSectionPassengers);

				EnterCriticalSection(data->criticalSectionPassengers);
				continue;
			}

			LeaveCriticalSection(data->criticalSectionPassengers);
			return 0;
		}
	}

	for (DWORD nrWaits = 0; nrWaits + MAXIMUM_WAIT_OBJECTS < *data->nrPassengers; ) {
		offset = WaitForMultipleObjects(MAXIMUM_WAIT_OBJECTS, &hEvents[nrWaits], TRUE, INFINITE);

		// Um dos eventos estava assinalado (não deu timeout)
		if (offset != WAIT_TIMEOUT) {
			i = (nrWaits + offset) - WAIT_OBJECT_0;
			break;
		}

		// Já experimentei fazer wait a todos
		if (nrWaits + MAXIMUM_WAIT_OBJECTS == MAX_PASSENGERS - 1) {
			continueFlag = TRUE;
			break;
		}

		nrWaits += MAXIMUM_WAIT_OBJECTS;

		// No caso de o Wait ficar à espera de eventos que são lixo
		// Isto garante que todos são vistos
		// (alguns poderão vistos 2x)
		if (nrWaits + MAXIMUM_WAIT_OBJECTS >= MAX_PASSENGERS) {
			nrWaits = MAX_PASSENGERS - MAXIMUM_WAIT_OBJECTS - 1;
		}
	}
	LeaveCriticalSection(data->criticalSectionPassengers);

	return 0;
}

DWORD WINAPI passengerThread(LPVOID param) {
	pPassengerThreadStruct thisPassenger = (pPassengerThreadStruct)param;
	BOOL ret, breakFlag;
	passenger aux;
	DWORD nBytes;
	unsigned int nrValidAirports = 0;
	TCHAR strWrite[STR_SIZE] = TEXT("");

	DWORD airplaneID = 0;

	HANDLE hGenericEvent;
	TCHAR genericEventName[STR_SIZE] = TEXT("");
	TCHAR tempStr1[STR_SIZE] = TEXT("");
	TCHAR tempStr2[STR_SIZE] = TEXT("");

	// fazer read da struct do passageiro na primeira vez
	ret = ReadFile(
		thisPassenger->namedPipe->hPipe,
		&aux,
		sizeof(passenger),
		&nBytes,
		NULL
	);

	if (!ret || !nBytes) {
		_ftprintf(stderr, TEXT("[ERRO] Erro ao fazer ReadFile %d %d..)\n"), ret, nBytes);

		free(thisPassenger);
		return -1;
	}

	// verifica se o aeroporto de destino e o aeroporto de origem existem
	EnterCriticalSection(thisPassenger->criticalSectionAirports);
	for (unsigned int i = 0; i < *thisPassenger->nrAirports; i++) {
		if (_tcsicmp(aux.destAirport, thisPassenger->airportsArray[i].name) == 0 || _tcsicmp(aux.srcAirport, thisPassenger->airportsArray[i].name) == 0) {
			nrValidAirports++;
		}
	}
	LeaveCriticalSection(thisPassenger->criticalSectionAirports);

	if (nrValidAirports != 2) {
		_ftprintf(stderr, TEXT("[ERRO] Passageiro %s introduziu um aeroporto inválido!\n"), aux.name);

		_stprintf_s(strWrite, _countof(strWrite) - 1, TEXT("%ld"), 0);

		ret = WriteFile(
			thisPassenger->namedPipe->hPipe,
			strWrite,
			_tcslen(strWrite) * sizeof(TCHAR),
			&nBytes,
			NULL
		);

		if (!ret || !nBytes) {
			_ftprintf(stderr, TEXT("[ERRO] Não foi possível escrever no pipe do passageiro %s!\n"), aux.name);

			free(thisPassenger);
			return -1;
		}

		free(thisPassenger);
		return -1;
	}

	EnterCriticalSection(thisPassenger->criticalSectionPassengers);
	aux.id = getPassengerID();
	copyPassenger(&thisPassenger->passengersArray[(*thisPassenger->nrPassengers)++], aux);
	LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

	thisPassenger->thisPassengerId = aux.id;

	_stprintf_s(strWrite, _countof(strWrite) - 1, TEXT("%ld"), aux.id);

	ret = WriteFile(
		thisPassenger->namedPipe->hPipe,
		strWrite,
		_tcslen(strWrite) * sizeof(TCHAR),
		&nBytes,
		NULL
	);

	if (!ret || !nBytes) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível escrever no pipe do passageiro %s!\n"), aux.name);

		EnterCriticalSection(thisPassenger->criticalSectionPassengers);
		removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
		LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

		free(thisPassenger);
		return -1;
	}

	_tcscpy_s(tempStr1, _countof(tempStr1), aux.srcAirport);
	_tcscpy_s(tempStr2, _countof(tempStr2), aux.destAirport);

	toUpperString(tempStr1);
	toUpperString(tempStr2);

	_stprintf_s(genericEventName, _countof(genericEventName) - 1, TEXT("ONBOARD_%s_%s"), tempStr1, tempStr2);

	hGenericEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		genericEventName
	);

	if (hGenericEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] O passageiro %s não conseguiu obter evento para embarcar!\n"), aux.name);

		EnterCriticalSection(thisPassenger->criticalSectionPassengers);
		removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
		LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

		free(thisPassenger);
		return -1;
	}

	EnterCriticalSection(thisPassenger->criticalSectionBool);
	while (!*thisPassenger->stop) {
		LeaveCriticalSection(thisPassenger->criticalSectionBool);

		if (WaitForSingleObject(hGenericEvent, 3000) == WAIT_TIMEOUT) {
			EnterCriticalSection(thisPassenger->criticalSectionBool);
			continue;
		}

		breakFlag = FALSE;

		EnterCriticalSection(thisPassenger->criticalSectionAirplanes);
		for (unsigned int i = 0; i < *thisPassenger->nrAirplanes; i++) {
			if (_tcsicmp(thisPassenger->airplanesArray[i].srcAirport, aux.srcAirport) == 0 && _tcsicmp(thisPassenger->airplanesArray[i].destAirport, aux.destAirport) == 0) {
				if (thisPassenger->airplanesArray[i].currentPassengers < thisPassenger->airplanesArray[i].capacity && thisPassenger->airplanesArray[i].stopped) {
					thisPassenger->airplanesArray[i].currentPassengers++;
					airplaneID = thisPassenger->airplanesArray[i].id;
					breakFlag = TRUE;
				}
				break;
			}
		}
		LeaveCriticalSection(thisPassenger->criticalSectionAirplanes);

		EnterCriticalSection(thisPassenger->criticalSectionBool);
		if (breakFlag) break;
	}
	LeaveCriticalSection(thisPassenger->criticalSectionBool);

	EnterCriticalSection(thisPassenger->criticalSectionBool);
	if (*thisPassenger->stop) {
		LeaveCriticalSection(thisPassenger->criticalSectionBool);

		EnterCriticalSection(thisPassenger->criticalSectionPassengers);
		removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
		LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

		free(thisPassenger);
		return -1;
	}
	LeaveCriticalSection(thisPassenger->criticalSectionBool);

	_stprintf_s(strWrite, _countof(strWrite) - 1, TEXT("Acabou de embarcar num avião! Prepare-se para a descolagem!"));

	EnterCriticalSection(thisPassenger->criticalSectionPassengers);
	aux.stopped = FALSE;

	for (unsigned int i = 0; i < *thisPassenger->nrPassengers; i++) {
		if (thisPassenger->passengersArray[i].id == aux.id) {
			thisPassenger->passengersArray[i].airplaneId = airplaneID;
			thisPassenger->passengersArray[i].stopped = aux.stopped;
		}
	}
	LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

	ret = WriteFile(
		thisPassenger->namedPipe->hPipe,
		strWrite,
		_tcslen(strWrite) * sizeof(TCHAR),
		&nBytes,
		NULL
	);

	if (!ret || !nBytes) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível escrever no pipe do passageiro %s!\n"), aux.name);

		EnterCriticalSection(thisPassenger->criticalSectionPassengers);
		removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
		LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

		free(thisPassenger);
		return -1;
	}

	_stprintf_s(genericEventName, _countof(genericEventName) - 1, TEXT("MOVED_%ld"), airplaneID);

	hGenericEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		genericEventName
	);

	if (hGenericEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] O passageiro %s não conseguiria saber que se está a mexer!"), aux.name);

		EnterCriticalSection(thisPassenger->criticalSectionPassengers);
		removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
		LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

		free(thisPassenger);
		return -1;
	}

	EnterCriticalSection(thisPassenger->criticalSectionBool);
	while (!*thisPassenger->stop) {
		LeaveCriticalSection(thisPassenger->criticalSectionBool);

		if (WaitForSingleObject(hGenericEvent, 3000) == WAIT_TIMEOUT) {
			EnterCriticalSection(thisPassenger->criticalSectionBool);
			continue;
		}

		breakFlag = TRUE;

		EnterCriticalSection(thisPassenger->criticalSectionAirplanes);
		for (unsigned int i = 0; i < *thisPassenger->nrAirplanes; i++) {
			if (thisPassenger->airplanesArray[i].id == airplaneID) {
				breakFlag = FALSE;

				// Se o voo tiver acabado (avião chegou ao destino)
				if (thisPassenger->airplanesArray[i].stopped) {
					_stprintf_s(strWrite, _countof(strWrite) - 1, PIPE_COMMAND_FLIGHT_ENDED);
					break;
				}

				_stprintf_s(strWrite, _countof(strWrite) - 1, TEXT("Está agora a voar sobre (%i, %i)!"), thisPassenger->airplanesArray[i].currentCoordinates.x, thisPassenger->airplanesArray[i].currentCoordinates.y);
				break;
			}
		}
		LeaveCriticalSection(thisPassenger->criticalSectionAirplanes);

		if (!breakFlag) {
			ret = WriteFile(
				thisPassenger->namedPipe->hPipe,
				strWrite,
				_tcslen(strWrite) * sizeof(TCHAR),
				&nBytes,
				NULL
			);

			if (!ret || !nBytes) {
				_ftprintf(stderr, TEXT("[ERRO] Não foi possível escrever no pipe do passageiro %s!\n"), aux.name);

				EnterCriticalSection(thisPassenger->criticalSectionPassengers);
				removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
				LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

				free(thisPassenger);
				return -1;
			}

			// Se o voo tiver acabado, sair do loop de escrita / espera por evento
			if (_tcscmp(strWrite, PIPE_COMMAND_FLIGHT_ENDED) == 0) {
				EnterCriticalSection(thisPassenger->criticalSectionBool);
				break;
			}

			// O voo ainda nã acabou; volta par ao início
			EnterCriticalSection(thisPassenger->criticalSectionBool);
			continue;
		}

		_stprintf_s(strWrite, _countof(strWrite) - 1, PIPE_COMMAND_AIRPLANE_CLOSED);

		ret = WriteFile(
			thisPassenger->namedPipe->hPipe,
			strWrite,
			_tcslen(strWrite) * sizeof(TCHAR),
			&nBytes,
			NULL
		);

		if (!ret || !nBytes) {
			_ftprintf(stderr, TEXT("[ERRO] Não foi possível escrever no pipe do passageiro %s!\n"), aux.name);

			EnterCriticalSection(thisPassenger->criticalSectionPassengers);
			removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
			LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

			free(thisPassenger);
			return -1;
		}

		EnterCriticalSection(thisPassenger->criticalSectionBool);
		break;
	}
	LeaveCriticalSection(thisPassenger->criticalSectionBool);

	EnterCriticalSection(thisPassenger->criticalSectionPassengers);
	removePassengerFromArray(thisPassenger->passengersArray, thisPassenger->nrPassengers, thisPassenger->thisPassengerId, thisPassenger->hThreadPassengersArray);
	LeaveCriticalSection(thisPassenger->criticalSectionPassengers);

	free(thisPassenger);
	return 0;
}