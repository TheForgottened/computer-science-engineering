#ifndef RETANGULO_H_INCLUDED
    #include "ponto.h"

    #define RETANGULO_H_INCLUDED

    typedef struct retangulo Retangulo;
    struct retangulo{
        ponto2D cie;

        int altura;
        int largura;
    };

    void algo(Retangulo r);
    void initR(Retangulo *r);
    int area(Retangulo r);
    int pontoInRetangulo(ponto2D p, Retangulo r);
    int addRetangulo(int tam, Retangulo v[], int *pl);
    void printR(Retangulo v[], int n);
    void deleteR(Retangulo v[], int *n);
#endif