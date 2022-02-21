#include "setup.h"

#include "controlThreads.h"
#include "structs.h"

HANDLE initialSetup(pOldMainThreadStruct thisStruct) {
	HANDLE hReturnThreadHandle;

	// Evento que ativa e suspende aceitação de aviões novos (reset manual e começa com o evento assinalado)
	thisStruct->hEventActivateSuspend = CreateEvent(
		NULL,
		TRUE,
		TRUE,
		EVENT_ACTIVATE_SUSPEND_NAME
	);

	if (thisStruct->hEventActivateSuspend == NULL) return INVALID_HANDLE_VALUE;

	thisStruct->hSetupFinishedEvent = CreateEvent(
		NULL,
		TRUE,
		FALSE,
		NULL
	);

	if (thisStruct->hSetupFinishedEvent == NULL) return INVALID_HANDLE_VALUE;
	// Cria a thread para a antiga main, main esta que inicializa todas as outras threads
	hReturnThreadHandle = CreateThread(
		NULL,
		0,
		oldMainThread,
		thisStruct,
		0,
		NULL
	);

	if (hReturnThreadHandle == NULL) return INVALID_HANDLE_VALUE;

	return hReturnThreadHandle;
}