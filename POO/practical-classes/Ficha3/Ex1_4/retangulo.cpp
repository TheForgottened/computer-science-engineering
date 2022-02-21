#include "retangulo.h"

std::string Retangulo::getAsString() const {
    std::stringstream ss;

    ss << "Canto Superior Esquerdo: " << cse.getAsString() << "\tLargura: " << larg << "\tAltura: " << altura;

    return ss.str();
}

float Retangulo::getArea() const {
    return larg * altura;
}

std::string Retangulo::getVertices() const {
    std::stringstream ss;

    ss << "(" << cse.getX() << ", " << cse.getY() << ")\t(" << cse.getX() + larg << ", " << cse.getY() << ")" << std::endl
    << "(" << cse.getX() << ", " << cse.getY() - altura << ")\t(" << cse.getX() + larg << ", " << cse.getY() - altura << ")";

    return ss.str();
}

Ponto Retangulo::getCSE() const {
    return cse;
}