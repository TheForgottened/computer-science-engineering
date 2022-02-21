#include <unistd.h>
#include <stdio.h>
#include <string.h>

#define TAM 20

int main() {
    char str[TAM];
    char p[9] = "port.exe";
    char i[8] = "ing.exe";
    int n = 0;

    printf("O que pretende executar?\n");
    fflush(stdout);
    fflush(stdin);
    scanf("%s", str);

    // strcat(str, ".exe");

    printf("E que metodo pretende usar? execl(): 1\texeclp(): 2\n");
    scanf("%i", &n);

    if(n == 1)
        execl(str, "Cena", NULL);
    else if(n == 2)
        execlp(str, "Cena", NULL);   

    printf("Input desconhecido!\n");
    
    return 0;
}