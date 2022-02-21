#include "Clube.h"
#include <sstream>
#include <iostream>

using namespace std;

ostream& operator<<(ostream& out, const Clube& a) {
    out << a.getAsString();

    return out;
}

bool Clube::setMembro(Pessoa *p) {
    if (conta != tam) {
        tab[conta++] = p;

        return 1;
    } else
        return 0;
}

string Clube::getAsString() const {
    ostringstream os;

    os << nome << endl;

    for (int i = 0; i < conta; i++) {
        os << tab[i]->getAsString() << endl;
    }

    return os.str();
}

bool Clube::eliminaMembro(int cc) {
    int i;

    for (i = 0; i < conta; i++)
        if (tab[i]->getCC() == cc) {
            tab[i] = tab[conta - 1];
            tab[conta--] = nullptr;
            return 1;
        }

    return 0;
}