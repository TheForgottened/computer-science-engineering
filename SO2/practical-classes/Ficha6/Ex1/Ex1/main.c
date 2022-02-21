#include <windows.h>
#include <winbase.h>
#include <tchar.h>
#include <fcntl.h>

#include <stdio.h>

int _tmain(int argc, TCHAR* argv[]) {
	SYSTEM_INFO sysInfo;
	HANDLE hFile, hFileMapping;

	TCHAR *fName = TEXT("letters.txt");
	char *fBuff = NULL, temp;

	unsigned int i;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	_tprintf(TEXT("Nome esperado para o ficheiro: %s\n"), fName);

	hFile = CreateFile (
		fName,
		GENERIC_READ | GENERIC_WRITE | FILE_SHARE_READ | FILE_SHARE_WRITE,
		0,
		NULL,
		OPEN_EXISTING,
		FILE_ATTRIBUTE_NORMAL,
		NULL
	);

	if (hFile == INVALID_HANDLE_VALUE) {
		_ftprintf(stderr, TEXT("Erro na abertura do ficheiro!\n"));
		return 0;
	}

	hFileMapping = CreateFileMapping (
		hFile,
		NULL,
		PAGE_READWRITE,
		0,
		0,
		NULL
	);

	if (hFileMapping == NULL) {
		_ftprintf(stderr, TEXT("Erro na criação do mapping!\n"));
		return 0;
	}

	fBuff = MapViewOfFile (
		hFileMapping,
		FILE_MAP_READ | FILE_MAP_WRITE,
		0,
		0,
		0
	);

	if (fBuff == NULL) {
		_ftprintf(stderr, TEXT("Erro na abertura do mapping!\n"));
		return 0;
	}

	_tprintf(TEXT("\n\nBefore:\n"));
	for (i = 0; i < 26; i++) {
		_tprintf(TEXT("[%i] -%c-\n"), i, fBuff[i]);
	}

	for (i = 0; i < 13; i++) {
		temp = fBuff[i];
		fBuff[i] = fBuff[25 - i];
		fBuff[25 - i] = temp;
	}

	_tprintf(TEXT("\n\nAfter:\n"));
	for (i = 0; i < 26; i++) {
		_tprintf(TEXT("[%i] -%c-\n"), i, fBuff[i]);
	}

	UnmapViewOfFile(fBuff);

	CloseHandle(hFile);
	CloseHandle(hFileMapping);

	return 0;
}