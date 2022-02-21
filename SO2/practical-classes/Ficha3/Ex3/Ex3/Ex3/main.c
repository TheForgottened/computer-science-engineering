#include <windows.h>
#include <winbase.h>
#include <winerror.h>
#include <winreg.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>

#include "SO2_F3_DLL.h"

// Lib Link: SO2_F3_DLL.h
extern __declspec(dllimport) double factor;
__declspec(dllimport) double applyFactor(double v);

#define TAM 200

int askUserForInt() {
	TCHAR temp[TAM] = TEXT("");

	_tprintf_s(TEXT("Insira um valor inteiro: "));
	_tscanf_s(TEXT("%s"), temp, TAM);

	return _tstoi(temp);
}

int _tmain(int argc, TCHAR* argv[]) {
	int x, y;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	_tprintf_s(TEXT("Valor inicial da variável da DLL: %f\n"), factor);

	do {
		x = 0;
		y = 0;

		_tprintf_s(TEXT("Variável x.\n"));
		x = askUserForInt();

		if (x == -1) {
			_tprintf_s(TEXT("Programa fechado com sucesso!\n"));
			break;
		}

		factor = (double)x;
		_tprintf_s(TEXT("Novo valor da variável da DLL: %f\n"), factor);

		_puttchar(TEXT('\n'));
		_tprintf_s(TEXT("Variável y.\n"));
		y = askUserForInt();

		_tprintf_s(TEXT("Resultado da função: %f\n"), applyFactor((double)y));

		_tprintf_s(TEXT("\n**********\n\n"));
	} while (1);

	return 0;
}