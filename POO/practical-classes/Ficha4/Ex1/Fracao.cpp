
#include "Fracao.h"

Fracao operator*(const Fracao& a, const Fracao& b) { // Passado por referencia para evitar copias desnecessarias. const por razoes de seguranca
    Fracao c;

    c.setNum(a.getNum() * b.getNum());
    c.setDen(a.getDen() * b.getDen());

    return c;
}

bool operator==(const Fracao &a, const Fracao &b) {
    return ((a.getNum() * b.getDen()) == (b.getNum() * a.getDen()) && (a.getDen() * b.getDen()) == (b.getDen() * a.getDen()));
}

bool operator!=(const Fracao &a, const Fracao &b) {
    return !(a == b);
}

std::ostream& operator<<(ostream &out, const Fracao &a) {
    return out << a.getAsString();
} 