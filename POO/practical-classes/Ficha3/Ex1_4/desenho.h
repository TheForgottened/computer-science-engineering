#ifndef DESENHO_H
#define DESENHO_H

#include <vector>
#include <iostream>
#include "Retangulo.h"

class Desenho {
    std::string nome;
    std::vector<Retangulo> v;

public:
    Desenho(std::string s)
    : nome(s) {}
    
    bool addR(Retangulo r);     // Adiciona Retangulo r ao Desenho
    
    int areaTotal() const;      // Devolve soma das areas dos retangulos do Desenho
    
    std::string getAsString() const; // Obtem descrição textual completa (Nome + Retangulos)

    std::vector<Retangulo> getRLocal(Ponto p) const; // Devolve conjunto de retangulos com cse no Ponto p   

    void elimina(int a); 
};

#endif /* DESENHO_H */