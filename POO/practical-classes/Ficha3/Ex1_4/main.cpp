#include <iostream>
#include "retangulo.h"
#include "ponto.h"
#include "desenho.h"

using namespace std;

/* int main(int argc, char** argv) {
    
    Ponto a(2, 4), b(-4, 0);
    const Ponto c(0, 3);
    
    cout << a.getAsString() << endl;
    
    a.setX(8);
    
    cout << c.getX() << endl;
    
    cout << a.dist(c) << endl;
    cout << c.dist(a) << endl;
    
    cout << a.getAsString() << endl;
   
    return 0;
} */

/* int main(){
    Ponto a(2, 3);
    
    Retangulo r(a, 4, 12);
    
    cout << r.getAsString() << endl;
    
    cout << r.getArea() << endl;

    cout << r.getVertices() << endl;
    
    return 0;    
} */

int main(){
    Desenho d("Outono");
    Ponto p1(1,2);
    
    d.addR(Retangulo(Ponto(1,2), 4, 6));
    d.addR(Retangulo(Ponto(-1,4), 4, 2));
    d.addR(Retangulo(Ponto(1,2), 5, 30));

    d.elimina(1000);
       
    cout << d.areaTotal() << endl;
    
    cout << d.getAsString() << endl;
    
    auto v = d.getRLocal(p1);
    
    cout << "Existem " << v.size() << " retangulos no ponto " << p1.getAsString() << ":" << endl;
    for(auto x : v){
        cout << x.getAsString() << endl;
    }
    
    return 0;    
}
