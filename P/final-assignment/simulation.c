#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "simulation.h"
#include "peoples.h"
#include "environments.h"
#include "utils.h"

int menuSim(int d, int b) {
    int c;

    printf("Dia: %i\n\n", d);
    printf("1. Avancar uma iteracao na simulacao\n");
    printf("2. Apresentar estatistica\n");
    printf("3. Adicionar doente\n");
    printf("4. Transferir pessoas\n");
    printf("5. Voltar atras X iteracoes (maximo %i)\n", b);
    printf("6. Terminar simulacao\n");

    do {
        printf("Opcao pretendida: ");
        scanf("%i", &c);
        
        if (c < 1 || c > 6)
            printf("Opcao invalida!\n\n");
    } while (c < 1 || c > 6);
    
    printf("\n");
    return c;
}

void assignLocal(nperson p, local *e, int eSize) {
    nperson temp = p, temp2;
    int total = 0, i, totalCapacity = 0, random;

    for (i = 0; i < eSize; i++)
        totalCapacity += e[i].capacidade;

    i = 0;

    while (temp != NULL && i < totalCapacity) {
        do {
            temp2 = p;
            total = 0;
            
            random = intUniformRnd(0, eSize - 1);
            temp->idlocal = e[random].id;

            while (temp2 != NULL) {
                if (temp2->idlocal == e[random].id)
                    total++;
                
                temp2 = temp2->next;
            }
        } while (total > e[random].capacidade);

        i++;

        if (i == totalCapacity) {
            freePeople(temp->next);
            temp->next = NULL;
        }

        temp = temp->next;
    }
}

void showStatistics(nperson p, local *e, int eSize) {
    int i, S, D, diasD, I, total, Stotal = 0, Dtotal = 0, Itotal = 0, diasDtotal = 0, Ttotal = 0;
    nperson temp;

    for (i = 0; i < eSize; i++) {
        printf("Local ID = %i:\n\n", e[i].id);

        temp = p;

        S = 0;
        D = 0;
        diasD = 0;
        I = 0;
        total = 0;

        while (temp != NULL) {
            

            if (temp->idlocal == e[i].id) {
                printf("N: %s\tI: %i\tE: %c", temp->name, temp->age, temp->state);

                if (temp->state == 'D') {
                    printf("\tDias: %i", temp->sickD);
                }

                printf("\n\n");

                if (temp->state == 'S') {
                    S++;
                }

                if (temp->state == 'D') {
                    D++;
                    diasD += temp->sickD;
                }

                if (temp->state == 'I')
                    I++;

                total++;
            }

            temp = temp->next;
        }
        
        if (total != 0) {
            printf("Numero / taxa de saudaveis: %i / %.2f%%\n", S, ((S / (float)total) * 100));

            printf("Numero / taxa de doentes: %i / %.2f%%\t", D, ((D / (float)total) * 100));
            
            if (D != 0)
                printf("Media de dias doente: %.2f", (diasD / (float)D));
            
            printf("\n");
            printf("Numero / taxa de imunes: %i / %.2f%%\n", I, ((I / (float)total) * 100));
        }

        printf("Numero de pessoas: %i\n\n\n", total);

        Stotal += S;
        Dtotal += D;
        diasDtotal += diasD;
        Itotal += I;
        Ttotal += total;
    }

    printf("Espaco:\n\n");
    
    if (Ttotal != 0) {
        printf("Numero / taxa de saudaveis: %i / %.2f%%\n", Stotal, ((Stotal / (float)Ttotal) * 100));
        printf("Numero / taxa de doentes: %i / %.2f%%\t", Dtotal, ((Dtotal / (float)Ttotal) * 100), (diasDtotal / (float)Dtotal));

        if (Dtotal != 0)
            printf("Media de dias doente: %.2f", (diasDtotal / (float)Dtotal));

        printf("\n");
        printf("Numero / taxa de imunes: %i / %.2f%%\n", Itotal, ((Itotal / (float)Ttotal) * 100));
    }

    printf("Numero de pessoas: %i\n\n\n", Ttotal);
}

void movePeople(nperson p, local *e, int eSize) {
    int n, pTotalO = 0, pTotalD = 0, idO, idD, indexD, b = 0, i, j = 0;
    nperson pTemp = p;

    for (i = 0; i < eSize; i++)
        j += e[i].capacidade;

    i = 0;
    
    while (pTemp != NULL) {
        i++;
        pTemp = pTemp->next;
    }

    if (j == i) {
        printf("O espaco esta cheio, e impossivel mover pessoas!\n\n");
        return;
    }

    pTemp = p;

    do {
        printf("Local de origem: ");
        scanf("%i", &idO);

        for (i = 0; i < eSize; i++) {
            if (e[i].id == idO) {
                b = 1;
                i = eSize;
            }
        }

        if (b == 0)
            printf("Local nao existe!\n\n");
    } while (b != 1);

    do {
        printf("Local de destino: ");
        scanf("%i", &idD);

        for (i = 0; i < eSize; i++) {
            if (e[i].id == idD) {
                for (j = 0; j < 3; j++) {
                    if (e[i].liga[j] == idO && e[i].liga[j] != -1) {
                        j = 3;
                        b = 1;
                    } else {
                        b = 3;
                    }
                }

                indexD = i;
                i = eSize;
            } else 
                b = 2;
        }

        if (b == 2)
            printf("Local nao existe!\n\n");
        
        if (b == 3)
            printf("O local origem de ID = %i nao esta ligado com o local destino de ID = %i!\n\n", idO, idD);
    } while (b != 1);

    while (pTemp != NULL) {
        if (pTemp->idlocal == idO)
            pTotalO++;
        if (pTemp->idlocal == idD)
            pTotalD++;

        pTemp = pTemp->next;
    }  

    if (pTotalO == 0) {
        printf("O local de origem não tem pessoas para mover!\n\n");
        return;
    }

    if (pTotalD == e[indexD].capacidade) {
        printf("O local de destino já está cheio!\n\n");
        return;
    }

    do {
        printf("Numero de pessoas que pretende mover: ");
        scanf("%i", &n);

        if (n <= 0)
            printf("Deve mover 1 ou mais pessoas!\n\n");
            
        if (n > pTotalO)
            printf("Nao pode mover mais pessoas do que aquelas que o local origem possui!\n\n");
    } while (n <= 0 || n > pTotalO);

    pTemp = p;

    if (n == pTotalO) {
        while (pTemp != NULL) {
            if (pTemp->idlocal == idO)
                pTemp->idlocal = idD;

            pTemp = pTemp->next;
        }       
    } else {
        for (i = 0; i < n; ) {
            pTemp = p;

            while (pTemp != NULL) {
                j = probEvento(0.5);

                if (j == 1 && pTemp->idlocal == idO && i < n) {
                    pTemp->idlocal = idD;
                    i++;
                }

                pTemp = pTemp->next;
            }
        }
    }
}

nperson goBack(nperson current, nperson *one, nperson *two, nperson *three, int *day, int *back) {
    int n;

    do {
        printf("Numero de iteracoes que pretende recuar: ");
        scanf("%i", &n);

        if (n < 0)
            printf("O numero inserido deve ser maior ou igual a 0!\n\n");

        if (n > *back)
            printf("Deve recuar no maximo %i iteracoes!\n\n", *back);
    } while (n < 0 || n > *back);

    if (n == 0)
        return current;

    freePeople(current);

    switch (n) {
        case 1:
            current = *one;
            *one = *two;
            *two = *three;
            *three = NULL;

            *day -= 1;
            *back -= 1;
            break;
        
        case 2:
            current = *two;
            *one = *three;
            *two = NULL;
            *three = NULL;
            
            *day -= 2;
            *back -= 2;
            break;
        
        case 3:
            current = *three;
            *one = NULL;
            *two = NULL;
            *three = NULL;

            *day -= 3;
            *back -= 3;
            break;
    }

    return current;
}

void nextDay(nperson p, local *e, int eSize, int day) {
    int i, j, total, sick, random;
    nperson temp = p;

    while (temp != NULL) {
        if (temp->state == 'D') {
            random = probEvento(1 / (float)(temp->age));

            if (random == 1 || temp->sickD >= (5 + (temp->age / 10))) {
                random = probEvento(20 / 100);

                if (random == 1)
                    temp->state = 'I';
                else
                    temp->state = 'S';

                temp->sickD = 0;
            } else
                (temp->sickD)++;          
        }

        temp = temp->next;
    }

    for (i = 0; i < eSize; i++) {
        total = 0;
        sick = 0;
        temp = p;

        while (temp != NULL) {
            if (temp->idlocal == e[i].id) {
                total++;

                if (temp->state == 'D')
                    sick++;
            }

            temp = temp->next;
        }

        total *= (30/100);
        total *= sick;

        for (j = 0; j < total; ) {
            temp = p;

            while (temp != NULL && j < total) {
                random = probEvento(0.5);

                if (temp->idlocal == e[i].id && random == 1) {
                    if (temp->state != 'D' && temp->state != 'I') {
                        temp->state = 'D';
                        temp->sickD = 1;
                    }

                    j++;
                }
            }
        }
    }
}

nperson insertSFinal(nperson p, local *e, int eSize) {
    nperson new, temp;

    new = malloc(sizeof(person));

    if (new == NULL) {
        printf("Erro na alocacao de memoria!\n\n");
        return p;
    }

    fillSick(new, p, e, eSize);

    if (new = NULL) {
        free(new);
        return p;
    }

    if (p == NULL)
        p = new;
    else {
        temp = p;

        while (temp->next != NULL)
            temp = temp->next;

        temp->next = new;
    }
    return p;
}

void fillSick(nperson new, nperson p, local *e, int eSize) {
    int i, j = 0, index = 0;
    nperson temp = p;

    for (i = 0; i < eSize; i++)
        index += e[i].capacidade;

    i = 0;
    
    while (temp != NULL) {
        i++;
        temp = temp->next;
    }

    if (index == i) {
        printf("O espaco ja esta cheio, e impossivel adicionar um novo doente!\n\n");
        new = NULL;
        return;
    }

    do {
        printf("Nome: ");
        scanf("%99s", &(new->name));

        temp = p;

        while (temp != NULL) {
            if (strcmp(new->name, temp->name) == 0)
                temp = NULL;
            else if (temp->next == NULL) {
                j = 1;
                temp = NULL;
            } else
                temp = temp->next;
        }

        if (j != 1)
            printf("Ja existe uma pessoa com esse nome!\n\n");
    } while (j != 1);

    do {
        printf("Idade: ");
        scanf("%i", &(new->age));

        if (new->age <= 0)
            printf("A idade deve ser superior a 0!\n\n");
    } while (new->age <= 0);

    do {
        printf("Numero de dias infetado: ");
        scanf("%i", &(new->sickD));

        if (new->sickD <= 0)
            printf("A pessoa deve estar doente ha pelo menos 1 dia!\n\n");
    } while (new->sickD <= 0);

    do {
        printf("ID do local onde deve ser inserido: ");
        scanf("%i", &(new->idlocal));

        j = 0;

        for (i = 0; i < eSize; i++) {
            if (e[i].id == new->idlocal) {
                index = i;
                j = 1;
                i = eSize;
            }
        }

        i = 0;

        while (temp != NULL) {
            if (temp->idlocal == new->idlocal)
                i++;

            temp = temp->next;
        }

        if (j != 1)
            printf("Esse local nao existe!\n\n");

        if (j == 1 && i == e[index].capacidade)
            printf("Esse local esta cheio!\n\n");
    } while (j != 1 || i == e[index].capacidade);

    printf("i = %i\tj = %i\tCapacidade = %i", i, j, e[index].capacidade);

    new->state = 'D';

    new->next = NULL;
}

void finishSim(nperson p, local *e, int eSize, int day, char *fPeopleO, char *fEnv) {
    char fPeople[100];
    int i, S, D, diasD, I, total, Stotal = 0, Dtotal = 0, Itotal = 0, diasDtotal = 0, Ttotal = 0;
    nperson temp;
    FILE *stats;

    printf("Nome do ficheiro da populacao (com extensao): ");
    scanf("%99s", fPeople);

    savePeople(p, fPeople);

    stats = fopen("report.txt", "wt");

    fprintf(stats, "Nome do ficheiro de espaco usado: %s\n", fEnv);
    fprintf(stats, "Nome do ficheiro de populacao inicial: %s\n", fPeopleO);
    fprintf(stats, "Nome do ficheiro de populacao final: %s\n", fPeople);
    fprintf(stats, "Numero de dias passados na simulacao: %i\n\n", day);

    for (i = 0; i < eSize; i++) {
        fprintf(stats, "Local ID = %i:\n\n", e[i].id);

        temp = p;

        S = 0;
        D = 0;
        diasD = 0;
        I = 0;
        total = 0;

        while (temp != NULL) {
            if (temp->idlocal == e[i].id) {
                if (temp->state == 'S') {
                    S++;
                }

                if (temp->state == 'D') {
                    D++;
                    diasD += temp->sickD;
                }

                if (temp->state == 'I')
                    I++;

                total++;
            }

            temp = temp->next;
        }
        
        if (total != 0) {
            fprintf(stats, "Numero / taxa de saudaveis: %i / %.2f%%\n", S, ((S / (float)total) * 100));

            if (D != 0)
                fprintf(stats, "Numero / taxa de doentes: %i / %.2f%%\tMedia de dias doente: %.2f\n", D, ((D / (float)total) * 100), (diasD / (float)D));
            else
                fprintf(stats, "Numero / taxa de doentes: %i / %.2f%%\n", D, ((D / (float)total) * 100));
            
            fprintf(stats, "Numero / taxa de imunes: %i / %.2f%%\n", I, ((I / (float)total) * 100));
        }

        fprintf(stats, "Numero de pessoas: %i\n\n\n", total);

        Stotal += S;
        Dtotal += D;
        diasDtotal += diasD;
        Itotal += I;
        Ttotal += total;
    }

    fprintf(stats, "Espaco:\n\n");
    fprintf(stats, "Numero / taxa de saudaveis: %i / %.2f%%\n", Stotal, ((Stotal / (float)Ttotal) * 100));
    fprintf(stats, "Numero / taxa de doentes: %i / %.2f%%\tMedia de dias doente: %.2f\n", Dtotal, ((Dtotal / (float)Ttotal) * 100), (diasDtotal / (float)Dtotal));
    fprintf(stats, "Numero / taxa de imunes: %i / %.2f%%\n", Itotal, ((Itotal / (float)Ttotal) * 100));
    fprintf(stats, "Numero de pessoas: %i\n\n\n", Ttotal);

    fclose(stats);
}