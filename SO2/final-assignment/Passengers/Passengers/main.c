#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>
#include <io.h>

#include <stdio.h>
#include <ctype.h>

#include "utils.h"
#include "structs.h"

// função callback que é chamada caso o tempo de espera tenha terminado
VOID CALLBACK TimerAPCProc(LPVOID lpArg, DWORD dwTimerLowValue, DWORD dwTimerHighValue) {
	pNamedPipeStruct thisPipe = (pNamedPipeStruct)lpArg;

	_tprintf(TEXT("O tempo que definiu para esperar acabou! Tenha um bom dia!\n"));

	CloseHandle(thisPipe->hPipe);
	exit(0);
}

// thread que vai ficar à espera que um evento seja assinalado caso o controlador feche
DWORD WINAPI stopEventThread(LPVOID param){
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
		// espera 2000 milisegundos ,  se o evento nao for assinalado, faz continue e volta a esperar
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

// USO: app.exe <nome> <aeroporto origem> <aeroporto destino> <tempo espera (opcional)>
BOOL getInitialValuesFromArgument(pPassenger thisPassenger, int argc, TCHAR* argv[]) {
	if (argc != 4 && argc != 5) {
		_ftprintf(stderr, TEXT("[ERRO] Número inválido de argumentos!\n"));
		return FALSE;
	}
	
	thisPassenger->id = 0;
	thisPassenger->airplaneId = 0;

	// recebe os dados vindos do passageiro
	_tcscpy_s(thisPassenger->name, _countof(thisPassenger->name) - 1, argv[1]);
	_tcscpy_s(thisPassenger->srcAirport, _countof(thisPassenger->srcAirport) - 1, argv[2]);
	_tcscpy_s(thisPassenger->destAirport, _countof(thisPassenger->destAirport) - 1, argv[3]);

	thisPassenger->stopped = TRUE;

	// verifica o nr de argumentos, se for 4 mete o waitBoard a 0
	if (argc == 4) {
		thisPassenger->waitToBoard = 0;
		return TRUE;
	}
	// verifica se o tempo de espera é um inteiro
	if (!isStringANumber(argv[4])) {
		_ftprintf(stderr, TEXT("[ERRO] Tempo de espera deve ser um número inteiro!\n"));
		return FALSE;
	}
	// caso contrario o passageiro introduziu um tempo de espera
	thisPassenger->waitToBoard = (unsigned int) _tstoi(argv[4]);

	return TRUE;
}

int _tmain(int argc, TCHAR* argv[]) {
	passenger thisPassenger;
	DWORD nBytes;
	BOOL ret1, ret2, stop = FALSE;
	TCHAR strRead[STR_SIZE] = TEXT("");
	CRITICAL_SECTION criticalSectionBool;

	HANDLE hStopEventThread;
	stopEventThreadStruct mainStopEvent;

	HANDLE hWaitableTimer = NULL;
	__int64 qwDueTime;
	LARGE_INTEGER liTimeToWait;

	namedPipeStruct thisPipe;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionBool,
		500
	);

	if (!getInitialValuesFromArgument(&thisPassenger, argc, argv)) {
		_ftprintf(stderr, TEXT("[ERRO] Argumentos inválidos! Uso: app.exe <nome> <aeroporto origem> <aeroporto destino> <tempo espera (opcional)>\n"));
		return -1;
	}

	if (_tcsicmp(thisPassenger.destAirport, thisPassenger.srcAirport) == 0) {
		_tprintf(TEXT("Já se encontra no sítio onde pretende estar. Tenha um ótimo dia!\n"));
		return 0;
	}
	// tenta ligar-se a uma das instâncias do named pipe
	if (!WaitNamedPipe(PASSENGERS_PIPE_NAME, NMPWAIT_WAIT_FOREVER)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível ligar ao pipe! Será que o Control está aberto?\n"));
		return -1;
	}
	// se o passageiro tiver introduzido algum tempo de espera, criamos um waitabletimer
	if (thisPassenger.waitToBoard != 0) {
		hWaitableTimer = CreateWaitableTimer(
			NULL,
			TRUE,
			NULL
		);

		if (hWaitableTimer == NULL) {
			_ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o waitable timer!\n"));
			return -1;
		}
	}

	//ligamo-nos ao named pipe que ja existe nesta altura
	//1º nome do named pipe, 2ºpermissoes (têm de ser iguais ao CreateNamedPipe do servidor), 3ºshared mode 0 aqui,
	//4º security atributes, 5ºflags de criação OPEN_EXISTING, 6º o default é FILE_ATTRIBUTE_NORMAL e o 7º é o template é NULL
	thisPipe.hPipe = CreateFile(
		PASSENGERS_PIPE_NAME, 
		GENERIC_READ | GENERIC_WRITE, 
		0, 
		NULL, 
		OPEN_EXISTING, 
		FILE_FLAG_OVERLAPPED,
		NULL
	);

	if (thisPipe.hPipe == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível ligar ao pipe! Será que o Control está aberto?\n"), GetLastError());
		return -1;
	}
	// limpamos a struct overlap
	ZeroMemory(
		&thisPipe.overlap,
		sizeof(thisPipe.overlap)
	);
	// criamos um evento para associar à struct overlap
	thisPipe.overlap.hEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		NULL
	);

	if (thisPipe.overlap.hEvent == NULL) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível criar o evento overlap!\n"), GetLastError());
		
		CloseHandle(thisPipe.hPipe);
		return -1;
	}

	mainStopEvent.stop = &stop;
	mainStopEvent.criticalSectionBool = &criticalSectionBool;

	// Thread para ficar à escuta de um evento caso o controlador feche
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

		CloseHandle(thisPipe.hPipe);
		return -1;
	}
	// escreve inicialmente a sua struct no named pipe
	ret1 = WriteFile(
		thisPipe.hPipe,
		&thisPassenger,
		sizeof(thisPassenger),
		NULL,
		&thisPipe.overlap
	);
	// vê o resultado dessa escrita
	ret2 = GetOverlappedResultEx(
		thisPipe.hPipe,
		&thisPipe.overlap,
		&nBytes,
		INFINITE,
		TRUE
	);

	// o ret1 é difícil de usar com sucesso pois devido ao uso de overlapped o resultado é inconsistente
	if (!ret2 || !nBytes) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível escrever no pipe!\n"), GetLastError());

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		CloseHandle(thisPipe.hPipe);
		return -1;
	}
	// lê a resposta do controlador que escreveu no named pipe
	ret1 = ReadFile(
		thisPipe.hPipe,
		strRead,
		_countof(strRead) * sizeof(TCHAR),
		NULL,
		&thisPipe.overlap
	);
	// vê o resultado dessa leitura
	ret2 = GetOverlappedResultEx(
		thisPipe.hPipe,
		&thisPipe.overlap,
		&nBytes,
		INFINITE,
		TRUE
	);

	// o ret1 é difícil de usar com sucesso pois devido ao uso de overlapped o resultado é inconsistente
	if (!ret2 || !nBytes) {
		_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível ler do pipe! -%i-%i-%ld-\n"), GetLastError(), ret1, ret2, nBytes);

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		CloseHandle(thisPipe.hPipe);
		return -1;
	}

	if (!isStringANumber(strRead)) {
		_ftprintf(stderr, TEXT("[ERRO] Mensagem recebida inválida!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		CloseHandle(thisPipe.hPipe);
		return -1;
	}

	thisPassenger.id = (DWORD) _tstoi(strRead);
	// se o id for 0 significa que o passageiro introduziu algum dado errado
	if (thisPassenger.id == 0) {
		_ftprintf(stderr, TEXT("[ERRO] Registo foi invalidado!\n"));

		EnterCriticalSection(&criticalSectionBool);
		stop = TRUE;
		LeaveCriticalSection(&criticalSectionBool);

		WaitForSingleObject(hStopEventThread, INFINITE);

		CloseHandle(thisPipe.hPipe);
		return -1;
	}

	if (thisPassenger.waitToBoard != 0) {
		liTimeToWait.QuadPart = -100000000LL;

		// faz set ao waitable timer neste momento
		ret1 = SetWaitableTimer(
			hWaitableTimer,
			&liTimeToWait,
			0,
			TimerAPCProc,
			&thisPipe,
			TRUE
		);

		if (!ret1) {
			_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível realizar SetWaitableTimer!\n"), GetLastError());

			EnterCriticalSection(&criticalSectionBool);
			stop = TRUE;
			LeaveCriticalSection(&criticalSectionBool);

			WaitForSingleObject(hStopEventThread, INFINITE);

			CloseHandle(thisPipe.hPipe);
		}
	}

	// enquanto nao terminar a viagem, vai lendo do named pipe e vendo o resultado
	EnterCriticalSection(&criticalSectionBool);
	while (!stop) {
		LeaveCriticalSection(&criticalSectionBool);

		resetString(strRead, (unsigned int) _countof(strRead));

		ret1 = ReadFile(
			thisPipe.hPipe,
			strRead,
			_countof(strRead) * sizeof(TCHAR),
			NULL,
			&thisPipe.overlap
		);

		ret2 = GetOverlappedResultEx(
			thisPipe.hPipe,
			&thisPipe.overlap,
			&nBytes,
			INFINITE,
			TRUE
		);

		// o ret1 é difícil de usar com sucesso pois devido ao uso de overlapped o resultado é inconsistente

		if (!ret2 || !nBytes) {
			_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível ler do pipe!\n"), GetLastError());

			EnterCriticalSection(&criticalSectionBool);
			stop = TRUE;
			LeaveCriticalSection(&criticalSectionBool);

			WaitForSingleObject(hStopEventThread, INFINITE);

			CloseHandle(thisPipe.hPipe);
			return -1;
		}

		if (hWaitableTimer != NULL) {
			if (!CancelWaitableTimer(hWaitableTimer)) {
				_ftprintf(stderr, TEXT("[ERRO-%i] Não foi possível cancelar o WaitableTimer!\n"), GetLastError());

				EnterCriticalSection(&criticalSectionBool);
				stop = TRUE;
				LeaveCriticalSection(&criticalSectionBool);

				WaitForSingleObject(hStopEventThread, INFINITE);

				CloseHandle(thisPipe.hPipe);
				return -1;
			}

			hWaitableTimer = NULL;
		}
		// se receber um # significa que vem um comando especifico do controlador
		if (strRead[0] == TEXT('#')) {
			if (_tcsicmp(strRead, PIPE_COMMAND_FLIGHT_ENDED) == 0) {
				EnterCriticalSection(&criticalSectionBool);
				break;
			}

			if (_tcsicmp(strRead, PIPE_COMMAND_AIRPLANE_CLOSED) == 0) {
				_tprintf(TEXT("O avião despitou-se e o senhor acabou por morrer. Descanse em paz.\n"));

				EnterCriticalSection(&criticalSectionBool);
				stop = TRUE;
				LeaveCriticalSection(&criticalSectionBool);

				WaitForSingleObject(hStopEventThread, INFINITE);

				CloseHandle(thisPipe.hPipe);
				return -1;
			}

			_tprintf(TEXT("[AVISO] Mensagem recebida é inválida!\n"));

			EnterCriticalSection(&criticalSectionBool);
			continue;
		}
		// mostra as coordenadas da viagem
		_tprintf(TEXT("%s\n"), strRead);

		EnterCriticalSection(&criticalSectionBool);
	}
	LeaveCriticalSection(&criticalSectionBool);

	_tprintf(TEXT("\nChegou ao seu destino!\n"));

	EnterCriticalSection(&criticalSectionBool);
	stop = TRUE;
	LeaveCriticalSection(&criticalSectionBool);

	WaitForSingleObject(hStopEventThread, INFINITE);

	CloseHandle(thisPipe.hPipe);
	return -1;
}