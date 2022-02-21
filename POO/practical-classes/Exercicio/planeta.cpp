#include <sstream>
#include <iostream>

#include "planeta.h"

Planeta& Planeta::operator=(const Planeta &p) {
    if (&p == this)
        return *this;

    while (pop.size() != 0) {
        delete pop.at(0);
        pop.erase(pop.begin());
    }

    pop.clear();
}

string Planeta::getPop() {
    stringstream ss;

    for (Alien *a: pop)
        ss << a->getNome() << endl;

    return ss.str();
}

string Planeta::getAsString() {
    stringstream ss;

    ss << "Planeta " << nome << ":\n" << getPop();

    return ss.str();
}

Alien* Planeta::removeAlien(string a) {
    Alien *alien = NULL;

    for (auto i = pop.begin(); i != pop.end(); i++)
        if ((*i)->getNome() == a) {
            alien = *i;
            pop.erase(i);
            break;
        }

    return alien;
}