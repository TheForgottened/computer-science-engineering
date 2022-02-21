#include <windows.h>
#include <Windowsx.h>
#include <tchar.h>

#include "setup.h"
#include "logic.h"
#include "resource.h"
#include "structs.h"
#include "keyHandling.h"
#include "callbackHeaders.h"
#include "utils.h"

HINSTANCE hInstance;
TCHAR szProgName[] = TEXT("Control");


int WINAPI WinMain(HINSTANCE hInst, HINSTANCE hPrevInst, LPSTR lpCmdLine, int nCmdShow) {
	oldMainThreadStruct oldMain;
	sharedDataStruct sharedData;

	unsigned int maxAirplanes = 0, maxAirports = 0;
	unsigned int nrAirports = 0, nrAirplanes = 0, nrPassengers = 0;

	BOOL stop = FALSE, fMouseTracking = FALSE;

	// Vai buscar os valores maximos referentes aos aviões e aeroportos
	if (!getMaximumValues(&maxAirplanes, &maxAirports)) {
		maxAirplanes = DEFAULT_MAX_AIRPLANES;
		maxAirports = DEFAULT_MAX_AIRPORTS;
	}

	// inicializa arrays
	pAirport airportsArray = malloc(maxAirports * sizeof(airport));
	pAirplane airplanesArray = malloc(maxAirplanes * sizeof(airplane));
	pPassenger passengersArray = malloc(MAX_PASSENGERS * sizeof(passenger));

	if (airportsArray == NULL || airplanesArray == NULL || passengersArray == NULL) {
		free(airportsArray);
		free(airplanesArray);
		free(passengersArray);

		return -1;
	}

	// inicializa as critical sections
	CRITICAL_SECTION criticalSectionAirplanes;
	CRITICAL_SECTION criticalSectionAirports;
	CRITICAL_SECTION criticalSectionPassengers;
	CRITICAL_SECTION criticalSectionBool;

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionAirplanes,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionAirports,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPassengers,
		500
	);

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionBool,
		500
	);

	// Cria o filemapping dos aeroportos
	HANDLE hFileMapAirports = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(airport) * maxAirports,
		FILE_MAPPING_AIRPORTS_NAME
	);

	if (hFileMapAirports == NULL) {
		free(airportsArray);
		free(airplanesArray);
		free(passengersArray);

		return -1;
	}

	// Mapeia o bloco de memoria dos aeroportos
	pAirport airportsSharedArray = (pAirport)MapViewOfFile(
		hFileMapAirports,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		0
	);

	if (airportsSharedArray == NULL) {
		free(airportsArray);
		free(airplanesArray);
		free(passengersArray);

		return -1;
	}

	HWND hWnd;
	MSG lpMsg;
	WNDCLASSEX wcApp;

	wcApp.cbSize = sizeof(WNDCLASSEX);
	wcApp.hInstance = hInst;
	hInstance = hInst;

	wcApp.lpszClassName = szProgName;
	wcApp.lpfnWndProc = TrataEventos;

	wcApp.style = CS_HREDRAW | CS_VREDRAW;

	wcApp.hIcon = LoadIcon(hInst, MAKEINTRESOURCE(IDI_APP_ICON));
	wcApp.hIconSm = LoadIcon(hInst, MAKEINTRESOURCE(IDI_ICON1));
	wcApp.hCursor = LoadCursor(NULL, IDC_ARROW);
	wcApp.lpszMenuName = MAKEINTRESOURCE(ID_MAIN_MENU);

	wcApp.cbClsExtra = sizeof(sharedDataStruct);
	wcApp.cbWndExtra = 0;
	wcApp.hbrBackground = CreateSolidBrush(RGB(220, 220, 220));

	if (!RegisterClassEx(&wcApp)) return 0;

	// Cria janela principal
	hWnd = CreateWindow(
		szProgName,
		TEXT("Control - SO2 TP"),
		WS_OVERLAPPED | WS_MINIMIZEBOX | WS_SYSMENU,
		CW_USEDEFAULT,
		CW_USEDEFAULT,
		1000,
		700,
		(HWND)HWND_DESKTOP,
		(HMENU)NULL,
		(HINSTANCE)hInst,
		0
	);

	// inicializa struct da main
	oldMain.maxAirplanes = &maxAirplanes;
	oldMain.maxAirports = &maxAirports;
	oldMain.nrAirports = &nrAirports;
	oldMain.nrAirplanes = &nrAirplanes;
	oldMain.nrPassengers = &nrPassengers;
	oldMain.stop = &stop;
	oldMain.airportsArray = airportsArray;
	oldMain.airplanesArray = airplanesArray;
	oldMain.passengersArray = passengersArray;
	oldMain.airportsSharedArray = airportsSharedArray;
	// hEventActivateSuspend declarado dentro do initialSetup()
	// hSetupFinishedEvent declarado dentro do initialSetup()
	oldMain.hMainWnd = hWnd;
	oldMain.criticalSectionAirplanes = &criticalSectionAirplanes;
	oldMain.criticalSectionAirports = &criticalSectionAirports;
	oldMain.criticalSectionPassengers = &criticalSectionPassengers;
	oldMain.criticalSectionBool = &criticalSectionBool;

	// Cria thread do initialSetup que vai inicializar todas as outras threads
	HANDLE hOldMainThread = initialSetup(&oldMain);


	if (hOldMainThread == INVALID_HANDLE_VALUE) return -1;

	// Se existir o TIMEOUT chegar ao fim significa que alguma coisa nao inicializou
	if (WaitForSingleObject(oldMain.hSetupFinishedEvent, 10000) == WAIT_TIMEOUT) return -1;

	// Fazemos reset ao evento
	ResetEvent(oldMain.hSetupFinishedEvent);

	HDC hdc;
	HANDLE hBmpAirplane, hBmpAirport;

	// LoadImage do avião
	hBmpAirplane = (HBITMAP)LoadImage(NULL, TEXT("airplane.bmp"), IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE | LR_LOADTRANSPARENT);
	GetObject(hBmpAirplane, sizeof(sharedData.bitAirplane), &sharedData.bitAirplane); // vai buscar info sobre o handle do bitmap

	hdc = GetDC(hWnd);
	// criamos copia do device context e colocar em memoria
	sharedData.bitAirplaneDC = CreateCompatibleDC(hdc);
	// aplicamos o bitmap ao device context
	SelectObject(sharedData.bitAirplaneDC, hBmpAirplane);
	ReleaseDC(hWnd, hdc);

	// LoadImage do aeroporto
	hBmpAirport = (HBITMAP)LoadImage(NULL, TEXT("airport.bmp"), IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE | LR_LOADTRANSPARENT);
	GetObject(hBmpAirport, sizeof(sharedData.bitAirport), &sharedData.bitAirport); // vai buscar info sobre o handle do bitmap

	hdc = GetDC(hWnd);
	// criamos copia do device context e colocar em memoria
	sharedData.bitAirportDC = CreateCompatibleDC(hdc);
	// aplicamos o bitmap ao device context
	SelectObject(sharedData.bitAirportDC, hBmpAirport);
	ReleaseDC(hWnd, hdc);

	// inicialziamos a critical section da pintura
	CRITICAL_SECTION criticalSectionPaint;

	InitializeCriticalSectionAndSpinCount(
		&criticalSectionPaint,
		500
	);

	sharedData.hMainWnd = hWnd;
	sharedData.memDC = NULL;
	// bitAirportDC inicializado antes
	// bitAirplaneDC inicializado antes
	// bitAirport inicializado antes
	// bitAirplane inicializado antes
	sharedData.hBitmapDB = NULL;
	sharedData.criticalSectionPaint = &criticalSectionPaint;
	sharedData.hOldMainThread = hOldMainThread;
	sharedData.maxAirplanes = &maxAirplanes;
	sharedData.maxAirports = &maxAirports;
	sharedData.nrAirports = &nrAirports;
	sharedData.nrAirplanes = &nrAirplanes;
	sharedData.nrPassengers = &nrPassengers;
	sharedData.stop = &stop;
	sharedData.fMouseTracking = &fMouseTracking;
	sharedData.airportsArray = airportsArray;
	sharedData.airplanesArray = airplanesArray;
	sharedData.passengersArray = passengersArray;
	sharedData.airportsSharedArray = airportsSharedArray;
	sharedData.hEventActivateSuspend = oldMain.hEventActivateSuspend;
	sharedData.criticalSectionAirplanes = &criticalSectionAirplanes;
	sharedData.criticalSectionAirports = &criticalSectionAirports;
	sharedData.criticalSectionPassengers = &criticalSectionPassengers;
	sharedData.criticalSectionBool = &criticalSectionBool;

	// Passa a estrutura sharedData para a função callback principal
	LONG_PTR x = SetWindowLongPtr(hWnd, GWLP_USERDATA, (LONG_PTR)&sharedData);

	ShowWindow(hWnd, nCmdShow);

	while (GetMessage(&lpMsg, NULL, 0, 0)) {
		TranslateMessage(&lpMsg);
		DispatchMessage(&lpMsg);
	}

	return((int)lpMsg.wParam);
}

// Função callback principal
LRESULT CALLBACK TrataEventos(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	//Recebe a estrutura 
	pSharedDataStruct mainData = (pSharedDataStruct)GetWindowLongPtr(hWnd, GWLP_USERDATA);
	HDC hdc;
	PAINTSTRUCT ps;
	RECT rect;

	static HWND hWndStaticTextInfo;
	static TRACKMOUSEEVENT trackMouseEvent;
	TCHAR initialStr[BUFFER_SIZE];

	int mapOrigin[2] = { 0, 0 }; // coordenadas do mapa
	int mouseCoordinates[2] = { 0, 0 }; // coordenadas do rato
	int nrPassengers = 0;
	BOOL breakFlag = TRUE;

	switch (messg) {
	case WM_CREATE:
		GetClientRect(hWnd, &rect);

		// Cria a janela lateral direita
		hWndStaticTextInfo = CreateWindow(
			TEXT("STATIC"),
			TEXT(""),
			WS_VISIBLE | WS_CHILD | SS_CENTER | WS_CLIPCHILDREN,
			rect.left + MAP_OFFSET + MAP_SIDE + MAP_OFFSET + BITMAP_SIZE,
			rect.top,
			rect.right - (rect.left + MAP_OFFSET + MAP_SIDE + MAP_OFFSET + BITMAP_SIZE),
			rect.bottom - rect.top,
			hWnd,
			NULL,
			hInstance,
			NULL
		);
		// começa vazia a janela
		SetWindowText(hWndStaticTextInfo, TEXT(""));

		break;

	case WM_COMMAND:
		switch (LOWORD(wParam)) {
			// Caso tenha carregado no botão de Ajuda
		case IDM_HELP:
			DialogBox(NULL, MAKEINTRESOURCE(IDD_HELP), hWnd, TrataEventosHelp);
			break;
			// Caso tenha carregado no botão de Listar Aviões
		case IDM_LISTAR_AIRPLANES:
			DialogBox(NULL, MAKEINTRESOURCE(IDD_HELP), hWnd, TrataEventosListarAirplanes);
			break;
			// Caso tenha carregado no botão de Listar Aeroportos
		case IDM_LISTAR_AIRPORTS:
			DialogBox(NULL, MAKEINTRESOURCE(IDD_HELP), hWnd, TrataEventosListarAirports);
			break;
			// Caso tenha carregado no botão de Adicionar Aeroportos
		case IDM_ADD_AIRPORTS:
			DialogBox(NULL, MAKEINTRESOURCE(IDD_ADD_AIRPORTS), hWnd, TrataEventosAddAirport);
			break;
			// Caso tenha carregado no botão de Aceitar/Suspender Aviões
		case IDM_SUS_AIRPLANES:
			DialogBox(NULL, MAKEINTRESOURCE(IDD_SUS_AIRPLANES), hWnd, TrataEventosSusAirplanes);
			break;
			// Caso tenha carregado no botão de Sair
		case IDM_SHUTDOWN:
			EnterCriticalSection(mainData->criticalSectionBool);
			*mainData->stop = TRUE;
			LeaveCriticalSection(mainData->criticalSectionBool);

			WaitForSingleObject(mainData->hOldMainThread, INFINITE);

			PostQuitMessage(0);
			break;
		}

		break;

	// Onde se encontra a lógica do double buffer
	case WM_PAINT:
		hdc = BeginPaint(hWnd, &ps);

		GetClientRect(hWnd, &rect);

		// Vamos buscar as origens do mapa
		mapOrigin[0] = rect.left + MAP_OFFSET;
		mapOrigin[1] = rect.top + MAP_OFFSET + MAP_SIDE;

		// Se a copia estiver a NULL, significa que é a 1ª vez que estamos a passar no WM_PAINT e estamos a trabalhar com a copia em memoria
		if (mainData->memDC == NULL) {
			// cria copia em memoria
			mainData->memDC = CreateCompatibleDC(hdc);
			mainData->hBitmapDB = CreateCompatibleBitmap(hdc, rect.right, rect.bottom);
			// aplicamos na copia em memoria as configs que obtemos com o CreateCompatibleBitmap
			SelectObject(mainData->memDC, mainData->hBitmapDB);
			DeleteObject(mainData->hBitmapDB);
		}
		// Operações feitas na copia(memDC) , operações feitas na memória
		FillRect(mainData->memDC, &rect, CreateSolidBrush(RGB(220, 220, 220)));

		BLENDFUNCTION blendFunction;
		blendFunction.BlendOp = AC_SRC_OVER;
		blendFunction.BlendFlags = 0;
		blendFunction.SourceConstantAlpha = 0;
		blendFunction.AlphaFormat = 0;

		EnterCriticalSection(mainData->criticalSectionPaint);
		EnterCriticalSection(mainData->criticalSectionAirports);
		// Para cada aeroporto existente, desenhamos o seu bitmap na sua coordenada (isto ainda é em memória)
		for (unsigned int i = 0; i < *mainData->nrAirports; i++) {
			GdiAlphaBlend(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airportsArray[i].coordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airportsArray[i].coordinates.y * MAP_SCALE),
				mainData->bitAirport.bmWidth,
				mainData->bitAirport.bmHeight,
				mainData->bitAirportDC,
				0,
				0,
				mainData->bitAirport.bmWidth,
				mainData->bitAirport.bmHeight,
				blendFunction
			);

			GdiTransparentBlt(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airportsArray[i].coordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airportsArray[i].coordinates.y * MAP_SCALE),
				mainData->bitAirport.bmWidth,
				mainData->bitAirport.bmHeight,
				mainData->bitAirportDC,
				0,
				0,
				mainData->bitAirport.bmWidth,
				mainData->bitAirport.bmHeight,
				RGB(255, 255, 255)
			);
			
			/*
			BitBlt(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airportsArray[i].coordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airportsArray[i].coordinates.y * MAP_SCALE),
				mainData->bitAirport.bmWidth,
				mainData->bitAirport.bmHeight,
				mainData->bitAirportDC,
				0,
				0,
				SRCCOPY
			);
			*/
		}
		LeaveCriticalSection(mainData->criticalSectionAirports);

		EnterCriticalSection(mainData->criticalSectionAirplanes);
		// Para cada avião existente, desenhamos o seu bitmap na sua coordenada (insto ainda é em memória)
		for (unsigned int i = 0; i < *mainData->nrAirplanes; i++) {
			// Se o avião estiver parado não é desenhado
			if (mainData->airplanesArray[i].stopped) continue;

			GdiAlphaBlend(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airplanesArray[i].currentCoordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airplanesArray[i].currentCoordinates.y * MAP_SCALE),
				mainData->bitAirplane.bmWidth,
				mainData->bitAirplane.bmHeight,
				mainData->bitAirplaneDC,
				0,
				0,
				mainData->bitAirplane.bmWidth,
				mainData->bitAirplane.bmHeight,
				blendFunction
			);

			GdiTransparentBlt(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airplanesArray[i].currentCoordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airplanesArray[i].currentCoordinates.y * MAP_SCALE),
				mainData->bitAirplane.bmWidth,
				mainData->bitAirplane.bmHeight,
				mainData->bitAirplaneDC,
				0,
				0,
				mainData->bitAirplane.bmWidth,
				mainData->bitAirplane.bmHeight,
				RGB(255, 255, 255)
			);

			/*
			BitBlt(
				mainData->memDC,
				mapOrigin[0] + (int)(mainData->airplanesArray[i].currentCoordinates.x * MAP_SCALE),
				mapOrigin[1] - (int)(mainData->airplanesArray[i].currentCoordinates.y * MAP_SCALE),
				mainData->bitAirplane.bmWidth,
				mainData->bitAirplane.bmHeight,
				mainData->bitAirplaneDC,
				0,
				0,
				SRCCOPY
			);
			*/
		}
		LeaveCriticalSection(mainData->criticalSectionAirplanes);

		// definir ponto partida
		MoveToEx(mainData->memDC, mapOrigin[0] - 1, mapOrigin[1] + BITMAP_SIZE + 2, NULL);
		// linha inferior esquerda -> direita
		LineTo(mainData->memDC, mapOrigin[0] + MAP_SIDE + BITMAP_SIZE + 2, mapOrigin[1] + BITMAP_SIZE + 2);
		// linha direira baixo -> cima
		LineTo(mainData->memDC, mapOrigin[0] + MAP_SIDE + BITMAP_SIZE + 2, mapOrigin[1] - MAP_SIDE - 2);
		// linha superior direita -> esquerda
		LineTo(mainData->memDC, mapOrigin[0] - 2, mapOrigin[1] - MAP_SIDE - 2);
		// linha esquerda cima -> baixo
		LineTo(mainData->memDC, mapOrigin[0] - 2, mapOrigin[1] + BITMAP_SIZE + 3);

		LeaveCriticalSection(mainData->criticalSectionPaint);

		// Bitblit da copia que esta em memoria para a janela principal - é a unica operação feita na janela principal
		BitBlt(hdc, 0, 0, rect.right, rect.bottom, mainData->memDC, 0, 0, SRCCOPY);

		// Encerra a pintura, que substitui o ReleaseDC
		EndPaint(hWnd, &ps);
		break;

	// Caso seja carregado no botão do lado esquerdo do rato
	case WM_LBUTTONDOWN:
		SetWindowText(hWndStaticTextInfo, TEXT(""));

		// Guardamos as coordenadas do rato onde foi carregado
		mouseCoordinates[0] = GET_X_LPARAM(lParam); 
		mouseCoordinates[1] = GET_Y_LPARAM(lParam); 

		int airportCoordinates[2] = { 0, 0 };
		TCHAR airportName[STR_SIZE] = TEXT("");
		int nrAirplanes = 0;
		nrPassengers = 0;
		breakFlag = TRUE;

		GetClientRect(hWnd, &rect);

		mapOrigin[0] = rect.left + MAP_OFFSET;
		mapOrigin[1] = rect.top + MAP_OFFSET + MAP_SIDE;

		EnterCriticalSection(mainData->criticalSectionAirports);
		// Para cada aeroporto, se tivermos a carregar em cima de um aeroporto metemos a breakFlag a falso
		for (unsigned int i = 0; i < *mainData->nrAirports; i++) {
			if (
				mouseCoordinates[0] >= (mapOrigin[0] + (mainData->airportsArray[i].coordinates.x * MAP_SCALE))
				&& mouseCoordinates[1] >= (mapOrigin[1] - (mainData->airportsArray[i].coordinates.y * MAP_SCALE))
				&& mouseCoordinates[0] <= ((mapOrigin[0] + (mainData->airportsArray[i].coordinates.x * MAP_SCALE)) + mainData->bitAirport.bmWidth)
				&& mouseCoordinates[1] <= ((mapOrigin[1] - (mainData->airportsArray[i].coordinates.y * MAP_SCALE)) + mainData->bitAirport.bmHeight)
				) {
				airportCoordinates[0] = mainData->airportsArray[i].coordinates.x;
				airportCoordinates[1] = mainData->airportsArray[i].coordinates.y;
				_tcscpy_s(airportName, _countof(airportName), mainData->airportsArray[i].name);

				breakFlag = FALSE;
				break;
			}
		}
		LeaveCriticalSection(mainData->criticalSectionAirports);

		if (breakFlag) break; // Se tiver a true significa que nao carregamos em cima de um aeroporto

		EnterCriticalSection(mainData->criticalSectionAirplanes);
		// Caso tenhamos carregado num aeroporto vamos ver quantos aviões o mesmo alberga
		for (unsigned int i = 0; i < *mainData->nrAirplanes; i++) {
			if (!mainData->airplanesArray[i].stopped) continue;

			if (_tcsicmp(mainData->airplanesArray[i].srcAirport, airportName) == 0) nrAirplanes++;
		}
		LeaveCriticalSection(mainData->criticalSectionAirplanes);

		EnterCriticalSection(mainData->criticalSectionPassengers);
		// Caso tenhamos carregado num aeroporto vamos ver quantos passageiros o mesmo alberga
		for (unsigned int i = 0; i < *mainData->nrPassengers; i++) {
			if (!mainData->passengersArray[i].stopped) continue;

			if (_tcsicmp(mainData->passengersArray[i].srcAirport, airportName) == 0) nrPassengers++;
		}
		LeaveCriticalSection(mainData->criticalSectionPassengers);
		// Copiamos toda a informação para uma string
		_stprintf_s(
			initialStr,
			_countof(initialStr),
			TEXT("Aeroporto %s:\n\nNúmero de aviões: %i\nNúmero de passageiros: %i\n"),
			airportName,
			nrAirplanes,
			nrPassengers
		);
		// Mostramos a string no static text
		SetWindowText(hWndStaticTextInfo, initialStr);

		break;

	case WM_MOUSEMOVE:
		GetWindowText(hWndStaticTextInfo, initialStr, _countof(initialStr));

		if (!*mainData->fMouseTracking && _tcscmp(initialStr, TEXT("")) == 0) {
			TRACKMOUSEEVENT tme;

			tme.cbSize = sizeof(TRACKMOUSEEVENT);
			tme.dwFlags = TME_HOVER | TME_LEAVE;
			tme.hwndTrack = hWnd;
			tme.dwHoverTime = 200;
			*mainData->fMouseTracking = TrackMouseEvent(&tme);

			break;
		}

		SetWindowText(hWndStaticTextInfo, TEXT(""));
		break;

	case WM_MOUSEHOVER:
		GetWindowText(hWndStaticTextInfo, initialStr, _countof(initialStr));

		*mainData->fMouseTracking = FALSE;

		if (_tcscmp(initialStr, TEXT("")) != 0) break;

		// Guardamos as coordenadas do rato onde foi carregado
		mouseCoordinates[0] = GET_X_LPARAM(lParam);
		mouseCoordinates[1] = GET_Y_LPARAM(lParam);

		TCHAR srcAirport[STR_SIZE] = TEXT(""), destAirport[STR_SIZE] = TEXT("");
		DWORD airplaneId = 0;
		nrPassengers = 0;
		breakFlag = TRUE;

		GetClientRect(hWnd, &rect);

		mapOrigin[0] = rect.left + MAP_OFFSET;
		mapOrigin[1] = rect.top + MAP_OFFSET + MAP_SIDE;

		// lógica do avião
		// mostrar id
		// mostrar src e dest
		// mostrar número de passageiros

		EnterCriticalSection(mainData->criticalSectionAirplanes);
		// Para cada aeroporto, se tivermos a carregar em cima de um aeroporto metemos a breakFlag a falso
		for (unsigned int i = 0; i < *mainData->nrAirplanes; i++) {
			if (
				mouseCoordinates[0] >= (mapOrigin[0] + (mainData->airplanesArray[i].currentCoordinates.x * MAP_SCALE))
				&& mouseCoordinates[1] >= (mapOrigin[1] - (mainData->airplanesArray[i].currentCoordinates.y * MAP_SCALE))
				&& mouseCoordinates[0] <= ((mapOrigin[0] + (mainData->airplanesArray[i].currentCoordinates.x * MAP_SCALE)) + mainData->bitAirplane.bmWidth)
				&& mouseCoordinates[1] <= ((mapOrigin[1] - (mainData->airplanesArray[i].currentCoordinates.y * MAP_SCALE)) + mainData->bitAirplane.bmHeight)
				) {

				if (mainData->airplanesArray[i].stopped) break;

				_tcscpy_s(srcAirport, _countof(srcAirport), mainData->airplanesArray[i].srcAirport);
				_tcscpy_s(destAirport, _countof(destAirport), mainData->airplanesArray[i].destAirport);
				airplaneId = mainData->airplanesArray[i].id;

				breakFlag = FALSE;
				break;
			}
		}
		LeaveCriticalSection(mainData->criticalSectionAirplanes);

		if (breakFlag) break; // Se tiver a true significa que temos o rato em cima de um avião

		EnterCriticalSection(mainData->criticalSectionPassengers);
		// Caso tenhamos carregado num aeroporto vamos ver quantos passageiros o mesmo alberga
		for (unsigned int i = 0; i < *mainData->nrPassengers; i++) {
			if (mainData->passengersArray[i].stopped) continue;

			if (mainData->passengersArray[i].airplaneId == airplaneId) nrPassengers++;
		}
		LeaveCriticalSection(mainData->criticalSectionPassengers);

		// Copiamos toda a informação para uma string
		_stprintf_s(
			initialStr,
			_countof(initialStr),
			TEXT("Avião %ld:\n\nOrigem: %s - Destino: %s\nNúmero de passageiros: %i\n"),
			airplaneId,
			srcAirport,
			destAirport,
			nrPassengers
		);
		// Mostramos a string no static text
		SetWindowText(hWndStaticTextInfo, initialStr);

		TRACKMOUSEEVENT tme;
		tme.cbSize = sizeof(TRACKMOUSEEVENT);
		tme.dwFlags = TME_LEAVE;
		tme.hwndTrack = hWnd;
		TrackMouseEvent(&tme);

		break;

	case WM_MOUSELEAVE:
		GetWindowText(hWndStaticTextInfo, initialStr, _countof(initialStr));

		*mainData->fMouseTracking = FALSE;

		if (_tcscmp(initialStr, TEXT("")) == 0) break;

		SetWindowText(hWndStaticTextInfo, TEXT(""));
		break;

	case WM_ERASEBKGND:
		return TRUE;
		
	case WM_DESTROY:
		EnterCriticalSection(mainData->criticalSectionBool);
		*mainData->stop = TRUE;
		LeaveCriticalSection(mainData->criticalSectionBool);

		WaitForSingleObject(mainData->hOldMainThread, INFINITE);

		PostQuitMessage(0);
		break;

	default:
		return DefWindowProc(hWnd, messg, wParam, lParam);
		break;
	}

	return 0;
}
// Função callback para a janela de ajuda
LRESULT CALLBACK TrataEventosHelp(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	RECT tempRect;
	switch (messg) {
	case WM_INITDIALOG:
		GetWindowRect(hWnd, &tempRect);

		TCHAR initialStr[BUFFER_SIZE];

		_stprintf_s(
			initialStr,
			_countof(initialStr),
			TEXT("Listar:\n%s\n\nAdicionar Aeroportos:\n%s\n\nAceitar/Suspender Aviões:\n%s\n\nEncerrar:\n%s\n\nAjuda:\n%s\n\n\nClique Esquerdo Num Aeroporto:\n%s\n\nHover Sobre Um Avião:\n%s\n"),
			TEXT("Lista todos os aviões ou aeroportos existentes no sistema."),
			TEXT("Abre uma janela que permite adicionar aeroportos."),
			TEXT("Ativa ou desativa a suspensão de aviões no sistema. A janela que abre irá dizer-lhe o estado em que se encontra e existe apenas um botão que altera entre estes dois estados."),
			TEXT("Encerra todo o sistema."),
			TEXT("Abre esta janela."),
			TEXT("Mostra o nome do aeroporto e quantos aviões e passageiros alberga."),
			TEXT("Mostra o ID do avião, o aeroporto de origem, o aeroporto de destino e o número de passageiros a bordo.")
		);

		HWND hWndExample = CreateWindow(
			TEXT("STATIC"),
			initialStr,
			WS_VISIBLE | WS_CHILD | SS_CENTER,
			CW_USEDEFAULT,
			CW_USEDEFAULT,
			tempRect.right - tempRect.left - 20,
			tempRect.bottom - tempRect.top - 100,
			hWnd,
			NULL,
			hInstance,
			NULL
		);

		break;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK_FECHAR) EndDialog(hWnd, 0);
		break;

	case WM_CLOSE:
		EndDialog(hWnd, 0);
		return TRUE;
	}

	return FALSE;
}
// Função callback para listar aviões
LRESULT CALLBACK TrataEventosListarAirplanes(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	HWND auxhWnd = GetParent(hWnd); // GetParent vai servir para depois conseguir ir buscar a struct declarada no main e nao usar vars globais
	pSharedDataStruct mainData = (pSharedDataStruct)GetWindowLongPtr(auxhWnd, GWLP_USERDATA);

	switch (messg) {
	case WM_INITDIALOG:
		SetWindowText(hWnd, TEXT("Listar Aviões"));

		int length;
		TCHAR initialStr[BUFFER_SIZE];

		RECT tempRect;
		GetWindowRect(hWnd, &tempRect);
		// Cria janela com o static text
		HWND hWndList = CreateWindow(
			TEXT("STATIC"),
			TEXT("\0"),
			WS_VISIBLE | WS_CHILD | SS_CENTER,
			CW_USEDEFAULT,
			CW_USEDEFAULT,
			tempRect.right - tempRect.left - 20,
			tempRect.bottom - tempRect.top - 100,
			hWnd,
			NULL,
			hInstance,
			NULL
		);

		EnterCriticalSection(mainData->criticalSectionAirplanes);
		// Percorre o array de aviões e guarda as informações do mesmo na initialStr
		for (unsigned int i = 0; i < *mainData->nrAirplanes; i++) {
			_stprintf_s(
				initialStr,
				_countof(initialStr),
				TEXT("ID: %ld\nCapacidade: %u - Velocidade: %u m/s\nPassageiros atuais: %u\nOrigem: %s - Destino: %s\n%s\n\n"),
				mainData->airplanesArray[i].id,
				mainData->airplanesArray[i].capacity,
				mainData->airplanesArray[i].velocity,
				mainData->airplanesArray[i].currentPassengers,
				mainData->airplanesArray[i].srcAirport,
				mainData->airplanesArray[i].destAirport,
				mainData->airplanesArray[i].stopped ? TEXT("Encontra-se parado") : TEXT("Encontra-se em movimento")
			);

			length = GetWindowTextLength(hWndList) + _tcslen(initialStr) + 1;

			TCHAR* buf = malloc(length * sizeof(TCHAR));

			GetWindowText(hWndList, buf, length);
			_tcscat_s(buf, length, initialStr);
			SetWindowText(hWndList, buf); // Mostra no static text a informação final

			free(buf);
		}
		LeaveCriticalSection(mainData->criticalSectionAirplanes);

		break;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK_FECHAR) EndDialog(hWnd, 0);
		break;

	case WM_CLOSE:
		EndDialog(hWnd, 0);
		return TRUE;
	}

	return FALSE;
}
// Função callback para listar aeroportos
LRESULT CALLBACK TrataEventosListarAirports(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	HWND auxhWnd = GetParent(hWnd); // GetParent vai servir para depois conseguir ir buscar a struct declarada no main e nao usar vars globais
	pSharedDataStruct mainData = (pSharedDataStruct)GetWindowLongPtr(auxhWnd, GWLP_USERDATA);

	switch (messg) {
	case WM_INITDIALOG:
		SetWindowText(hWnd, TEXT("Listar Aeroportos"));

		int length;
		TCHAR initialStr[BUFFER_SIZE];

		RECT tempRect;
		GetWindowRect(hWnd, &tempRect);

		// Cria a janela com o static text
		HWND hWndList = CreateWindow(
			TEXT("STATIC"),
			TEXT("\0"),
			WS_VISIBLE | WS_CHILD | SS_CENTER,
			CW_USEDEFAULT,
			CW_USEDEFAULT,
			tempRect.right - tempRect.left - 20,
			tempRect.bottom - tempRect.top - 100,
			hWnd,
			NULL,
			hInstance,
			NULL
		);

		EnterCriticalSection(mainData->criticalSectionAirports);
		// Percorre o array de aeroportos e guarda a informação na initialStr
		for (unsigned int i = 0; i < *mainData->nrAirports; i++) {
			_stprintf_s(
				initialStr,
				_countof(initialStr),
				TEXT("Nome: %s\nPosição: (%i, %i)\n\n"),
				mainData->airportsArray[i].name,
				mainData->airportsArray[i].coordinates.x,
				mainData->airportsArray[i].coordinates.y
			);

			length = GetWindowTextLength(hWndList) + _tcslen(initialStr) + 1;

			TCHAR* buf = malloc(length * sizeof(TCHAR));

			GetWindowText(hWndList, buf, length);
			_tcscat_s(buf, length, initialStr);
			SetWindowText(hWndList, buf);

			free(buf);
		}
		LeaveCriticalSection(mainData->criticalSectionAirports);

		break;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK_FECHAR) EndDialog(hWnd, 0);
		break;

	case WM_CLOSE:
		EndDialog(hWnd, 0);
		return TRUE;
	}

	return FALSE;
}

// Função callback para adicionar aeroportos
LRESULT CALLBACK TrataEventosAddAirport(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	HWND auxhWnd = GetParent(hWnd); // GetParent vai servir para depois conseguir ir buscar a struct declarada no main e nao usar vars globais
	pSharedDataStruct mainData = (pSharedDataStruct)GetWindowLongPtr(auxhWnd, GWLP_USERDATA);

	TCHAR airportName[BUFFER_SIZE], temp[BUFFER_SIZE];
	int coordX, coordY;
	BOOL ret;

	switch (messg) {
	case WM_COMMAND:
		// Se o utilizador carregar no OK
		if (LOWORD(wParam) == IDOK_ADD_AIRPLANE) {
			// Vamos buscar o nome e as coordenadas
			GetDlgItemText(hWnd, IDC_EDIT_AIRPORT_NAME, airportName, _countof(airportName));

			GetDlgItemText(hWnd, IDC_EDIT_AIRPORT_X, temp, _countof(temp));

			if (!isStringANumber(temp)) {
				break;
			}

			coordX = _tstoi(temp);

			GetDlgItemText(hWnd, IDC_EDIT_AIRPORT_Y, temp, _countof(temp));

			if (!isStringANumber(temp)) {
				break;
			}

			coordY = _tstoi(temp);

			// Chamamos a funcao de adicionar aeroportos
			EnterCriticalSection(mainData->criticalSectionAirports);
			ret = caeroporto(airportName, coordX, coordY, mainData->airportsArray, mainData->airportsSharedArray, mainData->maxAirports, mainData->nrAirports);
			LeaveCriticalSection(mainData->criticalSectionAirports);

			// Se a funcao retornar TRUE, significa que tudo correu bem
			if (ret) {
				MessageBox(hWnd, TEXT("Aeroporto criado com sucesso!"), TEXT("SUCESSO"), MB_OK | MB_ICONINFORMATION);

				// Força WM_PAINT na janela principal
				RECT tempRect;
				GetClientRect(mainData->hMainWnd, &tempRect);

				tempRect.right -= tempRect.right - (tempRect.left + MAP_OFFSET + MAP_SIDE + MAP_OFFSET + BITMAP_SIZE);

				InvalidateRect(mainData->hMainWnd, &tempRect, FALSE);

				EndDialog(hWnd, 0);
				break;
			}

			MessageBox(hWnd, TEXT("Erro a criar o aeroporto!"), TEXT("ERRO"), MB_OK | MB_ICONERROR);

			EndDialog(hWnd, 0);
			break;
		}

		if (LOWORD(wParam) == IDCANCEL_ADD_AIRPLANE) {
			EndDialog(hWnd, 0);
			return TRUE;
		}
		break;

	case WM_CLOSE:
		EndDialog(hWnd, 0);
		return TRUE;
	}

	return FALSE;
}

// Função callback para ativar/suspender a entrada de aviões
LRESULT CALLBACK TrataEventosSusAirplanes(HWND hWnd, UINT messg, WPARAM wParam, LPARAM lParam) {
	HWND auxhWnd = GetParent(hWnd); // GetParent vai servir para depois conseguir ir buscar a struct declarada no main e nao usar vars globais
	pSharedDataStruct mainData = (pSharedDataStruct)GetWindowLongPtr(auxhWnd, GWLP_USERDATA);

	BOOL isActive = TRUE;

	switch (messg) {
	case WM_INITDIALOG:
		isActive = TRUE;

		// Se passar o TIME_OUT fica a falso
		if (WaitForSingleObject(mainData->hEventActivateSuspend, 50) == WAIT_TIMEOUT) isActive = FALSE;

		// Mostra se está ou nao aceitar novos aviões inicialmente
		SetDlgItemText(hWnd,
			IDC_TEXT,
			isActive ? TEXT("De momento está a aceitar novos aviões") : TEXT("De momento não está a aceitar novos aviões")
		);
		break;

	case WM_COMMAND:
		// Se o utilizador carregar no botão para mudar o estado, metemos o isActive a true
		if (LOWORD(wParam) == IDC_BUTTON_CHANGE_STATE) {
			isActive = TRUE;

			if (WaitForSingleObject(mainData->hEventActivateSuspend, 50) == WAIT_TIMEOUT) isActive = FALSE;

			// Se esta ativo damos reset ao evento para nao aceitar nenhum avião
			if (isActive) {
				ResetEvent(mainData->hEventActivateSuspend);
				// Se nao está ativo damos set ao evento para aceitarmos aviões
			}
			else {
				SetEvent(mainData->hEventActivateSuspend);
			}
			// Mostra se está ou nao aceitar novos aviões depois de mudar o estado do botão
			SetDlgItemText(hWnd,
				IDC_TEXT,
				!isActive ? TEXT("De momento está a aceitar novos aviões") : TEXT("De momento não está a novos aviões")
			);
			break;
		}
		break;

	case WM_CLOSE:
		EndDialog(hWnd, 0);
		return TRUE;
	}

	return FALSE;
}