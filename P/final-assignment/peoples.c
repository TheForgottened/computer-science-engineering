#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "peoples.h"
#include "environments.h"

nperson insertFinal(nperson p, nperson new) {
    nperson temp;

    if (p == NULL)
        p = new;
    else {
        temp = p;

        new->next = temp;
        p = new;

        /*while (temp->next != NULL)
            temp = temp->next;

        temp->next = new;*/
    }

    return p;
}

nperson readPeople(char *fname) {
    FILE *f;

    nperson v = NULL, temp;
    int age, sickD;
    char name[100];
    char state;

    f = fopen(fname, "rt");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return v;
    }

    while (fscanf(f, "%99s %i %c", name, &age, &state) == 3) {
        temp = malloc(sizeof(person));

        if (temp != NULL) {
            sickD = 0;

            strcpy(temp->name, name);
            temp->age = age;
            temp->state = state;
            temp->next = NULL;

            if (temp->state == 'D')
                fscanf(f, " %i ", &sickD);
            
            temp->sickD = sickD;
            temp->idlocal = 0;

            v = insertFinal(v, temp);
        } else {
            printf("Erro na alocacao de memoria!\n\n");
            freePeople(v);
            return NULL;
        }
    }

    fclose(f);
    return v;
}

void printPeople(nperson p) {
    while (p != NULL) {
        printf("\nNome: %s.\nIdade: %i.\nEstado: %c.\nDias Doente: %i.\nID do Local: %i.\n", p->name, p->age, p->state, p->sickD, p->idlocal);

        p = p->next;
    }
}

nperson checkPeople(nperson p) {
    int i;
    nperson temp = p, temp1;

    while (temp != NULL) {
        if (temp->age <= 0) {
            printf("A pessoa %s tem idade invalida!", temp->name);
            return NULL;
        }
        if (temp->state != 'S' && temp->state != 'D' && temp->state != 'I') {
            printf("A pessoa %s tem um estado invalido!", temp->name);
            return NULL;
        }
        if (temp->state == 'D' && temp->sickD <= 0) {
            printf("A pessoa %s esta doente e o numero de dias e invalido!", temp->name);
            return NULL;
        }

        temp1 = temp->next;

        while (temp1 != NULL) {
            if (temp->name == temp1->name) {
                printf("O nome %s aparece mais que uma vez!", temp->name);
                return NULL;
            }

            temp1 = temp1->next;
        }

        temp = temp->next;
    }

    return p;
}

void freePeople(nperson p) {
    nperson temp;

    while (p != NULL) {
        temp = p;
        p = p->next;

        free(temp);
    }
}
nperson createEqual(nperson o) {
    nperson d = NULL, temp;

    while (o != NULL) {
        temp = malloc(sizeof(person));

        if (temp != NULL) {
            *temp = *o;
            temp->next = NULL;

            d = insertFinal(d, temp);

            o = o->next;
        } else {
            printf("Erro na alocacao de memoria!\n\n");
            freePeople(d);
            return NULL;
        }
    }

    return d;
}

void savePeople(nperson p, char *fname) {
    FILE *f;

    f = fopen(fname, "wt");

    if (f == NULL) {
        printf("Erro na criacao do ficheiro %s!\n", fname);
        return;
    }

    while (p != NULL) {
        fprintf(f, "%s\t%i\t%c", p->name, p->age, p->state);

        if (p->state == 'D')
            fprintf(f, "\t%i", p->sickD);

        fprintf(f, "\n");
        
        p = p->next;
    }

    fclose(f);
}