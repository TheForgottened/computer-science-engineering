
#include <iostream>
#include <string>

using namespace std;

class Pessoa{
    string nome;
    int id;
public:
    
    Pessoa(string a, int b): nome(a), id(b){ cout << "Ola " << nome << endl;}
    
    ~Pessoa(){cout << "Adeus " << nome << endl;}
    
    void setNome(string x){nome = x;}
    string getNome() const{return nome;}
};

void f1(Pessoa* & a, Pessoa* & b){

    a = new Pessoa("Ana", 123456);
    
    b = new Pessoa("Rui", 567890);
}


int main(){ 
    Pessoa *p1, *p2;
    
    Pessoa *a = new Pessoa[3]{{"Ze",123},{"Luis", 456},{"Carla", 789}};
  
    f1(p1, p2);
    
    cout << p1->getNome() << endl;

    delete p1;
    delete p2;
    
    delete [] a;
    
    return 0;
}



int mainB(){
     
//    int *p = new int(89);
//    cout << *p << endl;
//    *p = 234;
//    cout << *p << endl;
//    delete p;
    
//    int *a = new int[5]{1,2,3,4,60};
//    
//    a[2] = 123;
//    
//    for(int i=0; i<5; i++)
//        cout << a[i] << " ";
//    cout << endl;
//    
//    delete [] a;
    
    return 0;
}

