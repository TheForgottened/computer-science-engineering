#ifndef AQUARIO_H
#define AQUARIO_H

#include <vector>

#include "Peixe.h"

using namespace std;

class Aquario {
    string nome;
    vector<Peixe*> v;

public:
    Aquario(string a)
    : nome(a) {};

    Aquario(const Aquario& a, string n) {
        nome = n;
        for (auto i = a.v.begin(); i != a.v.end(); i++)
            this->addPeixe(new Peixe (*(*i)));   
    }

    ~Aquario() {
        for (int i = 0; i < v.size(); i++)
            delete v[i];
    }

    string getNome() const { return nome; }
    
    void addPeixe(Peixe *p);
    
    string getAsString() const;
    
    bool existePeixe(int num) const;

    void distribuiComida(int grams);

    void limpaFundo() { return; }
};

ostream& operator<<(ostream& out, const Aquario& a);

#endif /* AQUARIO_H */

