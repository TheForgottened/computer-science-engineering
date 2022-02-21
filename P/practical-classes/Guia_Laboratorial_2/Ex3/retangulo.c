#include <stdio.h>
#include "retangulo.h"

int addRetangulo(int tam, Retangulo v[], int *pl){
    if(*pl < tam){
        initR(&(v[*pl]));
        (*pl)++;

        return 1;
    }
    else {
        return 0;
    }
}

void deleteRunder(Retangulo v[], int *n, int a){
    int i = 0;

    while(i < *n){
        if(area(v[i]) < a){
            v[i] = v[*n - 1];
            (*n)--;
        }
        else   
            i++;
    }
}

void deleteR(Retangulo v[], int *n){
    if(*n == 0){
        printf("Tabela vazia!");
        return;
    }

    int i = 0, menorA = area(v[i]), menorR;

    for(i = 1; i < *n; i++){
        if(area(v[i]) < menorA){
            menorA = area(v[i]);
            menorR = i;
        }
    }

    v[i] = v[*n - 1];

    (*n)--;
}

void printR(Retangulo v[], int n){
    if(n == 0){
        printf("Tabela vazia!");
        return;
    }

    int i;

    for(i = 0; i < n; i++){
        algo(v[i]);
    }
}

void algo(Retangulo r){
    // ponto2D cse = r.cie, csd = r.cie, cid = r.cie;

    printf("(%3i, %3i)\t(%3i, %3i)\n", r.cie.x, r.cie.y + r.altura, r.cie.x + r.largura, r.cie.y + r.altura);
    printf("(%3i, %3i)\t(%3i, %3i)\n", r.cie.x, r.cie.y, r.cie.x + r.largura, r.cie.y);
}

void initR(Retangulo *r){
    printf("Coordenada x do canto inferior esquerdo: ");
    scanf("%i", &r->cie.x);

    printf("Coordenada y do canto inferior esquerdo: ");
    scanf("%i", &r->cie.y);

    printf("\nAltura do retangulo: ");
    scanf("%i", &r->altura);

    printf("Largura do retangulo: ");
    scanf("%i", &r->largura);
}

int area(Retangulo r){
    return r.altura * r.largura;  
}

int pontoInRetangulo(ponto2D p, Retangulo r){
    if(p.x > r.cie.x && p.y > r.cie.y && p.x < (r.cie.x + r.largura) && p.y < (r.cie.y + r.altura))
        return 1;
    else
        return 0;    
}