#include <iostream>
#include "TV.h"

int main(){

    TV a({"RTP", "RTP2", "SIC", "TVI"});
    
    a.ligar();
    
    a.aumentarVol();
    
    a.mudarCanal(3);
    
    cout << a.obterCanais() << endl;
    
    cout << a.getAsString() << endl;
    
    a.desligar();
    
    return 0;  
}