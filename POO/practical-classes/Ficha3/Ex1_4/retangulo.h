#ifndef RETANGULO_H
#define RETANGULO_H

#include <string>
#include <sstream>
#include <iostream>
#include <cmath>

#include "ponto.h"

class Retangulo {

    Ponto cse;
    int larg, altura;

public:

    Retangulo (Ponto a, int b, int c)
    :cse(a), larg(b), altura(c) {}

    std::string getAsString() const;
    float getArea() const;
    std::string getVertices() const;
    Ponto getCSE() const;
};

#endif