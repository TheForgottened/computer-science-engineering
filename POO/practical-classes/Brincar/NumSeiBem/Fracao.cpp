
#include "Fracao.h"

Fracao operator*(const Fracao& a, const Fracao& b) { // Passado por referencia para evitar copias desnecessarias. const por razoes de seguranca
    Fracao c;

    c.setNum(a.getNum() * b.getNum());
    c.setDen(a.getDen() * b.getDen());

    return c;
}