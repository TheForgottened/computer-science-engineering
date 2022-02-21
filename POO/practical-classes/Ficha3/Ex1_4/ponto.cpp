#include "ponto.h"

std::string Ponto::getAsString() const {
    std::stringstream ss;

    ss  << "(" << x << ", " << y << ")";

    return ss.str();
}

void Ponto::setX(int a) {
    x = a;
}

void Ponto::setY(int a) {
    y = a;
}

int Ponto::getX() const {
    return x;
}

int Ponto::getY() const {
    return y;
}

double Ponto::dist(Ponto p) const {
    return sqrt((x - p.getX()) * (x - p.getX()) + (y - p.getY()) * (y - p.getY()));
}

bool Ponto::compare(Ponto p) const {
    return (x == p.getX() && y == p.getY());
}