#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void paragraph(char string[]);

void main(){
    char frase[50];
    printf("Insira uma frase:\n");
    gets(frase);

    printf("\n\n");
    printf("**********\n");
    paragraph(frase);
    printf("\n**********");
}

void paragraph(char string[]){
    int i;

    for(i = 0; i < strlen(string); i++){
        if (string[i + 1] == ' ' && string[i] != ' ')
            printf("%c\n", string[i]);
        else if (string[i + 1] != ' ' && string[i] != ' ')
            printf("%c", string[i]);
    }
}