#ifndef APRESSADO_H
#define APRESSADO_H

#include "Tarifario.h"

class Apressado: public Tarifario {
public:
    virtual ~Apressado() {}

    virtual int calculaPagamento() {
        int tam = getTam(), *v = getPtrTreinos(), total = 0;

        for (int i = 0; i < tam; i++)
            if (v[i] <= 10)
                total += 10;
            else if (v[i] >= 11 && v[i] <= 20)
                total += 15;
            else
                total += 25;

        apagaTreinos();

        return total;
    }

    virtual Apressado* clone() { return new Apressado(*this); }
};

#endif /* APRESSADO_H */