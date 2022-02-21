#ifndef ZOOMER_H
#define ZOOMER_H

#include "Tarifario.h"

class Zoomer : public Tarifario {

public:
    virtual bool autoriza(int val) { return val > 0; }
    
    virtual int calculaPreco(int minutos) { return minutos > 0 ? (0.5 + ((minutos - 1) * 0.02)) : 0; }
    
    virtual bool carregaValor(float& valor) {
        if (valor >= 25) {
            if (valor >= 50)
                valor += 5;
                
            return true;
        } else
            return false;
    }
};

#endif /* ZOOMER_H */