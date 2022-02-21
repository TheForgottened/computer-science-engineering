#pragma once

#ifdef SO2_TP_DLL_2021_EXPORTS
#define DLL_IMP_API __declspec(dllexport)
#else
#define DLL_IMP_API __declspec(dllimport)
#endif

DLL_IMP_API int move(int cur_x, int cur_y, int final_dest_x, int final_dest_y, int * next_x, int * next_y);