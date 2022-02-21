#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "agenda.h"

cont* novoContacto(cont *a, int *n){
    char name[TNAME];

    printf("Nome do novo contacto: ");

    scanf(" %19[^\n]", name);

    if (indiceContacto(name, *n, a) == -1) {
        cont *agendaTemp;
        agendaTemp = (cont*) realloc(a, (*n + 1) * sizeof(cont));        

        if(agendaTemp != NULL) {
            a = agendaTemp;
            strcpy(a[*n].name, name);

            do {
                printf("Numero do contacto: ");
                scanf("%i", &(a[*n].number));
            } while (a[*n].number < 900000000 || a[*n].number > 999999999);

            (*n)++;
        } else
            printf("Erro de alocacao de memoria! Nao foi possivel adicionar o contacto.");

    } else {
        printf("Esse contacto ja existe na agenda!");
    }

    return a;
}

int indiceContacto(char *name, int tam, cont *a){
    int pos = -1, i;

    for (i = 0; i < tam; i++){
        printf("strcmp = %i", strcmp(name, a[i].name));

        if (strcmp(name, a[i].name) == 0){
            pos = i;
            return pos;
        }
    }

    printf("pos = %i", pos);
    return pos;
}

void printAgenda(cont *a, int tam){
    int i;

    for(i = 0; i < tam; i++){
        printf("%s: %i\n", a[i].name, a[i].number);
    }
}

cont* delContacto(cont *a, int *tam, char *name){
    int pos;
    pos = indiceContacto(name, *tam, a);

    if (pos >= 0){
        cont aux, *agendaTemp;
        aux = a[pos];
        a[pos] = a[*tam - 1];
        a[*tam - 1] = aux;

        agendaTemp = (cont*) realloc (a, (*tam - 1) * sizeof(cont));
        if (agendaTemp != NULL) {
            a = agendaTemp;
            (*tam)--;
        } else 
            printf("Erro de alocacao de memoria! Nao foi possivel remover o contacto.");
    } else {
        printf("Contacto %s nao existe.\n", name);
    }

    return a;
}