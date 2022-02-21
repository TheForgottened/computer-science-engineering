#include "Peixe.h"
#include "Aquario.h"

#include <sstream>
#include <random>
#include <ctime>

using namespace std;

double uDist(){
    static default_random_engine e(time(0));        // gerador de numeros aleatorios (unsigned int)  
    static uniform_real_distribution<double> u(0.0, 1.0);

    return  u(e);
}

int Peixe::conta = 500;

string Peixe::getAsString() const {
    stringstream os;

    os << "Peixe-" << id << " " << nome << ": " << cor << " e com " << peso << "g.";
    
    if (aq != nullptr)
        os << " Nada no aquario " << aq->getNome() << ".";

    return os.str();
}

void Peixe::comer(int grams) {
    peso += grams;

    efeito();
}

void Peixe::efeito() {
    if (peso > 50)
        if (uDist() < 0.5)
            vivo = 0;
        else {
            aq->addPeixe(new Peixe(nome, cor));
            peso = 40;
        }     
}