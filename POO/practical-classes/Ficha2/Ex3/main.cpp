#include <iostream>
#include <string>

#include "caderno.h"

int main() {
    Caderno c("Note", "Verde", 80, 40, 20, "ISEC");
    
    c.setLocal("Casa");
    
    std::cout << "O livro esta no local " << c.getLocal() << std::endl;
    
    c.abrir();
    
    c.escrever("Ola Mundo");
    
    c.setLocal("Cafe");
    
    c.fechar();
    
    std::cout << "O livro tem " << c.getPaginas() << " paginas." << std::endl;
    
    c.escrever("Mais uma frase");
    
    std::cout << "\nConteudo do caderno:\n\n" << c.obtemTexto() << "\n*FIM*" << std::endl;

    c.abrir();

    std::cout << "\nConteudo do caderno:\n\n" << c.obtemTexto() << "\n*FIM*" << std::endl;

    std::cout << "Esta caderno tem escrito " << c.getTotalPalavras() << " palavras." << std::endl;

    return 0;
}