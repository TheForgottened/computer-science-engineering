#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"

// alinea b)
void printPonto(ponto2D a){
    printf("Ponto: (%d,%d)\n", a.x, a.y);
}

// alinea c)
void initPonto(ponto2D* p){
    printf("Coordenada x: ");
    scanf("%i", &(p->x));

    printf("Coordenada y: ");
    scanf("%i", &(p->y));
}

// alinea d)
void movePonto(ponto2D* p, int dx, int dy){
    p->x = p->x + dx;
    p->y = p->y + dy;
}