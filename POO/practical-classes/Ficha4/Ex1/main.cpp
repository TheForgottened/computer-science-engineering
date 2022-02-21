#include <iostream>
#include "Fracao.h"

int main (int arc, char**argv) {
    Fracao x(1, 2), y(2), z(2, 4);

    std::cout << x++ << std::endl;
    std::cout << ++++y << std::endl;

    std::cout << x << std::endl;
    std::cout << y << std::endl;

    std::cout << (x == y) << std::endl;
    std::cout << (x != y) << std::endl;
    std::cout << (2 == y) << std::endl;
    std::cout << (z == 3) << std::endl;
    return 0;
}