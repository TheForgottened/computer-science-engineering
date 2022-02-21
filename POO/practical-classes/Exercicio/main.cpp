#include "alien.h"
#include "planeta.h"
#include "sistemaPlanetario.h"

int main(){

    SistemaPlanetario OuterRim({new Planeta("Tatooine"), new Planeta("Endor")});
    
    if(OuterRim.existePlaneta("Tatooine"))
        OuterRim.addAlien(new Alien("Greedo"), "Tatooine");
    
    if(OuterRim.existePlaneta("Endor"))
        OuterRim.addAlien(new Alien("Jabba"), "Endor");

    if(OuterRim.existePlaneta("Tatooine"))
        OuterRim.addAlien(new Alien("Yoda"), "Tatooine");
    
    OuterRim.mover("Greedo", "Tatooine", "Endor"); 
    
    cout << OuterRim << endl;
    
    return 0;
}

