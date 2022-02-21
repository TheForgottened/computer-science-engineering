#ifndef CLUBE_H
#define CLUBE_H

#include "Pessoa.h"
class Clube {
    string nome;
    int tam;
    Pessoa **tab;
    int conta;
    
public:
    Clube(string a, int b):nome(a), tam(b), conta(0){
        tab = new Pessoa*[tam];
        for(int i=0;i<tam; i++)
            tab[i] = nullptr;
    }

    ~Clube() { delete [] tab; }
    
    bool setMembro(Pessoa *p);          // Adiciona novo Sócio (se ainda existir espaço)
    
    bool eliminaMembro(int cc);         // Elimina Sócio, dado cc
    
    string getAsString() const;         // Obtem descrição textual

    Clube& operator=(const Clube& dir) {
        if (this == &dir)
            return *this;

        nome = dir.nome;
        tam = dir.tam;
        conta = dir.conta;
        delete [] tab;
        tab = new Pessoa*[tam];

        for (int i = 0; i < tam; i++) {
            tab[i] = dir.tab[i];
        }

        return *this;
    }
    
};

ostream& operator<<(ostream& out, const Clube& c);

#endif /* CLUBE_H */

