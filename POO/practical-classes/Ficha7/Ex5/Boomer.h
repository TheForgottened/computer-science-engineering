#ifndef BOOMER_H
#define BOOMER_H

#include "Tarifario.h"

class Boomer : public Tarifario {

public:
    virtual bool autoriza(int val) { return val > 0; }
    
    virtual int calculaPreco(int minutos) { return minutos * 0.25; }
    
    virtual bool carregaValor(float& valor) {
        if (valor >= 25) {
            if (valor >= 50)
                valor += 5;
                
            return true;
        } else
            return false;
    }
};

#endif /* BOOMER_H */