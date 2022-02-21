#include <stdio.h>
#include "ponto.h"

int main(int argc, char** argv) {
    
    ponto2D p1 = {1,3}, p2;
    
    initPonto(&p2);
    
    printPonto(p1);
    
    printPonto(p2);
    
    movePonto(&p1, 3, -2);
    printPonto(p1);

    return 0;
}