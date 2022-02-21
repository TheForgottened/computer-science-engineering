#include "Tarifario.h"
#include "Apressado.h"
#include "Boomer.h"

#include "Cliente.h"
#include "Sociavel.h"

#include "Ginasio.h"

int main(int argc, char** argv) {
    Ginasio g;
    
    g.addCliente(new Sociavel("Ana Silva", 123, new Apressado));
    g.addCliente(new Sociavel("Joao Pires", 456, new Boomer));
    
    g.passaTempo(10);
    
    g.registaEntrada(123);
    g.passaTempo(8);
    g.registaEntrada(456);
    g.registaSaida(123);
    
    g.passaTempo(30);
    
    g.registaSaida(456);
    
    std::cout << g.pagamento(123) << std::endl;
    
    std::cout << g.pagamento(123) << std::endl;
     
    return 0;
}

