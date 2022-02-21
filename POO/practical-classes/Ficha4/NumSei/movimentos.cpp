#include "movimentos.h"

bool Movimentos::doMovimento (int n) {
    if ((saldo + n) > 0) {
        saldo += n;

        return 1;
    } else
        return 0;
}

std::string Movimentos::getAsString() const {
    std::stringstream ss;

    ss << "Titular: " << nome << "\tSaldo: " << saldo << "\tMovimentos:";

    for (auto i = v.begin(); i != v.end(); i++)
        ss << ", " << *i;

    return ss.str();
}

std::ostream& operator<<(std::ostream &out, const Movimentos &a) {
    return out << a.getAsString();
} 