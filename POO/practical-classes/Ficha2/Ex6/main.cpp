#include <iostream>
#include <string>

class Auto {

    const std::string marca;
    const std::string modelo;
    std::string matricula;

    const int ano;

public:

    Auto(std::string a, std::string b, int n, std::string c = "NA") 
    : marca(a), modelo(b), ano(n), matricula(c) {}

    std::string getMarca() const {
        return marca;
    }

    std::string getMat() const {
        return matricula;
    }

    int getAno() const {
        return ano;
    }

    void setMat(std::string a) {
        matricula = a;
    }

    bool mudaMat(Auto& x) {
        if (matricula == "NA" && x.getMat() == "NA")
            return 0;

        std::string temp = matricula;

        matricula = x.getMat();
        x.setMat(temp);

        return 1;
    }
};

int main() {
    Auto c1("Opel", "Corsa", 2001, "AA-12-BB");
    Auto c2("Fiat", "Punto", 1995);

    std::cout << c1.getMarca() << std::endl;
    
    std::cout << c2.getAno() << std::endl;

    c2.setMat("XX-30-YY");
    
    std::cout << c2.getMat() << std::endl;

    c1.mudaMat(c2);

    std::cout << "C1: " << c1.getMat() << "\nC2: " << c2.getMat() << std::endl;

    return 0;
}