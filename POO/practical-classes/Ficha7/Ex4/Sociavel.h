#ifndef SOCIAVEL_H
#define SOCIAVEL_H

#include "Cliente.h"
#include "Ginasio.h"

class Sociavel : public Cliente {

public:
    Sociavel(std::string a, int b, Tarifario* c): Cliente(a, b, c) {

    }

    virtual void reageEntrada() {};

    virtual void reageSaida() {
        Ginasio* g = this->getGinasioPtr();

        if (g->getNrClientes() == 0)
            g->registaSaida(this->getCC());
    }
};

#endif