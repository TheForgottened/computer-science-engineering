#include "airplaneThreads.h"

#include "utils.h"
#include "structs.h"
#include "dllHandling.h"

DWORD WINAPI threadInterface(LPVOID param) {
	pInterfaceStruct mainInterface = (pInterfaceStruct)param;
	TCHAR command[STR_SIZE], ** commandArray = NULL;
	const TCHAR delim[2] = TEXT(" ");
	unsigned int nrArguments = 0;

	BOOL hasEmbarcado = FALSE;

	EnterCriticalSection(mainInterface->criticalSectionBool);
	while (!*mainInterface->stop) {
		LeaveCriticalSection(mainInterface->criticalSectionBool);

		_tprintf(TEXT("\nCOMANDO: "));
		_getts_s(command, _countof(command) - 1);

		free(commandArray); // a função free com um ponteiro de valor NULL não faz nada
		commandArray = splitString(command, delim, &nrArguments);

		if (commandArray == NULL) {
			EnterCriticalSection(mainInterface->criticalSectionBool);
			continue;
		}

		// Caso o utilizador queira terminar o programa enquanto está em viajem
		if (_tcscmp(commandArray[0], TEXT("terminar")) == 0) {
			if (nrArguments != 1) {
				_ftprintf(stderr, TEXT("[ERRO] Argumentos inválidos! Uso: terminar\n"));

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}
			_tprintf(TEXT("O programa vai terminar ...\n"));

			EnterCriticalSection(mainInterface->criticalSectionBool);
			break;
		}

		// No caso de o avião estar em voo, o utilizador apenas pode terminar o programa, não podendo executar outros comandos
		EnterCriticalSection(mainInterface->criticalSectionAirplane);
		if (!mainInterface->thisAirplane->stopped) {
			_tprintf(TEXT("Está em viagem, apenas pode terminar o programa!\n"));

			LeaveCriticalSection(mainInterface->criticalSectionAirplane);

			EnterCriticalSection(mainInterface->criticalSectionBool);
			continue;
		}
		LeaveCriticalSection(mainInterface->criticalSectionAirplane);

		// destino <nome_aeroporto>
		if (_tcscmp(commandArray[0], TEXT("destino")) == 0) {
			if (nrArguments != 2) {
				_ftprintf(stderr, TEXT("[ERRO-1] Argumentos inválidos! Uso: destino <nome_aeroporto>\n"));

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			if (hasEmbarcado) {
				_ftprintf(stderr, TEXT("[ERRO] Já mandou embarcar passageiros, não pode alterar o destino!\n"));

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			EnterCriticalSection(mainInterface->criticalSectionAirplane);

			// verifica se o destino inserido não é igual ao aeroporto onde se encontra no momento
			if (_tcsicmp(commandArray[1], mainInterface->thisAirplane->srcAirport) == 0) {
				_ftprintf(stderr, TEXT("[ERRO] Já se encontra nesse aeroporto!\n"));

				LeaveCriticalSection(mainInterface->criticalSectionAirplane);

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}
			// espera que o mutex da memoria partilhada que contem os aeroportos esteja livre
			WaitForSingleObject(mainInterface->hSharedAirportsMutex, INFINITE);
			
			unsigned int i;
			// encontra o aeroporto de destino e vai buscar as suas coordenadas
			for (i = 0; i < mainInterface->airportsSharedArray[0].maxAirports; i++) {
				if (_tcsicmp(commandArray[1], mainInterface->airportsSharedArray[i].name) == 0) {
					_tcscpy_s(mainInterface->thisAirplane->destAirport, _countof(mainInterface->thisAirplane->destAirport) - 1, mainInterface->airportsSharedArray[i].name);
					mainInterface->thisAirplane->destinationCoordinates = mainInterface->airportsSharedArray[i].coordinates;
					break;
				}
			}
			// se chegar ao fim e nao encontrar nenhum aeroporto, entao é porque não existe
			if (i == mainInterface->airportsSharedArray[0].maxAirports) {
				ReleaseMutex(mainInterface->hSharedAirportsMutex);

				_ftprintf(stderr, TEXT("[ERRO] Aeroporto não existe!\n"));

				LeaveCriticalSection(mainInterface->criticalSectionAirplane);

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			ReleaseMutex(mainInterface->hSharedAirportsMutex);

			*mainInterface->hasDestination = TRUE; // O utilizador ja definiu um destino

			// fazemos set do evento para que o controlador vá ler ao buffer circular
			SetEvent(mainInterface->hWriteToCircularBufferEvent);
			_tprintf(TEXT("Destino definido com sucesso!\n"));

			LeaveCriticalSection(mainInterface->criticalSectionAirplane);

			EnterCriticalSection(mainInterface->criticalSectionBool);
			continue;
		}

		// inicia viagem
		if (_tcscmp(commandArray[0], TEXT("inicia")) == 0) {
			if (nrArguments != 1) {
				_ftprintf(stderr, TEXT("[ERRO-1] Argumentos inválidos! Uso: inicia\n"));

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			EnterCriticalSection(mainInterface->criticalSectionAirplane);
			// verifica se o aeroporto de destino foi definido
			if (!(*mainInterface->hasDestination)) {
				_ftprintf(stderr, TEXT("[ERRO] Ainda não definiu um aeroporto de destino!\n"));

				LeaveCriticalSection(mainInterface->criticalSectionAirplane);

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			// inicializa a thread de movimentação
			mainInterface->hThreadMove = CreateThread(
				NULL,
				0,
				threadMove,
				mainInterface->thisMove,
				0,
				NULL
			);

			if (mainInterface->hThreadMove == NULL) {
				_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread de movimentação!\n"));

				LeaveCriticalSection(mainInterface->criticalSectionAirplane);

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			*mainInterface->hasDestination = FALSE;
			mainInterface->thisAirplane->getOnBoard = FALSE;
			hasEmbarcado = FALSE;

			_tprintf(TEXT("A iniciar descolagem...\n"));

			LeaveCriticalSection(mainInterface->criticalSectionAirplane);

			EnterCriticalSection(mainInterface->criticalSectionBool);
			continue;
		}

		// embarca passageiros
		if (_tcscmp(commandArray[0], TEXT("embarcar")) == 0) {
			if (nrArguments != 1) {
				_ftprintf(stderr, TEXT("[ERRO-1] Argumentos inválidos! Uso: inicia\n"));

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			EnterCriticalSection(mainInterface->criticalSectionAirplane);
			// verifica se o aeroporto de destino foi definido
			if (!(*mainInterface->hasDestination)) {
				_ftprintf(stderr, TEXT("[ERRO] Ainda não definiu um aeroporto de destino!\n"));

				LeaveCriticalSection(mainInterface->criticalSectionAirplane);

				EnterCriticalSection(mainInterface->criticalSectionBool);
				continue;
			}

			// Assinala que podem embarcar
			mainInterface->thisAirplane->getOnBoard = TRUE;
			hasEmbarcado = TRUE;
			// fazemos set do evento para que o controlador vá ler ao buffer circular
			SetEvent(mainInterface->hWriteToCircularBufferEvent);
			_tprintf(TEXT("Passageiros avisados!\n"));

			LeaveCriticalSection(mainInterface->criticalSectionAirplane);

			EnterCriticalSection(mainInterface->criticalSectionBool);
			continue;
		}

		_ftprintf(stderr, TEXT("[ERRO] Comando desconhecido!\n"));

		EnterCriticalSection(mainInterface->criticalSectionBool);
	}
	*mainInterface->stop = TRUE;
	LeaveCriticalSection(mainInterface->criticalSectionBool);

	free(commandArray);

	WaitForSingleObject(mainInterface->hThreadMove, INFINITE);

	return 0;
}

DWORD WINAPI threadMove(LPVOID param) {
	pMoveAirplaneStruct thisMove = (pMoveAirplaneStruct)param;
	HANDLE hMutex, hFileMap;
	pSharedCoordinatesStruct sharedMemory;
	DWORD waitTime = 1000 / thisMove->thisAirplane->velocity;

	coordinatesStruct nextCoordinates;
	int error, hotfiX = 0, hotfiY = 0;
	BOOL canMove;

	hMutex = OpenMutex(
		SYNCHRONIZE,
		FALSE,
		MUTEX_COORDINATES_NAME
	);

	if (hMutex == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível abrir o mutex das coordenadas!\n"), GetLastError());

		EnterCriticalSection(thisMove->criticalSectionBool);
		*thisMove->stop = TRUE;
		LeaveCriticalSection(thisMove->criticalSectionBool);

		return -1;
	}

	hFileMap = OpenFileMapping(
		FILE_MAP_ALL_ACCESS,
		FALSE,
		FILE_MAPPING_COORDINATES_NAME
	);

	if (hFileMap == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir o FileMapping para das coordenadas!\n"));
		
		EnterCriticalSection(thisMove->criticalSectionBool);
		*thisMove->stop = TRUE;
		LeaveCriticalSection(thisMove->criticalSectionBool);

		return -1;
	}

	// Mapeamos o bloco de memória para o espaço de endereçamento do processo
	sharedMemory = (pSharedCoordinatesStruct)MapViewOfFile(
		hFileMap,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		0
	);

	if (sharedMemory == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível mapear a memória a partilhar!\n"), GetLastError());
		
		EnterCriticalSection(thisMove->criticalSectionBool);
		*thisMove->stop = TRUE;
		LeaveCriticalSection(thisMove->criticalSectionBool);

		return -1;
	}

	EnterCriticalSection(thisMove->criticalSectionAirplane);
	thisMove->thisAirplane->stopped = FALSE;
	LeaveCriticalSection(thisMove->criticalSectionAirplane);

	// Movimentação do Avião
	EnterCriticalSection(thisMove->criticalSectionBool);
	while (!(*thisMove->stop)) {
		LeaveCriticalSection(thisMove->criticalSectionBool);
		canMove = TRUE;

		// Enquanto der erro na função da DLL, tentamos encontrar uma nova posição
		do {
			error = getNextPosition(
				thisMove->thisAirplane->currentCoordinates.x,
				thisMove->thisAirplane->currentCoordinates.y,
				thisMove->thisAirplane->destinationCoordinates.x + hotfiX,
				thisMove->thisAirplane->destinationCoordinates.y + hotfiY,
				&nextCoordinates.x,
				&nextCoordinates.y
			);
		} while (error == 2);

		EnterCriticalSection(thisMove->criticalSectionAirplane);

		// Avião chegou ao destino
		if (error == 0) {
			thisMove->thisAirplane->destinationCoordinates = nextCoordinates;
			
			*thisMove->hasArrivedAtDestination = TRUE;
			thisMove->thisAirplane->stopped = TRUE;
			_tcscpy_s(thisMove->thisAirplane->srcAirport, _countof(thisMove->thisAirplane->srcAirport) - 1, thisMove->thisAirplane->destAirport); // Agora o aeroporto de origem é igual ao aeroporto de destino
			_tcscpy_s(thisMove->thisAirplane->destAirport, _countof(thisMove->thisAirplane->destAirport) - 1, TEXT("")); // Limpamos o aeroporto de destino

			thisMove->thisAirplane->stopped = TRUE;
			LeaveCriticalSection(thisMove->criticalSectionAirplane);

			// Aciona o evento para escrever no buffer circular
			SetEvent(thisMove->hWriteToCircularBufferEvent);

			_tprintf(TEXT("Cheguei ao destino! [%s]\n"), thisMove->thisAirplane->srcAirport);

			EnterCriticalSection(thisMove->criticalSectionBool);
			break;
		}
		// se ainda nao tiver chegado ao destino ...
		if (nextCoordinates.x == thisMove->thisAirplane->destinationCoordinates.x && nextCoordinates.y == thisMove->thisAirplane->destinationCoordinates.y) {
			thisMove->thisAirplane->currentCoordinates = nextCoordinates;
			
			LeaveCriticalSection(thisMove->criticalSectionAirplane);
			
			Sleep(waitTime);

			_tprintf(TEXT("\nEstou agora em (%i, %i)!\n"), thisMove->thisAirplane->currentCoordinates.x, thisMove->thisAirplane->currentCoordinates.y);
			EnterCriticalSection(thisMove->criticalSectionBool);
			continue;
		}

		WaitForSingleObject(hMutex, INFINITE);

		// Verifica se tem algum avião nas coordenadas que pretende ir
		for (unsigned int i = 0; i < sharedMemory[0].maxAirplanes; i++) {
			if (sharedMemory[i].x == nextCoordinates.x && sharedMemory[i].y == nextCoordinates.y) {
				canMove = FALSE;
				break;
			}
		}

		// Se o avião não puder ir para onde inicialmente desejava
		if (!canMove) {
			ReleaseMutex(hMutex);
			LeaveCriticalSection(thisMove->criticalSectionAirplane);
			hotfiX = randomInt(0, 1000);
			hotfiY = randomInt(0, 1000);

			EnterCriticalSection(thisMove->criticalSectionBool);
			continue;
		}

		hotfiX = 0;
		hotfiY = 0;

		thisMove->thisAirplane->currentCoordinates = nextCoordinates;

		// Aciona o evento para escrever no buffer circular
		SetEvent(thisMove->hWriteToCircularBufferEvent);

		ReleaseMutex(hMutex);
		LeaveCriticalSection(thisMove->criticalSectionAirplane);

		_tprintf(TEXT("\nEstou agora em (%i, %i)!\n"), thisMove->thisAirplane->currentCoordinates.x, thisMove->thisAirplane->currentCoordinates.y);

		Sleep(waitTime);

		EnterCriticalSection(thisMove->criticalSectionBool);
	}
	LeaveCriticalSection(thisMove->criticalSectionBool);

	// Aciona o evento para escrever no buffer circular
	SetEvent(thisMove->hWriteToCircularBufferEvent);

	return 0;
}

DWORD WINAPI threadProducer(LPVOID param){
	pDataThreadStruct data = (pDataThreadStruct)param;

	EnterCriticalSection(data->criticalSectionBool);
	while (!*data->stop) {
		LeaveCriticalSection(data->criticalSectionBool);

		if (WaitForSingleObject(data->hWriteToCircularBufferEvent, 2000) == WAIT_TIMEOUT) {
			EnterCriticalSection(data->criticalSectionBool);
			continue;
		}

		// Thread única; reset event
		ResetEvent(data->hWriteToCircularBufferEvent);

		// Esperamos por uma posição de escrita
		WaitForSingleObject(data->hSemaphoreWrite, INFINITE);

		// Esperamos que o mutex esteja livre
		WaitForSingleObject(data->hMutex, INFINITE);

		EnterCriticalSection(data->criticalSectionAirplane);

		// Copiamos uma struct do tipo avião para a memória partilhada
		CopyMemory(
			&data->sharedMemory->buffer[data->sharedMemory->writeIndex],
			data->thisAirplane,
			sizeof(airplane)
		);

		LeaveCriticalSection(data->criticalSectionAirplane);

		data->sharedMemory->writeIndex++;

		if (data->sharedMemory->writeIndex == CIRCULAR_BUFFER_SIZE) data->sharedMemory->writeIndex = 0;

		ReleaseMutex(data->hMutex);

		ReleaseSemaphore(data->hSemaphoreRead, 1, NULL);

		EnterCriticalSection(data->criticalSectionBool);
	}
	LeaveCriticalSection(data->criticalSectionBool);

	return 0;
}

DWORD WINAPI threadPing(LPVOID param) {
	pPingThreadStruct pingStruct = (pPingThreadStruct)param;
	TCHAR eventName[STR_SIZE];
	HANDLE hEvent;

	_stprintf_s(eventName, _countof(eventName) - 1, TEXT("EVENT_PING_%i"), pingStruct->id);

	hEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		eventName
	);

	if (hEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível iniciar o evento de ping!\n"));

		EnterCriticalSection(pingStruct->criticalSectionBool);
		*pingStruct->stop = TRUE;
		LeaveCriticalSection(pingStruct->criticalSectionBool);

		return -1;
	}

	// vai enviando pings de 1000 milisegundos em 1000 milisegundos para o controlador
	EnterCriticalSection(pingStruct->criticalSectionBool);
	while (!*pingStruct->stop) {
		LeaveCriticalSection(pingStruct->criticalSectionBool);

		Sleep(1000);
		SetEvent(hEvent);
		ResetEvent(hEvent);

		EnterCriticalSection(pingStruct->criticalSectionBool);
	}
	LeaveCriticalSection(pingStruct->criticalSectionBool);

	return 0;
}

// vai ficar à espera de um evento caso o controlador feche
DWORD WINAPI stopEventThread(LPVOID param) {
	pStopEventThreadStruct thisStruct = (pStopEventThreadStruct)param;
	HANDLE hStopEvent;

	hStopEvent = OpenEvent(
		EVENT_ALL_ACCESS,
		FALSE,
		EVENT_CONTROL_CLOSED_NAME
	);

	if (hStopEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir o evento de paragem do Control!\n"));

		EnterCriticalSection(thisStruct->criticalSectionBool);
		*thisStruct->stop = TRUE;
		LeaveCriticalSection(thisStruct->criticalSectionBool);

		return -1;
	}

	EnterCriticalSection(thisStruct->criticalSectionBool);
	while (!*thisStruct->stop) {
		LeaveCriticalSection(thisStruct->criticalSectionBool);
		// espera 2000 milisegundos , se der timeout volta a esperar
		if (WaitForSingleObject(hStopEvent, 2000) == WAIT_TIMEOUT) {
			EnterCriticalSection(thisStruct->criticalSectionBool);
			continue;
		}

		EnterCriticalSection(thisStruct->criticalSectionBool);
		*thisStruct->stop = TRUE;
		break;
	}
	LeaveCriticalSection(thisStruct->criticalSectionBool);

	return 0;
}