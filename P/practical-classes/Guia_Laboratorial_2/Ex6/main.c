#include <stdio.h>
#include "agenda.h"

void main(){
    cont *agenda = NULL;
    int n = 0;

    printf("n = %i", n);
    agenda = novoContacto(agenda, &n);
    printf("n = %i", n);
    printAgenda(agenda, n);
    agenda = novoContacto(agenda, &n);
    agenda = novoContacto(agenda, &n);
    agenda = novoContacto(agenda, &n);

    printAgenda(agenda, n);
}