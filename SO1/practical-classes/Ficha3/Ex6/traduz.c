#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define TAM 20

int main() {
    char c, str[20];
    int i;

    setbuf(stdout, NULL);

    while (1 != 0) {
        printf("Insira uma letra: ");
        scanf("%s", &c);

        c = toupper(c); 

        if (c == 'X')
            return 0;
        else {
            printf("O que pretende traduzir?\n");
            scanf("%s", str);

            i = fork();

            if (i < 0) {
                printf("Erro na execucao!\n");
                return 0;
            }

            if (i == 0) {
                if (c == 'I')
                    execl("./ding.exe", "Cena", str, NULL);
                else if (c == 'F')
                    execl("dfran.exe", "Cena", str, NULL);
                else
                    printf("Lingua desconhecida!\n\n");

                return 0;
            } else
                sleep(2);
        }
    }
}