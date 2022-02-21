#include <stdio.h>

void rotation(float *a, float *b, float *c){
    float temp = *a;

    *a = *c;
    *c = *b;
    *b = temp;
}

void main(){
    float x, y, z;

    printf("Insira 3 numeros: ");
    scanf("%f %f %f", &x, &y ,&z);

    rotation(&x, &y, &z);

    printf("\n%.2f %.2f %.2f", x, y, z);
}