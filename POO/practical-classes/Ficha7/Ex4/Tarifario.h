#ifndef TARIFARIO_H
#define TARIFARIO_H

#include <iostream>
#include <sstream>

class Tarifario {
    int *v;
    int tam;   
    
protected:
    void apagaTreinos() { // Eliminar o array dinâmico de inteiros
        if (v == nullptr)
            return;

        delete [] v;
        v = nullptr;
        tam = 0;
    }
    
public:
    Tarifario() : v(nullptr), tam(0) {}

    Tarifario (const Tarifario& ref) {
        if (ref.tam == 0) {
            v = nullptr;
            tam = 0;
        } else {
            tam = ref.tam;
            v = new int (tam);

            for (int i = 0; i < tam; i++)
                v[i] = ref.v[i];
        }
    }
 
    virtual ~Tarifario(){ delete [] v; }
    
    void acrescentaTreino(int x) {
        int *temp = new int (tam + 1);

        for (int i = 0; i < tam; i++)
            temp[i] = v[i];

        temp[tam++] = x;

        delete [] v;

        v = temp;
    }

    virtual int calculaPagamento() = 0;

    virtual Tarifario* clone() = 0;

    int getTam() const { return tam; }

    int* getPtrTreinos() const { return v; }

    std::string getAsString() const {
        std::ostringstream os;

        os << "Numero de treinos guardados: " << tam << "\nDuracoes: ";

        for (int i = 0; i < tam; i++)
            if (i != (tam - 1))   
                os << v[i] << ", ";
            else
                os << v[i];

        return os.str();
    }
};

#endif /* TARIFARIO_H */

