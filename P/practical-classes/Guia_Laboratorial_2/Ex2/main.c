#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"

int main(int argc, char** argv) {
    Retangulo rt = {{0, 0}, 3, 5}, user;
    ponto2D p = {1,2};
    algo(rt);

    initR(&user);
    algo(user);

    printf("%i", pontoInRetangulo(p, rt));
    
    return 0;
}