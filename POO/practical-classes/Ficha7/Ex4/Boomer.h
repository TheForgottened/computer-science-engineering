#ifndef BOOMER_H
#define BOOMER_H

#include "Tarifario.h"

class Boomer: public Tarifario {
public:
    virtual ~Boomer() {}

    virtual int calculaPagamento() {
        int tam = getTam(), *v = getPtrTreinos(), total = 0;

        for (int i = 0; i < tam; i++)
            total += v[i];

        apagaTreinos();

        return total;
    }

    virtual Boomer* clone() { return new Boomer(*this); }
};

#endif /* BOOMER_H */