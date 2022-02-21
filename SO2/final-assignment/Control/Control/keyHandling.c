#include "keyHandling.h"

// Get do valor da chave, retorna TRUE se tudo correr bem
BOOL getValueFromKey(HKEY hKey, unsigned int *value, TCHAR *valueName) {
	LSTATUS lStatus;
	DWORD valueSize = sizeof(*value);

	lStatus = RegQueryValueEx (
		hKey,
		valueName,
		NULL,
		NULL,
		(LPBYTE)value,
		&valueSize
	);

	if (lStatus != ERROR_SUCCESS) {
		*value = 0;
		return FALSE;
	}

	return TRUE;
}

// Get do numero maximo de aviões e do numero maximo de aeroportos, retorna TRUE se tudo correr bem
BOOL getMaximumValues(unsigned int *maxAirplanes, unsigned int *maxAirports) {
	LSTATUS lStatus;
	HKEY hKey;

	lStatus = RegOpenKeyEx (
		HKEY_CURRENT_USER,
		KEY_PATH,
		0,
		KEY_ALL_ACCESS,
		&hKey
	);

	if (lStatus != ERROR_SUCCESS) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível abrir a chave principal!\n"));
		RegCloseKey(hKey);
		return FALSE;
	}

	if (!getValueFromKey(hKey, maxAirplanes, KEY_MAX_AIRPLANES_NAME)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível obter o valor MaxAirplanes!\n"));
		RegCloseKey(hKey);
		return FALSE;
	}

	if (!getValueFromKey(hKey, maxAirports, KEY_MAX_AIRPORTS_NAME)) {
		_ftprintf(stderr, TEXT("[ERRO] Não foi possível obter o valor MaxAirports!\n"));
		RegCloseKey(hKey);
		return FALSE;
	}

	RegCloseKey(hKey);

	return TRUE;
}