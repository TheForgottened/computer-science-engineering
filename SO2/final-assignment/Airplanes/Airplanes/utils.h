#ifndef UTILS_H
#define UTILS_H

#include <tchar.h>
#include <stdlib.h>
#include <windows.h>

#define STR_SIZE 128

#define CIRCULAR_BUFFER_SIZE 20

#define EVENT_CONTROL_CLOSED_NAME TEXT("EVENT_CONTROL_CLOSED_SO2TP")

#define SEMAPHORE_MAX_AIRPLANES_NAME TEXT("SEMAPHORE_MAX_AIRPLANES_SO2TP") // Para garantir que não há mais aviões que o permitido

#define FILE_MAPPING_CIRCULAR_NAME TEXT("CIRCULAR_FILE_MAPPING_SO2TP")
#define SEMAPHORE_CIRCULAR_READ_NAME TEXT("CIRCULAR_SEMAPHORE_READ_SO2TP") 
#define SEMAPHORE_CIRCULAR_WRITE_NAME TEXT("CIRCULAR_SEMAPHORE_WRITE_SO2TP") 
#define MUTEX_CIRCULAR_NAME TEXT("CIRCULAR_MUTEX_SO2TP")

#define EVENT_ACTIVATE_SUSPEND_NAME TEXT("EVENT_ACTIVATE_SUSPEND_SO2TP")

#define FILE_MAPPING_AIRPORTS_NAME TEXT("AIRPORTS_FILE_MAPPING_SO2TP") // FileMapping para o array de aeroportos
#define MUTEX_AIRPORTS_NAME TEXT("MUTEX_AIRPORTS_SO2TP")

#define FILE_MAPPING_COORDINATES_NAME TEXT("COORDINATES_FILE_MAPPING_SO2TP")
#define MUTEX_COORDINATES_NAME TEXT("MUTEX_COORDINATES_SO2TP")

// Limpa uma string
TCHAR* resetString(TCHAR* str, unsigned int size);

// Conta e divide uma string
// Memória deve ser libertada pelo invocador
TCHAR** splitString(TCHAR* str, const TCHAR* delim, unsigned int* size);

// Verifica se a string é um número inteiro
BOOL isStringANumber(TCHAR* str);

// Inicia o gerado de número pseudorandoms
void initRandom();

// Gera um número inteiro pseudorandom entre min e max, inclusive
int randomInt(int min, int max);

#endif