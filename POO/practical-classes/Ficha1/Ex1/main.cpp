#include <iostream>
#include <string>
#include <ctype.h>
using namespace std;

#define MAX 50

int main(){
    cout << "Exercicio 1 - Ficha 1\n";

    string nome;

    do {
        cout << "Introduza nome: ";
        getline(cin, nome);

        if (toupper(nome[0]) < 'A' || toupper(nome[0]) > 'Z')
            cout << "\nNome invalido!\n\n";
    } while (toupper(nome[0]) < 'A' || toupper(nome[0]) > 'Z');

    int idade = 10;

    do {
        if (cin.fail()){
            cin.clear();
            cin.ignore(1000, '\n');
        }

        cout << "\nIntroduza a idade: ";
        cin >> idade;

        if (!cin.fail())
            break;

        cout << "\nLeitura invalida da idade.\n\n";          
    } while (cin.fail());

    cout << "\nValores lidos: \nNome = " << nome << "\nIdade = " << idade << endl;

    return 0;
}