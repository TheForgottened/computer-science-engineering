

#ifndef AGENDA_H
#define AGENDA_H

#include <vector>
#include <iostream>
#include <sstream>
#include "Contacto.h"

class Agenda{
    vector<Contacto*> v;
public:

    Agenda() = default;

    Agenda(const Agenda& a) {
        for (auto i = a.v.begin(); i != a.v.end(); i++)
            this->addContacto((*i)->getNome(), (*i)->getTel());
    }
    
    ~Agenda() {
        for (auto i = v.begin(); i != v.end(); i++)
            delete (*i);
    }

    Agenda& operator=(const Agenda& a) {
        if (this == &a)
            return *this;

        for (auto i = v.begin(); i != v.end(); i++)
            delete (*i);

        v.clear();

        for (auto i = a.v.begin(); i != a.v.end(); i++)
            this->addContacto((*i)->getNome(), (*i)->getTel());

        return *this;
    } 
    
    // Alterar para impedir a adicao de contactos com nomes repetidos
    // Organizar a agenda por ordem alfabetica
    bool addContacto(string nome, int tel);
    
    int getTel(string a) const;     // Devolve telefone do Contacto a (-1 se não existir)
    
    bool atualizaContacto(string a, int nt);    // atualiza numero do contacto a
    
    bool eliminaContacto(int t);        // Elimina contacto com este numero
    
    string getAsString() const;
};

ostream& operator<<(ostream& out, const Agenda& ref);

#endif /* AGENDA_H */

