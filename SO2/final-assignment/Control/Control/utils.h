#ifndef UTILS_H
#define UTILS_H

#include <tchar.h>
#include <stdlib.h>
#include <windows.h>

#define STR_SIZE 128

#define MAX_PASSENGERS 255 // máximo de pipes por defeito que o sistema operativo permite - 255

#define CIRCULAR_BUFFER_SIZE 20 // Tamanho do buffer circular
#define DEFAULT_MAX_AIRPLANES 10 // Numero default máximo de avioes
#define DEFAULT_MAX_AIRPORTS 5 // Numero default máximo de aeroportos

#define EVENT_CONTROL_CLOSED_NAME TEXT("EVENT_CONTROL_CLOSED_SO2TP")

#define SEMAPHORE_UNIQUE_CONTROL_NAME TEXT("CONTROL_SEMAPHORE_SO2TP")
#define SEMAPHORE_MAX_AIRPLANES_NAME TEXT("SEMAPHORE_MAX_AIRPLANES_SO2TP") // Para garantir que não há mais aviões que o permitido
#define EVENT_ACTIVATE_SUSPEND_NAME TEXT("EVENT_ACTIVATE_SUSPEND_SO2TP")

#define FILE_MAPPING_AIRPORTS_NAME TEXT("AIRPORTS_FILE_MAPPING_SO2TP") // FileMapping para o array de aeroportos
#define MUTEX_AIRPORTS_NAME TEXT("MUTEX_AIRPORTS_SO2TP")

#define FILE_MAPPING_CIRCULAR_NAME TEXT("CIRCULAR_FILE_MAPPING_SO2TP") // Nome do FileMapping do Buffer Circular
#define SEMAPHORE_CIRCULAR_READ_NAME TEXT("CIRCULAR_SEMAPHORE_READ_SO2TP")  // Nome do Semaforo de Leitura
#define SEMAPHORE_CIRCULAR_WRITE_NAME TEXT("CIRCULAR_SEMAPHORE_WRITE_SO2TP")  // Nome do Semaforo de Escrita

#define FILE_MAPPING_COORDINATES_NAME TEXT("COORDINATES_FILE_MAPPING_SO2TP") // Nome do FileMapping de coordenadas
#define MUTEX_COORDINATES_NAME TEXT("MUTEX_COORDINATES_SO2TP") // Nome do Mutex das coordenadas

#define PASSENGERS_PIPE_NAME TEXT("\\\\.\\pipe\\PASSENGERS_SO2TP")

#define PIPE_COMMAND_FLIGHT_ENDED TEXT("#FIM")
#define PIPE_COMMAND_AIRPLANE_CLOSED TEXT("#CAIU")

#define CORD_MAX_X 1000
#define CORD_MAX_Y 1000

// Limpa uma string
TCHAR* resetString(TCHAR* str, unsigned int size);

// Conta e divide uma string
TCHAR** splitString(TCHAR* str, const TCHAR* delim, unsigned int* size);

// Verifica se a string é um número inteiro
BOOL isStringANumber(TCHAR* str);

// Aplica o toupper a uma string inteira
TCHAR* toUpperString(TCHAR* str);

#endif
