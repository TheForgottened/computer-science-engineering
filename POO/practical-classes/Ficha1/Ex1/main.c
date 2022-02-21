#include <stdio.h>

#define MAX 50

int main(){
    printf("Exercicio 1 - Ficha 1\n");

    char nome[MAX];

    printf("Introduza nome: ");
    scanf("%[^\n]s", nome);

    int idade = 10;

    printf("Introduza a idade: ");
    scanf("%i", &idade);

    printf("Valores lidos: Nome = %s \t Idade = %i\n", nome, idade);

    return 0;
}