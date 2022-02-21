#include "banco.h"

bool Banco::addConta (Pessoa *p) {
    for (auto i = contas.begin(); i != contas.end(); i++)
        if ((*i).getTitular()->getCC() == (*p).getCC())
            return 0;

    Conta c(p);
    contas.push_back(c);

    return 1;
}

bool Banco::atualizaSaldo(int cc, int n) {
    auto i = contas.begin();

    while (i != contas.end() && (*i).getTitular()->getCC() != cc)
        i++;

    if ((*i).getTitular()->getCC() != cc)
        return 0;

    return (*i).variarSaldo(n);
}

int Banco::obterSaldo(int cc) const {
    auto i = contas.begin();

    while (i != contas.end() && (*i).getTitular()->getCC() != cc)
        i++;
    
    if ((*i).getTitular()->getCC() != cc)
        return -1;

    return (*i).getSaldo();
}

int Banco::getTotalSaldos() const {
    int total = 0;

    for (auto i = contas.begin(); i != contas.end(); i++)
        total+= (*i).getSaldo();
    
    return total;
}

std::string Banco::getAsString() const {
    std::stringstream ss;

    ss << nome << "\tNumero total de clientes: " << contas.size();

    for (auto i = contas.begin(); i != contas.end(); i++)
        ss << "\nTitular: " << (*i).getTitular()->getNome() << "\tCC: " << (*i).getTitular()->getCC() << "\tSaldo: " << (*i).getSaldo();

    return ss.str();
}