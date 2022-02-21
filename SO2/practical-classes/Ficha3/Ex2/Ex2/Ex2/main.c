#include <windows.h>
#include <winbase.h>
#include <winerror.h>
#include <winreg.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>
#define TAM 200

int askUserForInt() {
	TCHAR temp[TAM] = TEXT("");

	_tprintf_s(TEXT("Insira um valor inteiro: "));
	_tscanf_s(TEXT("%s"), temp, TAM);

	return _tstoi(temp);
}

int _tmain(int argc, TCHAR* argv[]) {
	int x, y;
	HINSTANCE hLib = NULL;
	TCHAR pathDLL[TAM] = TEXT("SO2_F3_DLL.dll");
	// TCHAR pathDLL[TAM] = TEXT("D:\\Git\\so2-temp\\Ficha3\\Ex2\\Ex2\\Debug\\SO2_F3_DLL.dll");

	double (*ptrVar)(double) = NULL;
	double (*ptrFunc)(double) = NULL;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	hLib = LoadLibrary(pathDLL);

	if (hLib == NULL) {
		_ftprintf_s(stderr, TEXT("Erro a carregar a DLL!\n"));
		return 0;
	}

	ptrFunc = (double (*)(double)) GetProcAddress(hLib, "applyFactor");
	ptrVar = (double (*)(double)) GetProcAddress(hLib, "factor");

	if (ptrVar == NULL || ptrFunc == NULL) {
		_ftprintf_s(stderr, TEXT("Erro ao carregar variáveis e funções da DLL!\n"));
		return 0;
	}

	_tprintf_s(TEXT("Valor inicial da variável da DLL: %f\n"), *((double*)ptrVar));

	do {
		x = 0;
		y = 0;

		_tprintf_s(TEXT("Variável x.\n"));
		x = askUserForInt();

		if (x == -1) {
			_tprintf_s(TEXT("Programa fechado com sucesso!\n"));
			break;
		}

		*((double*)ptrVar) = (double)x;

		_tprintf_s(TEXT("Novo valor da variável da DLL: %f\n"), *((double*)ptrVar));

		_tprintf_s(TEXT("Variável y.\n"));
		y = askUserForInt();

		_tprintf_s(TEXT("Resultado da função: %f\n"), ptrFunc(y));

		_puttchar(TEXT('\n'));
	} while (1);

	FreeLibrary(hLib);

	return 0;
}