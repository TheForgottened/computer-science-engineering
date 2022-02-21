#include <iostream>
#include <string>

void imprime(std::string a);
void imprime(std::string a, int n);
void imprime(int n, std::string a);

int main(){
    imprime("Ola");
    imprime("A idade e: ",25);
    imprime(100, " euros");
}

void imprime(std::string a){
    std::cout << a << std::endl;
}

void imprime(std::string a, int n){
    std::cout << a << n << std::endl;
}

void imprime(int n, std::string a){
    std::cout << n << a << std::endl;
}