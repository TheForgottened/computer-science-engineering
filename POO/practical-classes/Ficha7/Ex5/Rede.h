#ifndef REDE_H
#define REDE_H

#include <initializer_list>
#include <vector>

#include "Tarifario.h"

using namespace std;

class Rede {
    vector<Tarifario*> v;

public:
    Rede(initializer_list<Tarifario*> v) {
        
    }
}

#endif /* REDE_H */