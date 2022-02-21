#include <iostream>
#include <string>

int soma();
int soma(int a);
int soma(int a, int b);
int soma(int a, int b, int c);

int main(){
    std::cout << "\n" << soma() << "\n" << soma(1);
    std::cout << "\n" << soma(1,2) << "\n" << soma(1,2,3);
}

int soma(){
    return 0;
}

int soma(int a){
    return a;
}

int soma(int a, int b){
    return a + b;
}

int soma(int a, int b, int c){
    return a + b + c;
}