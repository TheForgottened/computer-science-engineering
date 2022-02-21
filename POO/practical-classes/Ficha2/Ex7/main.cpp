#include <iostream>
#include <sstream>

class MSG {
    char letra;
    int num;
    
    static int contador;
    
public:

    MSG (char c = 'x')
    : letra(c), num(contador++) {}
        
    std::string getAsString() const {
        std::ostringstream str;

        str << "Letra: " << letra << "\tNum: " << num << std::endl;

        return str.str();
    }
};

int MSG::contador = 1;

int main(){  
    MSG a;
    MSG b('r');

    std::cout << a.getAsString() << std::endl;
    
    std::cout << b.getAsString() << std::endl;
    
    return 0;  
}
