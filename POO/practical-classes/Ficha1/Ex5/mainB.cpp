#include <iostream>
#include <string>

int soma(int a = 0, int b = 0, int c = 0);

int main(){
    std::cout << "\n" << soma() << "\n" << soma(1);
    std::cout << "\n" << soma(1,2) << "\n" << soma(1,2,3);
}

int soma(int a, int b, int c){
    return a + b + c;
}