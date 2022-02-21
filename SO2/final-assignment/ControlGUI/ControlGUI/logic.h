#ifndef LOGIC_H
#define LOGIC_H

#include <windows.h>

#include "structs.h"

BOOL caeroporto(TCHAR* name, int coordX, int coordY, pAirport airportsArray, pAirport airportsSharedArray, unsigned int* maxAirports, unsigned int* nrAirports);

#endif // !LOGIC_H