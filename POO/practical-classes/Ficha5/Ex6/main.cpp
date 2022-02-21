
#include "Agenda.h"

int main(){

    Agenda a;
      
    a.addContacto("Ana", 123123123);
    a.addContacto("Pedro", 333444555);

    Agenda b(a);
       
    cout << a << endl;
    
    string s = "Pedro";
    
    cout << "O telefone do/a " << s << " e: " << a.getTel(s) << endl;   
   
    a.atualizaContacto("Pedro", 999999999);
    
    a.eliminaContacto(123123123);
    
    a.addContacto("Luis",12);   
    cout << a << endl;

    a = a;

    cout << a << endl;
    cout << b << endl;

    b = a;

    cout << b << endl;
       
    return 0;
}

