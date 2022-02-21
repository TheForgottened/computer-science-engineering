#include <iostream>

#include "Peixe.h"
#include "Aquario.h"

int main(){ 
    // Má organização de codigo
    // Deveria existir um objeto responsavel pela criação e gestão dos peixes que não estão no aquario
    Peixe *p1 = new Peixe("Nemo", "Amarelo");
    Peixe *p2 = new Peixe("Shark");
       
    Aquario a("Oceano");    
   
    a.addPeixe(p1);
    a.addPeixe(p2);
    
    cout << a << endl;
    
    a.distribuiComida(50);
    
    a.limpaFundo();     // Eliminar os mortos e retirar o ponteiro do vetor

    cout << a << endl;

    return 0;
}

