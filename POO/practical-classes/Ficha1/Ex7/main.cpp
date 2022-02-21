#include <iostream>
#include <string>
#include <sstream>

// incompleto

int main(){
    std::string nome, p;
    std::istringstream is;

    std::cout << "Como se chama? ";
    getline(std::cin, nome);
    is.str(nome);

    std::cout << std::endl;

    while(is >> p){
        std::cout << p;

        if(p == "Silva")
            std::cout << "\tOlha o meu puto Silva!";

        std::cout << std::endl;
    }

}