#ifndef DLL_HANDLING_H
#define DLL_HANDLING_H

#include <tchar.h>
#include <windows.h>
#include <fcntl.h>

#define DLL_NAME TEXT("SO2_TP_DLL_2021.dll")

// 0 - chegou à posição final
// 1 - fez movimentação correta
// 2 - erro
int (*getNextPosition)(int cur_x, int cur_y, int final_dest_x, int final_dest_y, int* next_x, int* next_y);

BOOL setupDll();

#endif