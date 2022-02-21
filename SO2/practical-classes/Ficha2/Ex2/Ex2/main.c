#include <windows.h>
#include <winbase.h>
#include <winerror.h>
#include <winreg.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>
#define TAM 200

int _tmain(int argc, TCHAR* argv[]) {
	int result, i, size = TAM;
	HKEY chave;
	TCHAR chave_nome[TAM], par_nome[TAM], par_valor[TAM], chave_path[TAM], chave_path_bak[TAM], buffer[TAM];

	_tcscpy_s(chave_path, _countof(chave_path), TEXT("Software\\Aula"));
	_tcscpy_s(chave_path_bak, _countof(chave_path_bak), chave_path);
	
#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	/* ... as várias alíneas ...*/
	// a.
	_tprintf_s(TEXT("Olá! Insere o nome da chave: "));
	_tscanf_s(TEXT("%s"), chave_nome, TAM);

	_tcscat_s(chave_path, _countof(chave_path), "\\");
	_tcscat_s(chave_path, _countof(chave_path), chave_nome);
	_tprintf_s(TEXT("Chave indicada: %s.\n"), chave_path);

	// b.
	result = RegOpenKeyEx (HKEY_CURRENT_USER,
							chave_path,
							0,
							KEY_ALL_ACCESS,
							&chave);

	if (result == ERROR_FILE_NOT_FOUND) {
		result = RegCreateKeyEx (	HKEY_CURRENT_USER,
									chave_path,
									0,
									NULL,
									REG_OPTION_NON_VOLATILE,
									KEY_ALL_ACCESS,
									NULL,
									&chave,
									NULL
								 );

		if (result != ERROR_SUCCESS) {
			_ftprintf_s(stderr, TEXT("ERRO: Não foi possível criar a chave."));
			return 0;
		}

		_tprintf_s(TEXT("A chave de valor %s foi criada com sucesso!\n"), chave_nome);
	}


	// d.
	_tprintf_s(TEXT("\nDeseja fazer alguma coisa com chaves valor (1 - Criar / 2 - Eliminar / 3 - Consultar)? "), chave_path);
	_tscanf_s(TEXT("%i"), &i);

	switch (i) {
		case 1:
			_tprintf_s(TEXT("Nome da chave de valor a adicionar: "));
			_tscanf_s(TEXT("%s"), buffer, TAM);

			result = RegSetValueEx (
						chave,
						buffer,
						0,
						REG_SZ,
						NULL,
						0
					);

			if (result != ERROR_SUCCESS) {
				_ftprintf_s(stderr, TEXT("ERRO: Não foi possível criar a chave de valor."));
				return 0;
			}

			_tprintf_s(TEXT("A chave de valor %s foi criada com sucesso!\n"), buffer);
			break;

		case 2:
			_tprintf_s(TEXT("Nome da chave de valor a remover: "));
			_tscanf_s(TEXT("%s"), buffer, TAM);

			result = RegDeleteValue (
							chave,
							buffer
						);

			if (result != ERROR_SUCCESS) {
				_ftprintf_s(stderr, TEXT("ERRO: Não foi possível remover a chave de valor."));
				return 0;
			}

			_tprintf_s(TEXT("A chave de valor %s foi removida com sucesso!\n"), buffer);
			break;

		case 3:
			break;

		default:
			_ftprintf_s(stderr, TEXT("ERRO: Opção inválida."));
			return 0;
			break;
	}


	// e.
	_tprintf_s(TEXT("\nLista de chaves valor:\n"), chave_path);

	for (i = 0; ; i++) {
		result = RegEnumValue (
					chave,
					i,
					buffer,
					&size,
					NULL,
					NULL,
					NULL,
					NULL
				);
		
		if (result == ERROR_NO_MORE_ITEMS) {
			if (i == 0) {
				_tprintf_s(TEXT("Sem chaves de valor...\n"));
			}

			break;
		}

		if (result != ERROR_SUCCESS) {
			_ftprintf_s(stderr, TEXT("ERRO: Não foi ler as chaves valor."));
			return 0;
		}

		_tprintf_s(TEXT("- %s\n"), buffer);
	}

	_tprintf_s(TEXT("\n"));

	_tprintf_s(TEXT("\nDeseja eliminar a chave e respetivas chaves valor? (0 - Não / 1 - Sim)? "), chave_path);
	_tscanf_s(TEXT("%i"), &i);

	switch (i) {
		case 0:
			break;

		case 1:
			result = RegDeleteTree (
							HKEY_CURRENT_USER,
							chave_path_bak
						);

			if (result != ERROR_SUCCESS) {
				_ftprintf_s(stderr, TEXT("ERRO: Não foi possível remover a chave."));
				return 0;
			}

			_tprintf_s(TEXT("A chave %s foi removida com sucesso!\n"), chave_path_bak);
			break;
	}

	RegCloseKey(chave);

	_tprintf_s(TEXT("\n\n\n"));
	return 0;
}