// https://docs.microsoft.com/pt-pt/windows/win32/sync/using-critical-section-objects

#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>

#include <math.h>
#include <io.h>
#include <stdio.h>

// funcionalidade relacionada com temporização
static double PerfCounterFreq; // n ticks por seg.
void initClock() {
	LARGE_INTEGER aux;
	if (!QueryPerformanceFrequency(&aux))
		_tprintf(TEXT("\nSorry - No can do em QueryPerfFreq\n"));
	PerfCounterFreq = (double)(aux.QuadPart); // / 1000.0;
	_tprintf(TEXT("\nTicks por sec.%f\n"), PerfCounterFreq);
}
__int64 startClock() {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	return aux.QuadPart;
}
double stopClock(__int64 from) {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	return (double)(aux.QuadPart - from) / PerfCounterFreq;
}
// estrutura de dados para controlar as threads

typedef struct TDados TDados, * pTDados;

struct TDados {
	CRITICAL_SECTION* criticalSection;

	int lower, upper;
	int* counter;
};

DWORD WINAPI sumMultiplesThree(LPVOID lpParam) {
	pTDados p = (pTDados)lpParam;
	int i;

	for (i = p->lower; i <= p->upper; i++) {
		/*
		if ((p->total % 200) == 0) {
			Sleep(1000);
		}
		*/

		if ((i % 3) == 0) {
			EnterCriticalSection(p->criticalSection);

			(*p->counter)++;

			LeaveCriticalSection(p->criticalSection);
		}
	}

	return 0;
}

#define MAX_THREADS 20

int _tmain(int argc, TCHAR* argv[]) {
	// matriz de handles das threads
	HANDLE hThreads[MAX_THREADS];
	// secção crítica
	CRITICAL_SECTION criticalSection;
	// Matriz de dados para as threads;
	TDados tdados[MAX_THREADS];
	// número efectivo de threads
	int numthreads, i;
	// limite superior
	unsigned int limsup;
	// variáveis para cronómetro
	__int64 clockticks;
	double duracao;
	unsigned int range;
	unsigned int inter;
	unsigned int commonCounter = 0;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	initClock();
	_tprintf(TEXT("\nLimite sup. -> "));
	_tscanf_s(TEXT("%u"), &limsup);
	_tprintf(TEXT("\nNum threads -> "));
	_tscanf_s(TEXT("%u"), &numthreads);

	if (numthreads > MAX_THREADS) {
		numthreads = MAX_THREADS;
	}

	InitializeCriticalSection(&criticalSection);

	for (i = 0; i < numthreads; i++) {
		tdados[i].criticalSection = &criticalSection;
		tdados[i].lower = 1 + ((limsup / numthreads) * i);
		tdados[i].upper = (limsup / numthreads) * (i + 1);
		tdados[i].counter = &commonCounter;

		// _tprintf(TEXT("\nLower=%i;Upper=%i"), 1 + ((limsup / numthreads) * i), (limsup / numthreads) * (i + 1));

		hThreads[i] = CreateThread(
			NULL,
			0,
			sumMultiplesThree,
			&tdados[i],
			CREATE_SUSPENDED,
			NULL
		);
	}

	for (i = 0; i < numthreads; i++) {
		ResumeThread(hThreads[i]);
	}

	clockticks = startClock();

	WaitForMultipleObjects(numthreads, hThreads, TRUE, INFINITE);

	// FAZER aguarda / controla as threads
	// manda as threads parar
	duracao = stopClock(clockticks);

	_tprintf(TEXT("\nSegundos=%f\ncommonCounter=%i"), duracao, commonCounter);
	// FAZER apresenta resultados
	// Cód. ref. para aguardar por uma tecla – caso faça falta
	// _tprintf(TEXT("\nCarregue numa tecla"));
	// _gettch();

	for (i = 0; i < numthreads; i++) {
		CloseHandle(hThreads[i]);
	}

	DeleteCriticalSection(&criticalSection);

	return 0;
}