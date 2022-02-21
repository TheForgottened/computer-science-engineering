#ifndef PONTO_H
#define PONTO_H

#include <string>
#include <sstream>
#include <iostream>
#include <cmath>

class Ponto {
    int x;
    int y;

public:

    Ponto(int a, int b)
    : x(a), y(b) {}

    void setX(int a);
    void setY(int a);

    std::string getAsString() const;
    int getX() const;
    int getY() const;
    double dist(Ponto p) const;
    bool compare(Ponto p) const;
};

#endif