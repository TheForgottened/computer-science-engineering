#ifndef BANCO_H
#define BANCO_H

#include <string> 
#include <vector>
#include "conta.h"

class Banco {
    const std::string nome;
    std::vector<Conta> contas;

public:
    Banco (std::string n):
    nome(n) {}

    bool addConta (Pessoa *p);
    bool atualizaSaldo(int cc, int n);

    int obterSaldo(int cc) const;
    int getTotalSaldos() const;
    std::string getAsString() const;
};

#endif