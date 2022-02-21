
#ifndef CONTACTO_H
#define CONTACTO_H

#include <string>
#include <sstream>

using namespace std;

class Contacto{
    const string nome;
    int tel;

public:
    Contacto(string a, int b): nome(a), tel(b){}
    
    string getNome() const {return nome;}
    int getTel() const {return tel;}
    
    string getAsString() {
        stringstream ss;

        ss << "Nome: " << nome << " Tlm: " << tel;

        return ss.str();
    }

    void setTel(int a) {tel = a;}
};

#endif /* CONTACTO_H */

