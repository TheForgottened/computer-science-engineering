#ifndef CLIENTE_H
#define CLIENTE_H

#include <string>
#include <sstream>
#include "Tarifario.h"

class Ginasio;

class Cliente {
    std::string nome;
    int cc;
    Tarifario *t;
    Ginasio *g;
    int start;
    bool emTreino = false;

public:
    Cliente (std::string a, int b, Tarifario* c) : nome(a), cc(b), t(c), g(nullptr) {}

    Cliente (const Cliente& ref) {
        nome = ref.nome;
        cc = ref.cc;
        start = ref.start;
        emTreino = ref.emTreino;

        t = ref.t->clone();
    }

    virtual ~Cliente() { delete t; }
    
    void iniciaTreino(int x) { if (emTreino == false) { emTreino = true; start = x; } }
    void terminaTreino(int x) { if (emTreino == true) { emTreino = false; t->acrescentaTreino(x - start); } }
    
    int paga() { return emTreino ? 0 : t->calculaPagamento(); }

    virtual void reageEntrada() = 0;
    virtual void reageSaida() = 0;

    int getCC() const { return cc; }
    int getEmTreino() const { return emTreino; }
    Ginasio* getGinasioPtr() const { return g; }

    void setGinasio(Ginasio* a) { g = a; } 

    std::string getAsString() const {
        int tam = t->getTam(), *v = t->getPtrTreinos();
        std::ostringstream os;

        os << nome << " - " << cc << "\nTreinos por pagar: ";

        for (int i = 0; i < tam; i++)
            if (i == 0)
                os << v[i];
            else
                os << ", " << v[i];

        return os.str();
    }

    Cliente& operator=(const Cliente& dir) {
        if (this == &dir) return *this;

        delete t;

        nome = dir.nome;
        cc = dir.cc;
        start = dir.start;
        emTreino = dir.emTreino;

        t = dir.t->clone();

        return *this;
    }
};

std::ostream& operator<<(std::ostream& out, const Cliente& c) {
    out << c.getAsString();

    return out;
}

#endif /* CLIENTE_H */