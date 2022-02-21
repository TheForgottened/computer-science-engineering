#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "peoples.h"
#include "environments.h"
#include "utils.h"
#include "simulation.h"

void main() {
    initRandom();

    char fEnv[100], fPeople[100];
    int envSize, choice, day = 1, back = 0;

    local *env;
    nperson nextperson = NULL, nextperson1 = NULL, nextperson2 = NULL, nextperson3 = NULL;

    printf("*** FASE DE PREPARACAO ***\n\n");

    printf("Nome do ficheiro binario de espaco (incluir extensao): ");
    scanf("%99s", fEnv);

    env = readEnv(fEnv, &envSize);
    env = checkEnv(env, envSize);

    if (env == NULL)
        exit(0);

    printf("Nome do ficheiro de texto das pessoas (incluir extensao): ");
    scanf("%99s", fPeople);

    nextperson = readPeople(fPeople);
    nextperson = checkPeople(nextperson);

    if (nextperson == NULL) {
        exit(0);
    }

    assignLocal(nextperson, env, envSize);
    system("cls");

    printf("\n*** FASE DE SIMULACAO ***");

    do {
        printf("\n\n");
        choice = menuSim(day, back);

        switch (choice) {
            case 1: // Avancar uma iteracao na simulacao
                switch (back) {
                    case 0: // quando não tem nenhuma lista guardada
                        nextperson1 = createEqual(nextperson);
                        back++;
                        break;
                    
                    case 1: // quando tem 1 lista guardada
                        nextperson2 = createEqual(nextperson1);
                        freePeople(nextperson1);
                        nextperson1 = createEqual(nextperson);

                        back++;
                        break;
                    case 2: // quando tem 2 listas guardadas
                        nextperson3 = createEqual(nextperson2);
                        freePeople(nextperson2);
                        nextperson2 = createEqual(nextperson1);
                        freePeople(nextperson1);
                        nextperson1 = createEqual(nextperson);

                        back++;
                        break;
                    
                    case 3: // quando tem 3 listas guardadas
                        nextperson3 = createEqual(nextperson2);
                        freePeople(nextperson2);
                        nextperson2 = createEqual(nextperson1);
                        freePeople(nextperson1);
                        nextperson1 = createEqual(nextperson);
                        break;
                }
                
                nextDay(nextperson, env, envSize, day);

                day++;
                break;
                
            case 2: // Apresentar estatistica
                showStatistics(nextperson, env, envSize);
                break;

            case 3: // Adicionar doente
                insertSFinal(nextperson, env, envSize);
                break;

            case 4: // Transferir pessoas
                movePeople(nextperson, env, envSize);
                break;

            case 5: // Voltar atras X iteracoes (maximo 3)
                nextperson = goBack(nextperson, &nextperson1, &nextperson2, &nextperson3, &day, &back);
                break;

            case 6: // Terminar simulacao
                finishSim(nextperson, env, envSize, day, fPeople, fEnv);
                break;
        }
    } while (choice != 6);

    freePeople(nextperson);
    freePeople(nextperson1);
    freePeople(nextperson2);
    freePeople(nextperson3);

    free(env);

    printf("\n\n*** PROGRAMA CONCLUIDO ***");
}