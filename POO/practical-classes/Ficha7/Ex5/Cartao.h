#ifndef CARTAO_H
#define CARTAO_H

#include <string>
#include <sstream>
#include "Tarifario.h"

using namespace std;

class Cartao {
    int num;
    float saldo;
    Tarifario* t;   
     
public:
    Cartao(int a, float b, Tarifario* c): num(a), saldo(b), t(c){}
    
    virtual ~Cartao(){}
    
    int getNum() const {return num;}
    float getSaldo() const {return saldo;}
    
    void setT(Tarifario* p){t = p;}
    
    bool autorizaChamada() const { return t->autoriza(saldo);}
    
    void registaChamada(int min){saldo -= t->calculaPreco(min);}
    
    bool carrega(float valor){
        if (t->carregaValor(valor) == true){
            saldo += valor;
            return true;
        }
        return false;
    }
    
    string getAsString() const{
        ostringstream os;
        
        os << "Cartao " << num << " com saldo " << saldo;       
        return os.str();
    }
};

#endif /* CARTAO_H */

