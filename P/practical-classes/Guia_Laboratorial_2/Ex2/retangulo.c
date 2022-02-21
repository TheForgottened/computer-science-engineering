#include <stdio.h>
#include "retangulo.h"

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

    printf("Altura do retangulo: ");
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