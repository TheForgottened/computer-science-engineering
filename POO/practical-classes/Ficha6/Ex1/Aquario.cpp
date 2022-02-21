#include "Aquario.h"

#include <sstream>
#include <iostream>

using namespace std;

void Aquario::addPeixe(Peixe *p) {
    for (Peixe* i: v)
        if (i == p)
            return;

    v.push_back(p);
    p->setAquario(this);
    
    return;
}

string Aquario::getAsString() const {
    ostringstream os;

    os << "Aquario " << nome << " com " << v.size() << " peixes:" << endl;

    for (Peixe* i: v)
        os << i->getAsString() << endl;

    return os.str();
}

bool Aquario::existePeixe(int num) const {
    for (Peixe* i: v)
        if (i->getId() == num)
            return 1;

    return 0;
}

void Aquario::distribuiComida(int grams) {
    auto x = v;

    for (auto i = x.begin(); i != x.end(); i++)
        (*i)->comer(grams);
}

ostream& operator<<(ostream& out, const Aquario& a) {
    return out << a.getAsString();
}