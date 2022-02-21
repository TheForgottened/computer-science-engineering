#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>

#include <stdio.h>

#define BUFFER_SIZE 10

#define FILE_MAPPING_NAME TEXT("LOCAL\CIRCULAR_FILE_MAPPING_SO2")
#define SEMAPHORE_WRITING_NAME TEXT("LOCAL\CIRCULAR_SEMAPHORE_WRITING_SO2")
#define SEMAPHORE_READING_NAME TEXT("LOCAL\CIRCULAR_SEMAPHORE_READING_SO2")
#define MUTEX_NAME TEXT("LOCAL\CIRCULAR_MUTEX_SO2")

typedef struct BUFFERCELL_STRUCT bufferCell;
typedef struct CIRCULARBUFFER_STRUCT circularBuffer;
typedef struct THREADDATA_STRUCT threadData, *pThreadData;

struct BUFFERCELL_STRUCT {
	int id;
	int value;
};

struct CIRCULARBUFFER_STRUCT {
    int nProducers;
    int nConsumers;
    int writeIndex; // próxima posição de escrita
    int readIndex; // próxima posição de leitura

    bufferCell buffer[BUFFER_SIZE]; // buffer circular em si (array de estruturas)
};

struct THREADDATA_STRUCT {
    circularBuffer* sharedMemory; // ponteiro para a memória partilhada
    HANDLE hWritingSemaphore; // handle para o semáforo que controla as escritas (controla quantas posições estão vazias)
    HANDLE hReadingSemaphore; // handle para o semáforo que controla as leituras (controla quantas posições estão preenchidas)
    HANDLE hMutex;

    BOOL exit; // 1 para sair, 0 em caso contrário
    int id;
};

int randomInt(int min, int max) {
    return rand() % (max - min + 1) + min;
}

DWORD WINAPI consumerThread(LPVOID param) {
    pThreadData tData = (pThreadData) param;
    bufferCell cell;

    int counter = 0, sum = 0;

    while (!tData->exit) {
        // Aqui entramos na lógica da aula teórica
        // Esperamos por uma posição para ler
        WaitForSingleObject(tData->hReadingSemaphore, INFINITE);

        // Esperamos que o mutex esteja livre
        WaitForSingleObject(tData->hMutex, INFINITE);

        // Vamos copiar da próxima posição de leitura do buffer circular para a nossa variável cell
        CopyMemory (&cell, 
            &tData->sharedMemory->buffer[tData->sharedMemory->readIndex], 
            sizeof(bufferCell)
        );
        // Incrementamos a posição de leitura para o próximo consumidor ler na posição seguinte
        tData->sharedMemory->readIndex++; 

        // Se após o incremento a posição de leitura chegar ao fim, tenho de voltar ao início
        if (tData->sharedMemory->readIndex == BUFFER_SIZE) tData->sharedMemory->readIndex = 0;

        // Libertamos o mutex
        ReleaseMutex(tData->hMutex);

        // Libertamos o semáforo. Temos de libertar uma posição de escrita
        ReleaseSemaphore(tData->hWritingSemaphore, 1, NULL);

        counter++;
        sum += cell.value;
        _tprintf(TEXT("C%i consumiu %i.\n"), tData->id, cell.value);
    }

    _tprintf(TEXT("C%i consumiu %i items, somando um valor acumulado de %i.\n"), tData->id, counter, sum);

    return 0;
}

DWORD WINAPI producerThread(LPVOID param) {
    pThreadData tData = (pThreadData)param;
    bufferCell cell;

    int counter = 0;

    while (!tData->exit) {
        cell.id = tData->id;
        cell.value = randomInt(10, 99);

        // Aqui entramos na lógica da aula teorica
        // Esperamos por uma posição para escrever
        WaitForSingleObject(tData->hWritingSemaphore, INFINITE);

        // Esperamos que o mutex esteja livre
        WaitForSingleObject(tData->hMutex, INFINITE);

        // Vamos copiar a variável cell para a memória partilhada (para a posição de escrita)
        CopyMemory (
            &tData->sharedMemory->buffer[tData->sharedMemory->writeIndex],
            &cell, 
            sizeof(bufferCell)
        );
        // Incrementamos a posição de escrita para o próximo produtor escrever na posição seguinte
        tData->sharedMemory->writeIndex++; 

        // Se após o incremento a posição de escrita chegar ao fim, tenho de voltar ao início
        if (tData->sharedMemory->writeIndex == BUFFER_SIZE) tData->sharedMemory->writeIndex = 0;

        // Libertamos o mutex
        ReleaseMutex(tData->hMutex);

        // Libertamos o semáforo. Temos de libertar uma posição de leitura
        ReleaseSemaphore(tData->hWritingSemaphore, 1, NULL);

        counter++;
        _tprintf(TEXT("P%i produziu %i.\n"), tData->id, cell.value);
        Sleep(randomInt(2, 4) * 1000);
    }

    _tprintf(TEXT("P%i produziu %i items.\n"), tData->id, counter);

    return 0;
}



int _tmain(int argc, TCHAR* argv[]) {
    HANDLE hFileMap; // Handle para o file map
    HANDLE hThread = NULL;
    threadData tData;
    BOOL firstProcess = FALSE;
    int option;

#ifdef UNICODE
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    setvbuf(stdout, NULL, _IONBF, 0);
    setvbuf(stderr, NULL, _IONBF, 0);

    srand((unsigned int)time(NULL));

    _tprintf(TEXT("1. Sou produtor;\n2. Sou consumidor;\n3. Sair.\nOpção pretendida: "));
    _tscanf_s(TEXT("%i"), &option);

    if (option != 1 && option != 2) return 0;

    //
    // Abrir semáforo que controla escrita
    //

    tData.hWritingSemaphore = CreateSemaphore (
        NULL,
        BUFFER_SIZE,
        BUFFER_SIZE,
        SEMAPHORE_WRITING_NAME
    );

    if (tData.hWritingSemaphore == NULL) {
        _ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o semáforo tipo 1!"));
        return -1;
    }

    //
    // Abrir semáforo que controla leitura
    //

    tData.hReadingSemaphore = CreateSemaphore (
        NULL,
        0,
        BUFFER_SIZE,
        SEMAPHORE_READING_NAME
    );

    if (tData.hReadingSemaphore == NULL) {
        _ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o semáforo tipo 2!"));
        return -1;
    }

    //
    // Abrir mutex
    //

    tData.hMutex = CreateMutex (
        NULL,
        FALSE,
        MUTEX_NAME
    );

    if (tData.hMutex == NULL) {
        _ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o mutex!"));
        return -1;
    }

    // O OpenFileMapping(...) vai abrir um filemapping com o nome que passamos no LPNAME
    // 
    // Se devolver um HANDLE já existe e não fazemos a inicialização
    // Se devolver NULL não existe e vamos fazer a inicialização

    hFileMap = OpenFileMapping (
        FILE_MAP_ALL_ACCESS, 
        FALSE, 
        FILE_MAPPING_NAME
    );

    if (hFileMap == NULL) {
        firstProcess = TRUE;

        // Criamos o bloco de memória partilhada
        hFileMap = CreateFileMapping (
            INVALID_HANDLE_VALUE,
            NULL,
            PAGE_READWRITE,
            0,
            sizeof(circularBuffer), // Tamanho da memória partilhada
            FILE_MAPPING_NAME // Nome do filemapping. Nome que vai ser usado para partilha entre processos
        ); 

        if (hFileMap == NULL) {
            _ftprintf(stderr, TEXT("[ERRO] Não foi possível criar o file map!"));
            return -1;
        }
    }

    // Mapeamos o bloco de memoria para o espaco de enderaçamento do nosso processo
    tData.sharedMemory = MapViewOfFile (
        hFileMap, 
        FILE_MAP_ALL_ACCESS, 
        0, 
        0, 
        0
    );

    if (tData.sharedMemory == NULL) {
        _ftprintf(stderr, TEXT("[ERRO] Não foi possível mapear o file map!"));
        return -1;
    }

    if (firstProcess == TRUE) {
        tData.sharedMemory->nConsumers = 0;
        tData.sharedMemory->nProducers = 0;
        tData.sharedMemory->writeIndex = 0;
        tData.sharedMemory->readIndex = 0;
    }

    tData.exit = FALSE;

    // Lançamos a thread
    switch (option) {
        case 1:
            // temos de usar o mutex para aumentar o nProducers para termos os ids corretos
            WaitForSingleObject(tData.hMutex, INFINITE);
            tData.sharedMemory->nProducers++;
            tData.id = tData.sharedMemory->nProducers;
            ReleaseMutex(tData.hMutex);

            hThread = CreateThread (
                NULL,
                0,
                producerThread,
                &tData,
                0,
                NULL
            );
            break;

        case 2:
            // Temos de usar o mutex para aumentar o nConsumers para termos os ids corretos
            WaitForSingleObject(tData.hMutex, INFINITE);
            tData.sharedMemory->nConsumers++;
            tData.id = tData.sharedMemory->nConsumers;
            ReleaseMutex(tData.hMutex);

            hThread = CreateThread (
                NULL,
                0,
                consumerThread,
                &tData,
                0,
                NULL
            );
            break;
    }

    if (hThread != NULL) {
        fflush(stdin);
        fflush(stdout);

        _tprintf(TEXT("Escreva qualquer coisa para sair...\n"));
        _tscanf_s(TEXT("%i"), &option);
        tData.exit = TRUE;

        // Esperar que a thread termine
        WaitForSingleObject(hThread, INFINITE);
    }

    UnmapViewOfFile(tData.sharedMemory);
    // CloseHandles... mas é feito automaticamente quando o processo termina

	return 0;
}