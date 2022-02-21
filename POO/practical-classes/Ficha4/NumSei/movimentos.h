#include <string>
#include <vector>
#include <sstream>

class Movimentos {
    std::string nome;
    int saldo;
    std::vector<int> v;

public:
    Movimentos (std::string s, int v):
    nome(s), saldo(v) {};

    bool doMovimento (int n);

    Movimentos operator+ (int n) {
        Movimentos temp(this->nome, this->saldo);

        temp.doMovimento(n);
        return temp;
    }

    Movimentos operator- (int n) {
        Movimentos temp(this->nome, this->saldo);

        temp.doMovimento(-n);
        return temp;
    }

    Movimentos operator+= (int n) {
        Movimentos temp(this->nome, this->saldo);

        temp.doMovimento(n);
        return temp;
    }

    bool operator== (const Movimentos &a) {
        return (saldo == a.saldo);
    }

    std::string getAsString() const;
};

std::ostream& operator<<(std::ostream &out, const Movimentos &a);