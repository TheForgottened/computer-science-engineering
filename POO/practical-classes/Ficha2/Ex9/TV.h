#ifndef TV_H
#define TV_H

#include <string>
#include <iostream>
#include <sstream> 
#include <initializer_list>
#include <vector>
#include <iostream>

using namespace std;

class TV {
    static const int TAM = 10, VMAX = 10, VMIN = 0;   
    string canais[TAM];
    int canal;
    bool ligada = false;
    int vol;
    int nCanais;
    
public:
    
    TV(initializer_list<string> list) :vol(0)
    {       
        //initializer_list<string> :: iterator it;
        
        canal = 0;
        nCanais = 0;
        for (auto it = list.begin(); it < list.end(); it++)  
            canais[nCanais++] = *it;     
    }

    void ligar();
    void desligar();

    void aumentarVol(int n = 1);
    void diminuirVol(int n = 1);

    void mudarCanal(int n);
    string obterCanais();

    string getAsString();
};

#endif /* TV_H */