#ifndef SETUP_H
#define SETUP_H

#include <windows.h>

#include "structs.h"

// Devolve handle para thread que trata de toda a lógica
// Em caso de erro, devolve INVALID_HANDLE_VALUE
HANDLE initialSetup(pOldMainThreadStruct thisStruct);

#endif
