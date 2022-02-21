#ifndef PLANETA_H
#define PLANETA_H

using namespace std;

#include <string>
#include <vector>
#include <iostream>

#include "alien.h"

class Planeta {
    const string nome;
    vector<Alien*> pop;

public:
    Planeta(string n)
    : nome(n) {}

    Planeta(const Planeta &ref) {
        for (Alien *a: ref.pop)
            addAlien(new Alien(a->getNome()));
    }

    ~Planeta() {
        for (auto i = pop.begin(); i != pop.end(); i++)
            delete (*i);
    }

    const string getName() { return nome; }
    
    void addAlien(Alien *a) { pop.push_back(a); };
    Alien* removeAlien(string a);

    Planeta& operator=(const Planeta &p);

    string getPop();
    string getAsString();
};

#endif /* PLANETA_H */