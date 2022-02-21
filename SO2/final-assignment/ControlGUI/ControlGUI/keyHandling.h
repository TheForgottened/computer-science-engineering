#ifndef KEY_HANDLING_H
#define KEY_HANDLING_H

#include <tchar.h>
#include <windows.h>
#include <fcntl.h>

#define KEY_AIRPORT_PATH TEXT("SOFTWARE\\SO2-TP\\CurrentAirports")

#define KEY_PATH TEXT("SOFTWARE\\SO2-TP\\MaximumValues")
#define KEY_MAX_AIRPLANES_NAME TEXT("MaxAirplanes")
#define KEY_MAX_AIRPORTS_NAME TEXT("MaxAirports")

// Get do valor da chave, retorna TRUE se tudo correr bem
BOOL getValueFromKey(HKEY hKey, unsigned int* value, TCHAR* valueName);

// Get do numero maximo de aviões e do numero maximo de aeroportos, retorna TRUE se tudo correr bem
BOOL getMaximumValues(unsigned int* maxAirplanes, unsigned int* maxAirports);

#endif