#include <iostream>
#include <string>
#include <sstream>

int toInt(std::string s){
    std::string n[4] = {"UM", "DOIS", "TRES", "QUATRO"};
    
    for(int i = 0; i < 4; i++)
        if(n[i] == s)
            return i + 1;
    return -1;
}

std::string toFigure(int a){
    std::string n[4] = {"UM", "DOIS", "TRES", "QUATRO"};

    if(a >= 1 && a <= 4)
        return n[a - 1];

    return "ERROR 404";
}

int main(){
    std::string s;
    int n;
    bool flag = true;
    
    /* while(flag == true){
        std::cout << "Numero por extenso (1 a 4) - FIM para terminar: ";
        std::cin >> s;
        if(s == "FIM")
            flag = false;
        else
            std::cout << toInt(s) << std::endl;
    } */

    /* while(flag == true){
        std::cout << "Numero decimal (1 a 4) - -1 para terminar: ";
        std::cin >> n;
        if(n == -1)
            flag = false;
        else
            std::cout << toFigure(n) << std::endl;
    } */

    while(flag == true){
        std::cout << "Informacao - FIM para terminar: ";
        std::cin >> n;
        if(std::cin.good())
            std::cout << toFigure(n) << std::endl;
        else {
            std::cin.clear();
            std::cin >> s;

            if (s == "FIM")
                flag = false;
            else
                std::cout << toInt(s) << std::endl;
        }
        
    }   
}