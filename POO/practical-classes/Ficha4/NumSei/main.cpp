#include <iostream>
#include "movimentos.h"

int main(int argc, char** argv) {

    Movimentos p1("Artur", 100);        // Cria objeto com nome e saldo inicial
    Movimentos p2("Ana", 200);
    
    p1 = p1 + 20;                       // Depósito de 20 Euros
    
    p1 = p1 + 30 - 80;                  // Depósito de 30 euros e levantamento de 80 
    
    p2 += 50;                          // Depósito de 50 euros
    
    p2 - 20;                            // Deve compilar, mas não altera o objeto

    std::cout << "Iguais: " << (p1 == p2) << std::endl;       // São iguais se tiverem o mesmo saldo
    
    std::cout << p1 << std::endl;                 // Apresenta informação completa sobre o objeto: Nome, Saldo e Movimentos
    
    std::cout << p2 << std::endl;
       
    return 0;
}








