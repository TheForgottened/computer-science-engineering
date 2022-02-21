#include <windows.h>
#include <winbase.h>
#include <winerror.h>
#include <winreg.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>
#include <time.h>
#define TAM 200

typedef struct threadResult threadResult, *pThreadResult;

struct threadResult {
	int min;
	int max;
	int sum;
	int total;
};

DWORD WINAPI sumEvenNumbers(LPVOID lpParam) {
	pThreadResult p = (pThreadResult)lpParam;
	int i;

	_tprintf_s(TEXT("* Boas, sou a thread %i e vou somar números pares entre %i - %i *\n"), GetCurrentThreadId(), p->min, p->max);

	p->sum = 0;
	p->total = 0;

	for (i = p->min; i < p->max; i++) {
		/*
		if ((p->total % 200) == 0) {
			Sleep(1000);
		}
		*/

		if ((i % 2) == 0) {
			p->sum += i;
			(p->total)++;
		}
	}

	return 0;
}

DWORD WINAPI sumPrimeNumbers(LPVOID lpParam) {
	pThreadResult p = (pThreadResult)lpParam;
	int i, j;

	_tprintf_s(TEXT("* Boas, sou a thread %i e vou somar números primos entre %i - %i *\n"), GetCurrentThreadId(), p->min, p->max);

	p->sum = 0;
	p->total = 0;

	for (i = p->min; i < p->max; i++) {
		/*
		if ((p->total % 15) == 0) {
			Sleep(1000);
		}
		*/

		for (j = 2; j <= i / 2; j++) {
			if ((i % j) == 0) {
				break;
			}
		}

		if (j <= i / 2) {
			p->sum += i;
			(p->total)++;
		}
	}

	return 0;
}

int _tmain(int argc, TCHAR* argv[]) {
	HANDLE hThreadArray[4];
	threadResult thResEvenArray[2], thResPrimeArray[2];
	int i, res;

	int minEven = 500, maxEven = 3000, sumEven, totalEven;
	int minPrime = 700, maxPrime = 7000, sumPrime, totalPrime;

	// Even numbers
	thResEvenArray[0].min = minEven;
	thResEvenArray[0].max = (maxEven - minEven) / 2;
	hThreadArray[0] = CreateThread (
		NULL,
		0,
		sumEvenNumbers,
		&(thResEvenArray[0]),
		0,
		NULL
	);

	thResEvenArray[1].min = ((maxEven - minEven) / 2) + 1;
	thResEvenArray[1].max = maxEven;
	hThreadArray[1] = CreateThread (
		NULL,
		0,
		sumEvenNumbers,
		&(thResEvenArray[1]),
		0,
		NULL
	);

	// Prime Numbers
	thResPrimeArray[0].min = minPrime;
	thResPrimeArray[0].max = (maxPrime - minPrime) / 2;
	hThreadArray[2] = CreateThread (
		NULL,
		0,
		sumEvenNumbers,
		&(thResPrimeArray[0]),
		0,
		NULL
	);

	thResPrimeArray[1].min = ((maxPrime - minPrime) / 2) + 1;
	thResPrimeArray[1].max = maxPrime;
	hThreadArray[3] = CreateThread (
		NULL,
		0,
		sumEvenNumbers,
		&(thResPrimeArray[1]),
		0,
		NULL
	);

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	_tprintf_s(TEXT("Olá!\n\n"));

	/*
	// c.
	res = WaitForMultipleObjects(2, hThreadArray, FALSE, INFINITE) - WAIT_OBJECT_0;

	switch (res) {
		case 0:
			_tprintf_s(TEXT("Somatório de números pares: %i (%i números).\n"), thResArray[0].sum, thResArray[0].total);
			WaitForSingleObject(hThreadArray[1], INFINITE);
			_tprintf_s(TEXT("Somatório de números primos: %i (%i números).\n"), thResArray[1].sum, thResArray[1].total);
			break;

		case 1:
			_tprintf_s(TEXT("Somatório de números primos: %i (%i números).\n"), thResArray[1].sum, thResArray[1].total);
			WaitForSingleObject(hThreadArray[0], INFINITE);
			_tprintf_s(TEXT("Somatório de números pares: %i (%i números).\n"), thResArray[0].sum, thResArray[0].total);
			break;

		default:
			_tprintf_s(TEXT("ERRO: situação inesperada\n"));
			return 0;
			break;
	}
	*/

	WaitForMultipleObjects(4, hThreadArray, TRUE, INFINITE);

	sumEven = thResEvenArray[0].sum + thResEvenArray[1].sum;
	totalEven = thResEvenArray[0].total + thResEvenArray[1].total;

	sumPrime = thResPrimeArray[0].sum + thResPrimeArray[1].sum;
	totalPrime = thResPrimeArray[0].total + thResPrimeArray[1].total;

	_tprintf_s(TEXT("Somatório de números pares: %i (%i números).\n"), sumEven, totalEven);
	_tprintf_s(TEXT("Somatório de números primos: %i (%i números).\n"), sumPrime, totalPrime);

	CloseHandle(hThreadArray[0]);
	CloseHandle(hThreadArray[1]);
	CloseHandle(hThreadArray[2]);
	CloseHandle(hThreadArray[3]);

	return 0;
}