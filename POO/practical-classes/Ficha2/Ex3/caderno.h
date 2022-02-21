#ifndef CADERNO_H
#define CADERNO_H

#include <string>
#include <sstream>
#include <iostream>

class Caderno {
    std::string const marca, cor;
    int const nPag, alt, larg;
    bool aberto = 1;
    std::string conteudo, local;
   
public:
    Caderno(std::string a, std::string b, int c, int d, int e, std::string f = "")
    :marca(a), cor(b), nPag(c), alt(d), larg(e), local(f) {}

    ~Caderno() {
        std::cout << "Estou a destruir o caderno " << marca << std::endl;
    }

    int getPaginas() { return nPag; };
    int getTotalPalavras();

    std::string obtemTexto();

    void setLocal(std::string novoLocal);

    void abrir();
    void fechar();

    void escrever(std::string a);
 
    std::string getLocal() const { return local; }
};

#endif /* CADERNO_H */