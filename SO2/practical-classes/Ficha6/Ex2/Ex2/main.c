#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>

#include <stdio.h>
#include <stdbool.h>

#define MSG_SIZE 100
#define MAX_USERS 3

#define FILE_MAPPING_NAME TEXT("LOCAL\MSG_FILE_MAPPING_SO2")
#define MUTEX_NAME TEXT("LOCAL\MSG_MUTEX_SO2")
#define EVENT_NAME TEXT("LOCAL\MSG_EVENT_SO2")
#define SEMAPHORE_NAME TEXT("LOCAL\MSG_SEMAPHORE_SO2")

typedef struct tData tData, *tDataPtr;

struct tData {
	HANDLE hMutex, hEvent;
	bool exit, mine;
	TCHAR* msgBuff;
};

DWORD WINAPI sendInput(LPVOID lpParam) {
	tDataPtr p = (tDataPtr) lpParam;

	TCHAR msg[MSG_SIZE];
	_tcscpy_s(msg, MSG_SIZE, TEXT("\0"));

	while (_tcsicmp(msg, TEXT("fim")) != 0 && !p->exit) {
		_getts_s(msg, MSG_SIZE);

		WaitForSingleObject (
			p->hMutex,
			INFINITE
		);

		CopyMemory (
			p->msgBuff,
			msg,
			MSG_SIZE,
		);

		p->mine = TRUE;
		SetEvent(p->hEvent);
		Sleep(500);
		ResetEvent(p->hEvent);
		p->mine = FALSE;

		ReleaseMutex(p->hMutex);
	}

	p->exit = TRUE;

	return 0;
}

DWORD WINAPI printMessage(LPVOID lpParam) {
	tDataPtr p = (tDataPtr)lpParam;

	while (!p->exit) {
		WaitForSingleObject (
			p->hEvent,
			INFINITE
		);

		if (!p->mine) {
			_tprintf(TEXT("MSG > %s\n"), p->msgBuff);
		}
		
		if (_tcsicmp(p->msgBuff, TEXT("fugir")) == 0) {
			p->exit = true;
		}

		Sleep(1000);
	}

	return 0;
}

int _tmain(int argc, TCHAR* argv[]) {
	tData threadData;
	HANDLE hFileMapping, hMutex, hEvent, hSemaphore, hThreads[2];
	TCHAR* msgBuff;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	setvbuf(stdout, NULL, _IONBF, 0);
	setvbuf(stderr, NULL, _IONBF, 0);

	hSemaphore = OpenSemaphore (
		SEMAPHORE_ALL_ACCESS,
		TRUE,
		SEMAPHORE_NAME
	);

	if (hSemaphore == NULL) {
		hSemaphore = CreateSemaphore (
			NULL,
			MAX_USERS - 1,
			MAX_USERS,
			SEMAPHORE_NAME
		);

		if (hSemaphore == NULL) {
			_ftprintf(stderr, TEXT("Erro na criação do semáforo!\n"));
			return 0;
		}

		_tprintf(TEXT("Chat criado com sucesso!\n\n\n"));
	} else {
		_tprintf(TEXT("Estou em fila de espera...\n"));

		WaitForSingleObject (
			hSemaphore,
			INFINITE
		);

		_tprintf(TEXT("Entrei com sucesso!\n\n\n"));
	}

	hFileMapping = OpenFileMapping (
		FILE_MAP_READ | FILE_MAP_WRITE,
		0,
		FILE_MAPPING_NAME
	);
	
	hMutex = OpenMutex (
		MUTEX_ALL_ACCESS,
		FALSE,
		MUTEX_NAME
	);

	hEvent = OpenEvent (
		EVENT_ALL_ACCESS,
		FALSE,
		EVENT_NAME
	);

	if (hFileMapping == NULL || hMutex == NULL) {
		hFileMapping = CreateFileMapping (
			INVALID_HANDLE_VALUE,
			NULL,
			PAGE_READWRITE,
			0,
			MSG_SIZE * sizeof(TCHAR),
			FILE_MAPPING_NAME
		);

		if (hFileMapping == NULL) {
			_ftprintf(stderr, TEXT("Erro na criação do mapping!\n"));
			return 0;
		}

		hMutex = CreateMutex (
			NULL,
			FALSE,
			MUTEX_NAME
		);

		if (hMutex == NULL) {
			_ftprintf(stderr, TEXT("Erro na criação do mutex!\n"));
			return 0;
		}

		hEvent = CreateEvent (
			NULL,
			TRUE,
			FALSE,
			EVENT_NAME
		);

		if (hEvent == NULL) {
			_ftprintf(stderr, TEXT("Erro na criação do evento!\n"));
			return 0;
		}
	}

	msgBuff = MapViewOfFile (
		hFileMapping,
		FILE_MAP_READ | FILE_MAP_WRITE,
		0,
		0,
		0
	);

	if (msgBuff == NULL) {
		_ftprintf(stderr, TEXT("Erro na abertura do mapping!\n"));
		return 0;
	}

	threadData.hMutex = hMutex;
	threadData.hEvent = hEvent;
	threadData.exit = FALSE;
	threadData.mine = FALSE;
	threadData.msgBuff = msgBuff;

	hThreads[0] = CreateThread (
		NULL,
		0,
		sendInput,
		&threadData,
		0,
		NULL
	);

	hThreads[1] = CreateThread(
		NULL,
		0,
		printMessage,
		&threadData,
		0,
		NULL
	);

	WaitForMultipleObjects (
		2,
		hThreads,
		TRUE,
		INFINITE
	);

	_tprintf(TEXT("\n\nAplicação fechada com sucesso...\n"));

	ReleaseSemaphore (
		hSemaphore,
		1,
		NULL
	);

	CloseHandle(hFileMapping);
	CloseHandle(hMutex);
	CloseHandle(hEvent);
	CloseHandle(hSemaphore);
	CloseHandle(hThreads[0]);
	CloseHandle(hThreads[1]);

	return 0;
}