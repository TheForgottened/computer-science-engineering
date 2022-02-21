#include <iostream>
#include <fstream>
#include "Pessoa.h"
#include "Conta.h"
#include "Banco.h"

void getDados(istream& in, string &a, int &b, int &c){
    
    getline(in, a);
    in >> b >> c;
    
    if(in.fail()){
        a = "Ze Ninguem";
        b = c = -1;
        in.clear();
    }
}

int main(){
    
    int cc, nif;
    string nome;
    
    ifstream f;
    
    f.open("a.txt");
    
    if(!f)
        cout << "ERRO" << endl;
    else
    {
    
    getDados(f, nome, cc, nif);
    
    Pessoa a(nome, cc, nif);
    
    cout << a.getAsString() << endl;
    
    f.close();
    }
    
    return 0;
}