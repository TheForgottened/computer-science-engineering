#ifndef UTILS_H
#define UTILS_H

#include <tchar.h>
#include <stdlib.h>
#include <windows.h>

#define STR_SIZE 128

#define EVENT_CONTROL_CLOSED_NAME TEXT("EVENT_CONTROL_CLOSED_SO2TP")

#define PASSENGERS_PIPE_NAME TEXT("\\\\.\\pipe\\PASSENGERS_SO2TP")

#define PIPE_COMMAND_FLIGHT_ENDED TEXT("#FIM")
#define PIPE_COMMAND_AIRPLANE_CLOSED TEXT("#CAIU")

// Limpa uma string
TCHAR* resetString(TCHAR* str, unsigned int size);

// Conta e divide uma string
TCHAR** splitString(TCHAR* str, const TCHAR* delim, unsigned int* size);

// Verifica se a string é um número inteiro
BOOL isStringANumber(TCHAR* str);

#endif