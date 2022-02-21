#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"

#define TAM 10

int main(int argc, char** argv) {
    Retangulo tab[TAM];
    int proxLivre = 0;
/*
    if(addRetangulo(TAM, tab, &proxLivre)){
        printf("\nExiste %i retangulos na tabela.\n", proxLivre);
    }
    else {
        printf("Tabela cheia!");
    }
*/
    printR(tab, proxLivre);

    return 0;
}