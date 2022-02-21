#include "Agenda.h"

bool Agenda::addContacto(string nome, int tel) {
    for (auto i = v.begin(); i != v.end(); i++) {
        if ((*i)->getNome() == nome)
            return 0;
    }

    v.push_back(new Contacto(nome, tel));
    return true;
}

int Agenda::getTel(string a) const {
    for (auto i = v.begin(); i != v.end(); i++)
        if ((*i)->getNome() == a)
            return (*i)->getTel();

    return -1;
}

bool Agenda::atualizaContacto(string a, int nt) {
    for (auto i = v.begin(); i != v.end(); i++)
        if ((*i)->getNome() == a) {
            (*i)->setTel(nt);

            return 1;
        }

    return 0;
}

bool Agenda::eliminaContacto(int t) {
    for (auto i = v.begin(); i != v.end(); i++)
        if((*i)->getTel() == t) {
            delete (*i);
            v.erase(i);

            return 1;
        }

    return 0;
}

string Agenda::getAsString() const {
    stringstream ss;

    for (auto i = v.begin(); i != v.end(); i++)
        ss << endl << (*i)->getAsString();

    return ss.str();
}

ostream& operator<<(ostream& out, const Agenda& ref) {
    return out << ref.getAsString();
}