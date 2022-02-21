#ifndef AIRPLANE_THREADS_H
#define AIRPLANE_THREADS_H

#include <windows.h>

#include <stdio.h>

DWORD WINAPI threadInterface(LPVOID param); // thread da interface
DWORD WINAPI threadMove(LPVOID param); // thread da movimentação
DWORD WINAPI threadProducer(LPVOID param); // thread do produtor (buffer circular)
DWORD WINAPI threadPing(LPVOID param); // thread dos pings
DWORD WINAPI stopEventThread(LPVOID param); // thread do evento de parar do controlador

#endif /* AIRPLANE_THREADS_H */