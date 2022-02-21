#include "desenho.h"

bool Desenho::addR(Retangulo r) {
    auto i = v.begin();

    while (i < v.end() && (*i).getArea() < r.getArea())
        i++;

    v.insert(i, r);

    return 1;
}

int Desenho::areaTotal() const {
    int total;

    for (auto i = v.begin(); i != v.end(); i++) {
        total += (*i).getArea();
    }

    return total;
}

std::string Desenho::getAsString() const {
    std::stringstream ss;

    ss << "Desenho " << nome << " com " << v.size() << " retangulos:" << std::endl;

    for (auto i = v.begin(); i != v.end(); i++) {
        ss << (*i).getAsString() << std::endl;
    }
    
    return ss.str();
}

std::vector<Retangulo> Desenho::getRLocal(Ponto p) const {
    std::vector<Retangulo> a;

    for (auto i = v.begin(); i != v.end(); i++)
        if ((*i).getCSE().compare(p) == 1)
            a.push_back(*i);

    return a;
}

void Desenho::elimina(int a) {
    auto i = v.begin();

    while (i != v.end())
        if ((*i).getArea() < a)
            v.erase(i);
        else
            i++;
}