#include <iostream>
#include "Fracao.h"


void ex1f4() {
    Fracao a(1,2), b(2), c;

    c =  a * b;
    c = c * 2; // Converte o 2 para Fracao atraves do construtor. Apenas o faz porque o construtor permite fazer uma fracao apenas com um inteiro
    // c = 2 * c;
    
    //c *= 2; // Tem de ser definido a parte pois trata-se de outro operador

    //c = operator*(a,b);

    cout << a.getAsString() << endl;
    cout << b.getAsString() << endl;
    cout << c.getAsString() << endl;
}

int main () {
    ex1f4();
    return 0;
}