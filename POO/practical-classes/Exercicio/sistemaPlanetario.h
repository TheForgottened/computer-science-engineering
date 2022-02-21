#ifndef SISTEMAPLANETARIO_H
#define SISTEMAPLANETARIO_H

using namespace std;

#include <vector>
#include <string>
#include <initializer_list>
#include <iostream>

#include "planeta.h"

class SistemaPlanetario {
    vector<Planeta*> planetas;

public:
    SistemaPlanetario(initializer_list<Planeta*> l)
    : planetas(l) {};

    SistemaPlanetario(const SistemaPlanetario &ref) {
        for (Planeta *p: ref.planetas)
            addPlaneta(new Planeta(*p));
    }

    ~SistemaPlanetario() {
        for (auto i = planetas.begin(); i != planetas.end(); i++)
            delete *i;
    }

    bool existePlaneta (string a) const;
    bool temAlien (string a, string p) const;

    bool addAlien (Alien *a, string p);
    bool addPlaneta (Planeta *p);

    bool mover (string alien, string origem, string destino);

    string getAsString() const;
};

ostream& operator<<(ostream& out, const SistemaPlanetario& a);

#endif /* SISTEMAPLANETARIO_H */