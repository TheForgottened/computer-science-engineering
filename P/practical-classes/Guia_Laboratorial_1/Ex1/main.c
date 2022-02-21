#include <stdio.h>

void main(){
    int a, b, total, *p = &a, *q = &b, *r = &total;

    printf("Introduza o valor de a: ");
    scanf("%i", p);

    printf("Introduza o valor de b: ");
    scanf("%i", q);

    *r = *p + *q;

    printf("\na = %i | b = %i | total = %i", a, b, total);    
}