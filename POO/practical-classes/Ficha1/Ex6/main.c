#include <stdio.h>

void troca(int *a, int *b);

int main(){
    int a = 5, b = 10;

    troca(&a, &b);

    printf("a = %i\nb = %i", a, b);
}

void troca(int *a, int *b){
    int n;

    n = *a;
    *a = *b;
    *b = n;

    return;
}