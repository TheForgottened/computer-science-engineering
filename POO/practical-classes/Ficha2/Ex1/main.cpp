#include <iostream>
#include <string>
#include <sstream>

#define TAM 15

struct Tabela{
    int tab[TAM];    
};

void preenche(Tabela &t, int n){
    for (int i = 0; i < TAM; i++)
        t.tab[i] = n;
}

void setValor(Tabela &t, int p, int n){
    if (p < TAM)
        t.tab[p] = n;
}

void listar(Tabela t){
    for (int i = 0; i < TAM; i++)
        std::cout << t.tab[i] << "\t";
}

int getValor(Tabela t, int p){
    if (p < TAM)
        return t.tab[p];
    else
        return -1;    
}

int& elementoEm(Tabela& a, int pos){
    return a.tab[pos];
}

int main(int argc, char** argv){ 
    Tabela t;
    
    preenche(t, 20);          // Preenche todas as posições com valor do segundo parâmetro
    setValor(t, 10, 50);      // Colocar valor do terceiro parâmetro na posição do segundo parâmetro
    listar(t);                // Mostra na consola todos os valores armazenados
   
    std::cout << "\nValor: " << getValor(t, 12) << std::endl;      // Obtém o valor armazenado na posição do segundo parâmetro

    return 0;
}