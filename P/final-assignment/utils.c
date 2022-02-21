// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "utils.h"

void initRandom(){
    srand(time(NULL));
}

int intUniformRnd(int a, int b){
    return a + rand()%(b - a + 1);
}

int probEvento(float prob){
    return prob > ((float)rand()/RAND_MAX);
}


// Função main () com alguns exemplos simples de utilizacao das funcoes
/* 
int main(){

  int i;

    initRandom();   // esta função só deve ser chamada uma vez

    printf("10 valores aleatorios uniformes entre [4, 100]:\n");
    for(i = 0; i < 10; i++)
        printf("%d\n", intUniformRnd(4, 100));

    printf("Probabilidade 0.25 de um evento suceder: \n");
    for(i = 0; i < 10; i++)
        printf("%d\n", probEvento(0.25));

    return 0;
}
*/