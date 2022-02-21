#ifndef ALIEN_H
#define ALIEN_H

#include <string>
#include <iostream>

using namespace std;

class Alien{
    string nome;
    int energia;
public:
    Alien (string s)
    : nome(s), energia(10) {}
    
    // ~Alien(){cout << "D " << nome << endl;}
    
    string getNome() const {return nome;}
    int getEn() const {return energia;}
};

#endif /* ALIEN_H */

