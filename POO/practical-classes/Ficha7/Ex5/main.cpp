#include <iostream>
#include "Cartao.h"

#include "Tarifario.h"
#include "Boomer.h"
#include "Zoomer.h"

#include "Rede.h"

using namespace std;

int main(int argc, char** argv) {
    
    Rede tmn({new Zoomer, new Boomer});
    
    // Adicionar um novo cartão
    tmn.addC(new Cartao(123456, 20.0, nullptr), 0);
    tmn.addC(new Cartao(444, 50.0, nullptr), 0);
    
    // Obter descrição textual
    cout << tmn.getAsString() << endl;
    
    // Obter o saldo, dado o numero
    cout << tmn.getSaldo(123456) << endl;
    
    // Autorizar e registar chamada
    if(tmn.autoriza(123456))
        tmn.regista(123456, 401);
 
    cout << tmn.getSaldo(123456) << endl;

    return 0;
}



