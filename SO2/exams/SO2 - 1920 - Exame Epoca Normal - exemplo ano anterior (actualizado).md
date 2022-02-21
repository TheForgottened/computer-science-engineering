# SO2 - Época Normal 19/20

## Parte 1:

### 1.

Agir a nível da condição "detenção e espera" passaria por obrigar todos os processos a pedir no início da execução tudo o que vão precisar ou então apenas dar direito ao processo de obter recursos se este não possuir nenhum.

Agir a nível da condição "recursos não preemptíveis" passaria por fazer um processo ficar bloqueado à espera do recurso que pediu e perder os que já possuía, ficando à espera destes também ou então se quando um processo pedisse um recurso este fosse detido por um processo bloqueado, o recurso passaria para este processo que acabou de pedir se não este processo ao pedir ficaria bloqueado, podendo perder alguns recursos que já tinha.

Seria mais viável agir a nível da condição "recursos não preemptíveis". 

Imaginando 2 processos que precisem de vários recursos ao longo da sua execução seria inviável, por exemplo, obrigá-los a pedir no início da execução o que vão precisar pois assim impossibilitariam o acesso ao outro processo. 

Imaginando outros 2 processos que precisem de aceder a vários recursos ao mesmo tempo, ao dar-lhes direito de apenas possuir um recurso, tornaria inviável o tratamento da informação corretamente.


### 2.

O estado do instante t é seguro. Caminho possível: P2 -> P4 -> P3 -> P1


### 3.

Isto não é possível. A DLL, ao ser carregada por um processo, irá ser mapeada para a memória do mesmo. Assim, a variável presente na DLL é única àquele processo.


### 4.

```c
typedef struct DADOS_STRUCT dadosStruct, *pDadosStruct;

struct DADOS_STRUCT {
    TCHAR nome[10];
    TCHAR morada[10];
    int telefone;
};
```

Usando a função `SetWindowLongPtr` poderíamos associar à janela que estamos a criar um ponteiro para uma estrutura como a declarada anteriormente. Assim, bastaria usar esta função para todas as janelas ao criá-las, por exemplo `SetWindowLongPtr(hWnd, GWLP_USERDATA, (LONG_PTR)&dados)`. Para depois ter acesso a este ponteiro dentro da função "trataJanela" basta usar a função `GetWindowLongPtr` para obter o ponteiro para a estrutura, por exemplo `pDadosStruct dados = GetWindowLongPtr(hWnd, GWLP_USERDATA)`.

Deste modo, poderíamos ter acesso a uma cópia independente dos dados em todas as janelas sem recurso a variáveis globais.


### 5.

```c
char randomChar = '\0'; // variável que irá conter o caratér random. valor '\0' quando está vazia
int score = 0; // pontuação do jogador

LRESULT CALLBACK trataEventos(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    HDC hdc;
    PAINTSTRUCT ps;
    RECT rect;

    switch (msg) {
        case WM_CREATE:
            break;

        case WM_KEYDOWN:
            if (wParam == VK_SPACE) {
                randomChar = obtemValAleat(65, 90); // valor decimal de todos as letras maiúsculas

                // criar thread que irá usar InvalidateRect de 1 em 1 seg 6 vezes
                break;
            }

            if (wParam == randomChar) {
                score++;

                // fechar a thread que usa InvalidateRect
                randomChar = '\0';
                InvalidateRect(hWnd, NULL, FALSE);
            }

        case WM_PAINT:
            if (randomChar == '\0') break;

            hdc = BeginPaint(hWnd, &ps);

            GetClientRect(hWnd, &rect);

            FillRect(hdc, &rect, CreateSolidBrush(255, 255, 255));

            TextOut(
                hdc, 
                obtemValAleat(rect.left, rect.right), 
                obtemValAleat(rect.top, rect.bottom), 
                &randomChar, 
                1
            );

            EndPaint(hWnd, &ps);
            break;

        case WM_DESTROY:
            // fechar a thread que faz o invalidaterect caso exista uma
            PostQuitMessage(0);
            break;

        default:
            return DefWindowProc(hWnd, msg, wParam, lParam);
            break;
    }
}
```

## Parte 2

### 1.

Para limitar o número de balcões abertos basta apenas usar um semáforo com um máximo de 10 usos. Este semáforo deve ser criado pela agência ao abrir e os balcões ao abrir devem tentar aceder-lhe.

Para a comunicação inicial do balcão com a agência poderia ser usado um espaço de memória único (com tamanho para um inteiro) com nome "MEM_REGISTO". Assim, o balcão ao abrir pedia acesso a um mutex "MUTEX_LEITURA_REGISTO" e assinalava um evento "EVENTO_ESCRITA_REGISTO" para avisar a agência que está à espera de um id. A agência depois assinala um evento "EVENTO_LEITURA_REGISTO" para avisar o balcão de que pode ir ao espaço de memória ler o seu id. O balcão, no fim de ler, liberta o mutex. O mutex é importante para garantir que não há mais que um balcão a ler o espaço de memória.

O resto da comunicação poderia ser realizada num espaço de memória com nome genérico tipo "BALCAO_%i" onde %i seria o id deste. Para além disso teriam de existir eventos genéricos como "EVENTO_ESCRITA_BALCAO_%i" e "EVENTO_LEITURA_BALCAO_%i" onde, novamente, %i seria o id do balcão. Este espaço de memória deve ter o tamanho da seguinte struct:

```c
struct dados {
    TCHAR idVoo[15];
    int lugar;
};
```

A comunicação inicial deverá ser feita numa thread, e depois a comunicação específica deverá ser feita numa thread única a cada balcão.

### 2.
#### a)

```c
DWORD WINAPI threadRegisto(LPVOID lpParam) {
    HANDLE hFileMap;

    HANDLE hEventRegistoEscrita;
    HANDLE hEventRegistoLeitura;

    int* idMemRegisto;

    hFileMap = CreateFileMapping (
        INVALID_HANDLE_VALUE,
        NULL,
        PAGE_READWRITE,
        0,
        sizeof(int),
        TEXT("MEM_REGISTO")
    );

    if (hFileMap == NULL) return -1;

    idMemRegisto = MapViewOfFile(
        hFileMap,
        FILE_MAP_ALL_ACCESS,
        0,
        0,
        0
    );

    if (idMemRegisto == NULL) return -2;

    hEventRegistoEscrita = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        TEXT("EVENTO_ESCRITA_REGISTO")
    );

    if (hEventRegistoEscrita == NULL) return -3;

    hEventRegistoLeitura = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        TEXT("EVENTO_LEITURA_REGISTO")
    );

    if (hEventRegistoLeitura == NULL) return -4;

    while (1) {
        WaitForSingleObject(hEventRegistoEscrita, INFINITE); // esperar que um balcão assinale o evento a dizer que pretende um id

        ResetEvent(hEventRegistoEscrita); 

        *idMemRegisto = ObterIdValido(); // função que deve obter um id válido e disponível

        SetEvent(hEventRegistoLeitura);

        // criação da thread individual do balcão que acabou de entrar
    }

    return 0;
}

// Lançamento da thread

HANDLE hThread = CreateThread(
    NULL,
    0,
    threadRegisto,
    NULL,
    0,
    NULL
);

// Deve ser lançada logo no início do programa de modo a poder aceitar registos o mais depressa possível
```

#### b)

```c
struct dados {
    TCHAR idVoo[15];
    int lugar;
};

DWORD WINAPI threadBalcao(LPVOID lpParam) {
    int id = *(int)lpParam;

    HANDLE hFileMap;

    HANDLE hEventEscrita;
    HANDLE hEventLeitura;
    TCHAR tempStr[32] = TEXT("");

    struct dados *dadosPartilhados;

    _tsprintf_s(tempStr, _countof(tempStr), TEXT("BALCAO_%i"), id);

    hFileMap = CreateFileMapping (
        INVALID_HANDLE_VALUE,
        NULL,
        PAGE_READWRITE,
        0,
        sizeof(struct dados),
        tempStr
    );

    if (hFileMap == NULL) return -1;

    dadosPartilhados = MapViewOfFile(
        hFileMap,
        FILE_MAP_ALL_ACCESS,
        0,
        0,
        0
    );

    if (idMemRegisto == NULL) return -2;

    _tsprintf_s(tempStr, _countof(tempStr), TEXT("EVENTO_ESCRITA_BALCAO_%i"), id);

    hEventEscrita = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        tempStr
    );

    if (hEventRegistoEscrita == NULL) return -3;

    _tsprintf_s(tempStr, _countof(tempStr), TEXT("EVENTO_LEITURA_BALCAO_%i"), id);

    hEventLeitura = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        tempStr
    );

    if (hEventRegistoLeitura == NULL) return -4;

    while (1) {
        WaitForSingleObject(hEventEscrita, INFINITE); 
        ResetEvent(hEventEscrita);

        if (_tcscmp(dadosPartilhados->idVoo, TEXT("desistir")) == 0) {
            _tprintf(TEXT("O balcão %i desistiu!"), id);
            break;
        } 

        if (verificaSePode(dadosPartilhados->idVoo, dadosPartilhados->lugar) == 0) {
            _tcscpy_s(dadosPartilhados->idVoo, _countof(dadosPartilhados->idVoo), TEXT("rejeitado"));
            SetEvent(hEventLeitura);

            _tprintf(TEXT("O balcão %i foi rejeitado!"), id);
            break;
        }

        _tcscpy_s(dadosPartilhados->idVoo, _countof(dadosPartilhados->idVoo), TEXT("aceite"));
        SetEvent(hEventLeitura);

        _tprintf(TEXT("O balcão %i foi aceite!"), id);
    }

    return 0;
}

// Lançamento da thread

HANDLE hThread = CreateThread(
    NULL,
    0,
    threadBalcao,
    &id,
    0,
    NULL
);

// Deve ser lançada no sítio onde está comentado no exercício anterior
```

### 3.
#### a)

```c
struct dados {
    TCHAR idVoo[15];
    int lugar;
};

BOOL CALLBACK trataEventosDialogBox(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    TCHAR voo[15] = TEXT("");
    TCHAR lugarStr[15] = TEXT("");
    int lugarInt = -1;

    HANDLE hFileMap;

    HANDLE hEventEscrita;

    struct dados *dadosPartilhados;

    // tratar da memória partilhada e do o evento

    switch (msg) {
        case WM_INITDIALOG:
            break;

        case WM_COMMAND:
            if (LOWORD(wParam) != IDOK) break;
            
            GetDlgItemText(hWnd, IDC_EDIT_VOO, voo, _countof(voo));
            GetDlgItemText(hWnd, IDC_EDIT_LUGAR, lugarStr, _countof(lugarStr));

            lugarInt = _tstoi(lugar);

            _tcscpy(dadosPartilhados->idVoo, voo);
            dadosPartilhados->lugar = lugarInt;

            SetEvent(hEventEscrita);

            break;

        case WM_DESTROY:
            // fechar a thread que faz o invalidaterect caso exista uma
            EndDialog(hWnd, 0);
            break;

        default:
            return DefWindowProc(hWnd, msg, wParam, lParam);
            break;
    }
}

// Iniciar a DialogBox
DialogBox(NULL, MAKEINTRESOURCE(IDD_DIALOG_DADOS), hWnd, trataEventosDialogBox);
```

#### b)

```c
struct dados {
    TCHAR idVoo[15];
    int lugar;
};

DWORD WINAPI threadComunica(LPVOID lpParam) {
    HWND hMainWnd = (HWND)lpParam;

    HDC hdc;
    PAINTSTRUCT ps;
    RECT rect;
    TCHAR tempStr[254] = TEXT("");

    HANDLE hFileMap;

    HANDLE hEventLeitura;

    struct dados *dadosPartilhados;

    // trata da memória partilhada e do evento

    WaitForSingleObject(hEventLeitura, INFINITE);

    if (_tcscmp(dadosPartilhados->idVoo, TEXT("rejeitado")) == 0) {
        _tcscpy(tempStr, TEXT("Reserva de voo recusada pela agência."));
    }

    if (_tcscmp(dadosPartilhados->idVoo, TEXT("aceite")) == 0) {
        _tcscpy(tempStr, TEXT("Reserva de voo feita com sucesso."));
    }

    GetClientRect(hMainWnd, &rect);

    BeginPaint(hMainWnd, &ps);

    TextOut(
        hdc,
        rect.left + 10,
        rect.bottom - 10,
        tempStr,
        _tcslen(tempStr)
    );

    EndPaint(hMainWnd, &ps);

    return 0;
}
```