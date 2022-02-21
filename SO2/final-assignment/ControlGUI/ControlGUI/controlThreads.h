#ifndef CONTROL_THREADS_H
#define CONTROL_THREADS_H

#include <windows.h>

DWORD WINAPI oldMainThread(LPVOID param);

// Thread do Consumidor para a lógica do buffer circular
DWORD WINAPI consumerThread(LPVOID param);

// Thread das coordenadas
DWORD WINAPI coordinatesThread(LPVOID param);

// Thread dos pings
DWORD WINAPI pingThread(LPVOID param);

// Thread para controlo do pipe
DWORD WINAPI pipeThread(LPVOID param);

// Thread que lê e escreve para o pipe do cliente
DWORD WINAPI passengerThread(LPVOID param);

#endif // !CONTROL_THREADS_H
