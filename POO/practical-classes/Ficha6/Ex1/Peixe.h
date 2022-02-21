#ifndef PEIXE_H
#define PEIXE_H

#include <string>

using namespace std;

class Aquario;

class Peixe {
    string nome, cor;
    int peso = 10;
    int id;
    bool vivo;
    static int conta;
    Aquario *aq;

public:
  
    Peixe(string a, string b = "cinzento")
    : nome(a), cor(b) , id(conta++), aq(nullptr), vivo(true){};

    Peixe(const Peixe& a) {
        nome = a.getNome();
        cor = a.getCor();
        peso = a.getPeso();
        aq = a.getAquario();
        id = conta++;
    }
    
    string getAsString() const;
    string getNome() const { return nome; }
    string getCor() const { return cor; }
    int getPeso() const { return peso; }
    Aquario* getAquario() const { return aq; }
    int getId() const { return id; }

    void comer(int grams);
    void efeito();

    void setAquario(Aquario *a) { aq = a; }
};

// Tem que estar no .cpp
// static int Peixe::conta = 500;

#endif /* PEIXE_H */

