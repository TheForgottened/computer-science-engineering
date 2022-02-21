#include <iostream>
#include <string>

void troca(int &a, int &b);

int main(){
    int a = 5, b = 10;

    troca(a, b);

    std::cout << "\na = " << a << "\nb = " << b;
}

void troca(int &a, int &b){
    int n;

    n = a;
    a = b;
    b = n;

    return;
}

