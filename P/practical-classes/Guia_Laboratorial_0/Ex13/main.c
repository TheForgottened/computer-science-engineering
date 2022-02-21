#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define N 100

void comparatings(char S1[], char S2[], char S3[]);

void main(){
    char S1[N], S2[N], S3[N];

    printf("Insira S1:\n");
    gets(S1);
    printf("\n");

    printf("Insira S2:\n");
    gets(S2);
    printf("\n");
    
    comparatings(S1, S2, S3);

    puts(S3);
}

void comparatings(char S1[], char S2[], char S3[]){
    if (strcmp(S1, S2) == 0)
        strcpy(S3, "Conteudo igual!");
    else if(strlen(S1) == strlen(S2))
        strcpy(S3, "Tamanho igual!");
    else {
        strcpy(S3, S1);
        strcat(S3, S2);
    }
}