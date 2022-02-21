#include <stdio.h>
#include <stdlib.h>

#define LINES 5

int brincadeirinha(int size, int a[][3]);
void printer(int size, int a[][3]);

void main(){
    int matriz[LINES][3];
    brincadeirinha(LINES, matriz);
    printer(LINES, matriz);
}

int brincadeirinha(int size, int a[][3]){
    int valor, i, j;

    for(i = 0; i < size; i++){
        do {
            printf("Valor para a linha %i: ", i + 1);
            scanf("%i", &valor);

            for(j = 0; a[j][0] != valor && j <= i; j++)
                ;
        } while (valor < 0 || valor > 100 || j != (i + 1));

        a[i][0] = valor;
        a[i][1] = valor * valor;
        a[i][2] = valor * valor * valor;
    }
}

void printer(int size, int a[][3]){
    int i;

    printf("\n\n");

    for(i = 0; i < size; i++){
        printf("%i\t|%i\t|%i", a[i][0], a[i][1], a[i][2]);
        printf("\n");
    }
}