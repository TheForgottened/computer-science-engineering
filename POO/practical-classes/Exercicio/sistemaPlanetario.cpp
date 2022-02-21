#include <sstream>

#include "sistemaPlanetario.h"

bool SistemaPlanetario::existePlaneta (string a) const{
    for (Planeta *p: planetas)
        if (p->getName() == a)
            return 1;
    
    return 0;
}

bool SistemaPlanetario::temAlien (string a, string p) const{
    if (!existePlaneta(p))
        return 0;

    for (Planeta *p: planetas)
        if (p->getName() == a)
            return 1; 

    return 0;   
}

bool SistemaPlanetario::addAlien (Alien *a, string p) {
    if (!existePlaneta(p))
        return 0;

    for (auto i = planetas.begin(); i != planetas.end(); i++)
        if ((*i)->getName() == p) {
            (*i)->addAlien(a);
            return 1;
        }

    return 0;
}

bool SistemaPlanetario::addPlaneta (Planeta *p) {
    if (!existePlaneta(p->getName()))
        return 0;

    planetas.push_back(p);

    return 1;
}

bool SistemaPlanetario::mover (string alien, string origem, string destino) {
    if (!existePlaneta(origem) || !existePlaneta(destino))
        return 0;
    
    string words;
    Alien *a = NULL;

    for (auto i = planetas.begin(); i != planetas.end(); i++) {
        if ((*i)->getName() == origem) {
            stringstream ss((*i)->getPop());
            
            while (ss >> words)
                if (alien == words) {
                    a = (*i)->removeAlien(alien);
                    break;
                }
            
            if (a != NULL)
                break;
        }
    }

    for (auto i = planetas.begin(); i != planetas.end(); i++)
        if ((*i)->getName() == destino) {
            (*i)->addAlien(a);
            return 1;
        }

    return 0;
}

string SistemaPlanetario::getAsString() const {
    stringstream ss;

    for (Planeta *p: planetas)
        ss << p->getAsString();

    return ss.str();
}

ostream& operator<<(ostream& out, const SistemaPlanetario& a) {
    out << a.getAsString();

    return out;
}