#include "Caderno.h"

void Caderno::setLocal(std::string novoLocal) {
    if(aberto == false)
        local = novoLocal;
}

void Caderno::abrir() {
    aberto = 1;
}

void Caderno::escrever(std::string a) {
    if (aberto == false)
        return;

    conteudo += "\n" + a;
}

void Caderno::fechar() {
    aberto = 0;
}

std::string Caderno::obtemTexto() {
    if (aberto == false)
        return "O livro esta fechado portanto e impossivel le-lo.";
    
    return conteudo;
}

int Caderno::getTotalPalavras() {
    int nPalavras = 0;

    std::string palavra;
    std::stringstream iss;
    
    iss.str(conteudo);

    while (iss >> palavra)
        nPalavras++;

    return nPalavras;
}