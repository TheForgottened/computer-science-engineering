#ifndef GINASIO_H
#define GINASIO_H

#include <vector>
#include "Cliente.h"

class Ginasio {
    std::vector<Cliente*> v;
    int tempo;
    int aTreinar;

public:
    Ginasio():tempo(0), aTreinar(0){}
    
    virtual ~Ginasio() {
        while (v.size() != 0) {
            delete v.at(v.size() - 1);
            v.pop_back();
        }
    }     // Eliminar clientes
    
    int getNrClientes() const { return v.size(); }

    void addCliente(Cliente* p) { v.push_back(p); p->setGinasio(this); }
    
    void passaTempo(int x) { tempo += x; }
    
    void registaEntrada(int cc) {
        for (auto i = v.begin(); i != v.end(); i++)
            if ((*i)->getCC() == cc)
                (*i)->iniciaTreino(tempo);

        for (auto i = v.begin(); i != v.end(); i++)
            if ((*i)->getCC() != cc)
                (*i)->reageEntrada();
    };
    
    void registaSaida(int cc) {
        for (auto i = v.begin(); i != v.end(); i++)
            if ((*i)->getCC() == cc)
                (*i)->terminaTreino(tempo);

        for (auto i = v.begin(); i != v.end(); i++)
            if ((*i)->getCC() != cc)
                (*i)->reageSaida();
    };
    
    int pagamento(int cc) {
        for (auto i = v.begin(); i != v.end(); i++)
            if ((*i)->getCC() == cc)
                return (*i)->paga();

        return -1;
    }
};

#endif /* GINASIO_H */