#ifndef CALLBACKHEADER_H
#define CALLBACKHEADER_H

#include <windows.h>
#include <Windowsx.h>



LRESULT CALLBACK TrataEventos(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK TrataEventosHelp(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK TrataEventosListarAirplanes(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK TrataEventosListarAirports(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK TrataEventosAddAirport(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK TrataEventosSusAirplanes(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam);

#endif