#ifndef FRACAO_H
#define FRACAO_H

#include <string>
#include <sstream>

using namespace std;

class Fracao {
    int num, den;

    public:

    Fracao(int a = 0, int b = 1) : num(a), den(b > 0 ? b : 1) {}

    int getNum() const {return num;}
    int getDen() const {return den;}

    void setNum(int a) {num = a;}
    void setDen(int a) {if (a > 0) den = a;}

    string getAsString() const {
        ostringstream os;

        os << "(" << num << "/" << den << ")";

        return os.str();
    }

    //Fracao operator* (Fracao b) {
    //    b.setNum(b.getNum() * num);
    //    b.setDen(b.getDen() * den);
    //
    //    return b;
    //} 
};

Fracao operator*(Fracao a, Fracao b);

#endif /* FRACAO_H */