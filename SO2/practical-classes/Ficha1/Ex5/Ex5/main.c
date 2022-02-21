#include <windows.h>
#include <tchar.h>
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#define MAX 256

int _tmain(int argc, LPTSTR argv[]) {
	TCHAR executavel[MAX], resp;
	PROCESS_INFORMATION pi;
	STARTUPINFO si;

//UNICODE: Por defeito, a consola Windows não processa caracteres wide. 
//A maneira mais fácil para ter esta funcionalidade é chamar _setmode:
#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif

	// a)
	ZeroMemory(&si, sizeof(STARTUPINFO)); //É equivalente a preencher com 0
	si.cb = sizeof(STARTUPINFO);
	GetModuleFileName(NULL, executavel, MAX);
	_tprintf(TEXT("Nome do processo: %s\n"), executavel);

	// b)
	_tprintf(TEXT("Lançar outra aplicação (S/N)? "));
	_tscanf_s(TEXT("%c"), &resp, 1);

	if (resp == 'S' || resp == 's') {
		ZeroMemory(&si, sizeof(STARTUPINFO));//É equivalente a preencher com 0s
		si.cb = sizeof(STARTUPINFO);

		//GetModuleFileName(NULL, executavel, MAX);
		_tprintf(TEXT("Indique o nome da aplicação a lançar : \n"));
		_tscanf_s(TEXT("%s"), &executavel, MAX);

		_tprintf(TEXT("Processo a ser lançado: %s\n"), executavel);

		if (CreateProcess(NULL, executavel, NULL, NULL, 0, 0, NULL, NULL, &si, &pi))
			_tprintf(TEXT("Sucesso\n"));
		else
			_tprintf(TEXT("Erro\n"));
	}

	return 0;
}
