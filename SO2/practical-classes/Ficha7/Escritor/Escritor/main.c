#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>
#include <io.h>

#include <stdio.h>

#define CONNECTING_STATE 0 
#define READING_STATE 1 
#define WRITING_STATE 2 

#define BUFFER_SIZE 256
#define MAX_CLIENTS 2

#define PIPE_TIMEOUT 5000 //ms
#define PIPE_NAME TEXT("\\\\.\\pipe\\teste")

typedef struct INTERFACE_STRUCT interfaceStruct, *pInterfaceStruct;

typedef struct {
	OVERLAPPED oOverlap;
	HANDLE hPipeInst;
	TCHAR chRequest[BUFFER_SIZE];
	DWORD cbRead;
	TCHAR chReply[BUFFER_SIZE];
	DWORD cbToWrite;
	DWORD dwState;
	BOOL fPendingIO;
} PIPEINST, *LPPIPEINST;

struct INTERFACE_STRUCT {
	unsigned int* nrConnectedClients;
	LPPIPEINST pipesArray;

	LPCRITICAL_SECTION criticalSection;

	BOOL* mustExit;
};

BOOL ConnectToNewClient(HANDLE hPipe, LPOVERLAPPED lpo) {
	BOOL fConnected, fPendingIO = FALSE;

	// Start an overlapped connection for this pipe instance. 
	fConnected = ConnectNamedPipe(hPipe, lpo);

	// Overlapped ConnectNamedPipe should return zero. 
	if (fConnected) {
		printf("ConnectNamedPipe failed with %d.\n", GetLastError());
		return 0;
	}

	switch (GetLastError()) {
		// The overlapped connection in progress. 
		case ERROR_IO_PENDING:
			fPendingIO = TRUE;
			break;

			// Client is already connected, so signal an event. 

		case ERROR_PIPE_CONNECTED:
			if (SetEvent(lpo->hEvent))
				break;

			// If an error occurs during the connect operation... 
		default:
			printf("ConnectNamedPipe failed with %d.\n", GetLastError());
			return FALSE;
			break;
	}

	return fPendingIO;
}

VOID DisconnectAndReconnect(DWORD i, LPPIPEINST pipesArray) {
	// Disconnect the pipe instance. 

	if (!DisconnectNamedPipe(pipesArray[i].hPipeInst)) {
		printf("DisconnectNamedPipe failed with %d.\n", GetLastError());
	}

	// Call a subroutine to connect to the new client. 

	pipesArray[i].fPendingIO = ConnectToNewClient (
		pipesArray[i].hPipeInst,
		&pipesArray[i].oOverlap
	);

	pipesArray[i].dwState = pipesArray[i].fPendingIO ? CONNECTING_STATE :  READING_STATE;
}

DWORD WINAPI threadInterface(LPVOID param) {
	pInterfaceStruct mainInterface = (pInterfaceStruct) param;
	DWORD nBytesWritten;
	TCHAR buffer[BUFFER_SIZE];
	unsigned int i;

	_tprintf(TEXT("[ESCRITOR] Escreva uma frase seguida de enter para enviar aos leitores!\n"));

	EnterCriticalSection(mainInterface->criticalSection);
	while (!*mainInterface->mustExit) {
		LeaveCriticalSection(mainInterface->criticalSection);

		_fgetts(buffer, 256, stdin);
		buffer[_tcslen(buffer) - 1] = '\0';

		EnterCriticalSection(mainInterface->criticalSection);
		for (i = 0; i < *mainInterface->nrConnectedClients; i++) {
			if (!WriteFile(mainInterface->hPipes[i], buffer, _tcslen(buffer) * sizeof(TCHAR), &nBytesWritten, NULL)) {
				_tprintf(TEXT("[ERRO] Escrever no pipe! (WriteFile)\n"));
				*mainInterface->mustExit = TRUE;
				break;
			}

			_tprintf(TEXT("[ESCRITOR] Enviei %d bytes ao leitor... (WriteFile)\n"), nBytesWritten);
		}
		LeaveCriticalSection(mainInterface->criticalSection);

		if (_tcscmp(buffer, TEXT("fim")) == 0) *mainInterface->mustExit = TRUE;

		EnterCriticalSection(mainInterface->criticalSection);
	}
	LeaveCriticalSection(mainInterface->criticalSection);

	return 0;
}

int _tmain(int argc, LPTSTR argv[]) {
	unsigned int i;
	unsigned int nrConnectedClients = 0;

	CRITICAL_SECTION criticalSection;
	PIPEINST pipesArray[MAX_CLIENTS];
	HANDLE hThreadInterface, hEvents[MAX_CLIENTS];
	TCHAR buf[256];
	DWORD dwWait, cbRet, dwErr;
	BOOL mustExit = FALSE, fSuccess;

	interfaceStruct mainInterface;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	// Inicializa a secção crítica 
	InitializeCriticalSection(&criticalSection);

	// Inicializa a estrutura que vai ser passada à thread
	mainInterface.nrConnectedClients = &nrConnectedClients;
	mainInterface.pipesArray = pipesArray;
	mainInterface.criticalSection = &criticalSection;
	mainInterface.mustExit = &mustExit;

	// Inicializa a thread que vai servir de interface
	hThreadInterface = CreateThread(
		NULL,
		0,
		threadInterface,
		&mainInterface,
		0,
		NULL
	);

	if (hThreadInterface == NULL) {
		_ftprintf(stderr, TEXT("[ERRO] Impossível criar thread interface!\n"));
		return -1;
	}

	// Overlapped I/O
	for (i = 0; i < MAX_CLIENTS; i++) {
		hEvents[i] = CreateEvent (
			NULL,    // default security attribute 
			TRUE,    // manual-reset event 
			TRUE,    // initial state = signaled 
			NULL	 // unnamed event object 
		);   

		if (hEvents[i] == NULL) {
			_ftprintf(stderr, TEXT("CreateEvent failed with %d.\n"), GetLastError());
			mustExit = TRUE;

			WaitForSingleObject(hThreadInterface, INFINITE);
			return 0;
		}

		pipesArray[i].oOverlap.hEvent = hEvents[i];

		pipesArray[i].hPipeInst = CreateNamedPipe (
			PIPE_NAME,            
			PIPE_ACCESS_DUPLEX | FILE_FLAG_OVERLAPPED,    
			PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE | PIPE_WAIT,               
			MAX_CLIENTS,
			BUFFER_SIZE * sizeof(TCHAR),   // output buffer size 
			BUFFER_SIZE * sizeof(TCHAR),   // input buffer size 
			PIPE_TIMEOUT,            // client time-out 
			NULL
		);                  

		if (pipesArray[i].hPipeInst == INVALID_HANDLE_VALUE) {
			_ftprintf(stderr, TEXT("CreateNamedPipe failed with %d.\n"), GetLastError());
			mustExit = TRUE;

			WaitForSingleObject(hThreadInterface, INFINITE);
			return 0;
		}

		// Call the subroutine to connect to the new client

		pipesArray[i].fPendingIO = ConnectToNewClient (
			pipesArray[i].hPipeInst,
			&pipesArray[i].oOverlap
		);

		pipesArray[i].dwState = pipesArray[i].fPendingIO ? CONNECTING_STATE : READING_STATE;

		EnterCriticalSection(&criticalSection);
		nrConnectedClients++;
		LeaveCriticalSection(&criticalSection);
	}

	WaitForSingleObject(hThreadInterface, INFINITE);

	/*
	while (!mustExit) {
		dwWait = WaitForMultipleObjects (
			MAX_CLIENTS,    // number of event objects 
			hEvents,      // array of event objects 
			FALSE,        // does not wait for all 
			INFINITE
		);

		// dwWait shows which pipe completed the operation. 

		i = dwWait - WAIT_OBJECT_0;  // determines which pipe 
		if (i < 0 || i >(MAX_CLIENTS - 1)) {
			_ftprintf(stderr, TEXT("Index out of range.\n"));
			mustExit = TRUE;

			WaitForSingleObject(hThreadInterface, INFINITE);
			return 0;
		}

		if (pipesArray[i].fPendingIO) {
			fSuccess = GetOverlappedResult(
				pipesArray[i].hPipeInst, // handle to pipe 
				&pipesArray[i].oOverlap, // OVERLAPPED structure 
				&cbRet,            // bytes transferred 
				FALSE
			);

			switch (pipesArray[i].dwState) {
				// Pending connect operation 
				case CONNECTING_STATE:
					if (!fSuccess) {
						printf("Error %d.\n", GetLastError());
						return 0;
					}

					pipesArray[i].dwState = READING_STATE;
					break;

					// Pending read operation 
				case READING_STATE:
					if (!fSuccess || cbRet == 0) {
						DisconnectAndReconnect(i, pipesArray);
						continue;
					}

					pipesArray[i].cbRead = cbRet;
					pipesArray[i].dwState = WRITING_STATE;
					break;

					// Pending write operation 
				case WRITING_STATE:
					if (!fSuccess || cbRet != pipesArray[i].cbToWrite) {
						DisconnectAndReconnect(i, pipesArray);
						continue;
					}

					pipesArray[i].dwState = READING_STATE;
					break;

				default:
					_ftprintf(stderr, TEXT("Invalid pipe state.\n"));
					mustExit = TRUE;

					WaitForSingleObject(hThreadInterface, INFINITE);
					return 0;
					break;
			}
		}

		switch (pipesArray[i].dwState) {
				// READING_STATE: 
				// The pipe instance is connected to the client 
				// and is ready to read a request from the client. 

			case READING_STATE:
				fSuccess = ReadFile(
					pipesArray[i].hPipeInst,
					pipesArray[i].chRequest,
					BUFFER_SIZE * sizeof(TCHAR),
					&pipesArray[i].cbRead,
					&pipesArray[i].oOverlap);

				// The read operation completed successfully. 

				if (fSuccess && pipesArray[i].cbRead != 0) {
					pipesArray[i].fPendingIO = FALSE;
					pipesArray[i].dwState = WRITING_STATE;
					continue;
				}

				// The read operation is still pending. 

				dwErr = GetLastError();
				if (!fSuccess && (dwErr == ERROR_IO_PENDING)) {
					pipesArray[i].fPendingIO = TRUE;
					continue;
				}

				// An error occurred; disconnect from the client. 

				DisconnectAndReconnect(i, pipesArray);
				break;

				// WRITING_STATE: 
				// The request was successfully read from the client. 
				// Get the reply data and write it to the client. 

			case WRITING_STATE:
				GetAnswerToRequest(&pipesArray[i]);

				fSuccess = WriteFile(
					pipesArray[i].hPipeInst,
					pipesArray[i].chReply,
					pipesArray[i].cbToWrite,
					&cbRet,
					&pipesArray[i].oOverlap);

				// The write operation completed successfully. 

				if (fSuccess && cbRet == pipesArray[i].cbToWrite) {
					pipesArray[i].fPendingIO = FALSE;
					pipesArray[i].dwState = READING_STATE;
					continue;
				}

				// The write operation is still pending. 

				dwErr = GetLastError();
				if (!fSuccess && (dwErr == ERROR_IO_PENDING)) {
					pipesArray[i].fPendingIO = TRUE;
					continue;
				}

				// An error occurred; disconnect from the client. 

				DisconnectAndReconnect(i, pipesArray);
				break;

			default:
				_ftprintf(stderr, TEXT("Invalid pipe state.\n"));
				mustExit = TRUE;

				WaitForSingleObject(hThreadInterface, INFINITE);
				return 0;
				break;
		}
	}
	*/

	_tprintf(TEXT("[ESCRITOR] Fechado com sucesso!\n"));

	Sleep(2000);

	return 0;
}