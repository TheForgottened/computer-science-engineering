#include <stdio.h>
#define N 10

void f(int *t, int tam, int *np, int *ni, int *maior, int *pos){
    int i;

    *maior = *t;

    for(i = 0; i < tam; i++){
        if(*(t + i) % 2 == 0)
            *np += 1;
        else
            *ni += 1;
        
        if(*(t + i) > *maior){
            *maior = *(t + i);
            *pos = i + 1;
        }
    }
}

void main(){
    int v[N] = {1, 2, 4, 76, 8, 10, 19, 20, 70, 69}, i, nump, numi, big, posicao;

    for(i = 0; i < N; i++)
        printf("%i |", v[i]);
    
    f(v, N, &nump, &numi, &big, &posicao);
    printf("\n\n");

    printf("Pares = %i\n", nump);
    printf("Impares = %i\n", numi);
    printf("Maior = %i\n", big);
    printf("Posicao do maior = %i\n\n", posicao);
}