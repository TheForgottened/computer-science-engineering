#include <tchar.h>
#include <math.h>
#include <stdio.h>
#include <fcntl.h>
#include <io.h>
#include <windows.h>

#define TAM 100

int _tmain(int argc, TCHAR* argv[]) {
    HKEY reg_key; // handle para a chave principal

    TCHAR MaximumValues_key_path[TAM] = TEXT("SOFTWARE\\SO2-TP\\MaximumValues"); // caminho das chaves

    DWORD key_res; // resultado do RegCreateKeyEx

    TCHAR key_name_planes[TAM] = TEXT("MaxAirplanes"), key_name_airports[TAM] = TEXT("MaxAirports");
    DWORD key_value_planes, key_value_airports;

#ifdef UNICODE
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    _tprintf(TEXT("Número máximo de aviões (default 10)? "));
    _tscanf_s(TEXT("%ul"), &key_value_planes);

    _tprintf(TEXT("Número máximo de aeroportos (default 5)? "));
    _tscanf_s(TEXT("%ul"), &key_value_airports);

    // Cria chave para valores máximos
    if ( RegCreateKeyEx(
            HKEY_CURRENT_USER,
            MaximumValues_key_path,
            0,
            NULL,
            REG_OPTION_NON_VOLATILE,
            KEY_ALL_ACCESS, NULL,
            &reg_key,
            &key_res
    ) != ERROR_SUCCESS) {
        _ftprintf(stderr, TEXT("[ERRO] Não foi possível criar a chave principal!\n"));
        return -1;
    }

    //  As chaves foram criadas
    if (key_res == REG_CREATED_NEW_KEY) {

        // Criar um par "nome-valor" para os aviões
        if (RegSetValueEx(reg_key, key_name_planes, 0, REG_DWORD, (LPBYTE)&key_value_planes, sizeof(key_value_planes)) != ERROR_SUCCESS) {
            _ftprintf(stderr, TEXT("[ERRO] Não foi possível adicionar o atributo %s!\n"), key_name_planes);
        }

        // Criar um par "nome-valor" para os aeroportos
        if (RegSetValueEx(reg_key, key_name_airports, 0, REG_DWORD, (LPBYTE)&key_value_airports, sizeof(key_value_airports)) != ERROR_SUCCESS) {
            _ftprintf(stderr, TEXT("[ERRO] Não foi possível adicionar o atributo %s!\n"), key_name_airports);
        }

        _tprintf(TEXT("Chave criada com sucesso!\n\n%s: %i\n%s: %i\n"), key_name_planes, key_value_planes, key_name_airports, key_value_airports);

        return 0;
    }

    _tprintf(TEXT("Chave já existe e o programa não alterou nada!\n"));

    return 0;
}

