#include <stdio.h>
#define N 10

void procura_dupla(int *tab, int tam, int *prim, int *seg){
    int i;

    *prim = *tab;
    *seg = *(tab + 1);
/* código inicial
    for(i = 0; i < tam; i++){
        if(*(tab + i) > *prim)
            *prim = *(tab + i);
    }

    *seg = *tab;

    for(i = 0; i < tam; i++){
        if(*(tab + i) > *seg && *(tab + i) != *prim)
            *seg = *(tab + i);
    }
*/

    for(i = 0; i < tam; i++){
        if(*(tab + i) > *prim){
            *seg = *prim;
            *prim = *(tab + i);
        }
        else if(*(tab + i) > *seg)
            *seg = *(tab + i);
    }
}

void main(){
    int v[N] = {1, 90, 4, 76, 8, 10, 76, 20, 70, 69}, i, first, second;

    for(i = 0; i < N; i++)
        printf("%i |", v[i]);

    procura_dupla(v, N, &first, &second);
    printf("\n\n");

    printf("Maior: %i\n", first);
    printf("Segundo maior: %i", second);
}