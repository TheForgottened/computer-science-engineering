#include <stdio.h>
#include <stdlib.h>

void SomaMedia(char *fname, float *m, int *s) {
    FILE *f;
    int buffer = 0, n = 0;

    *s = 0;
    *m = 0;

    f = fopen(fname, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        *s = *s + buffer;
        n++;
    }

    *m = *s / (float)n;

    fclose(f);
    return;
}

void separaValores(char *nOrig, char *nMaior, char *nMenor) {
    FILE *fOrig, *fMaior, *fMenor;

    int buffer = 0, n = 0;

    fOrig = fopen(nOrig, "rb");

    if (fOrig == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", nOrig);
        fclose(fOrig);
        return;
    }

    fMaior = fopen(nMaior, "wb");

    if (fMaior == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", nMaior);
        fclose(fOrig);
        fclose(fMaior);
        return;
    }

    fMenor = fopen(nMenor, "wb");

    if (fMenor == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", nMenor);
        fclose(fOrig);
        fclose(fMaior);
        fclose(fMaior);
        return;
    }

    int soma;
    float media;
    SomaMedia(nOrig, &media, &soma);

    while (fread(&buffer, sizeof(buffer), 1, fOrig) == 1) {
        if (buffer >= media){
            fwrite(&buffer, sizeof(buffer), 1, fMaior);
            printf("\nMAIOR: %i\n", buffer);
        }
        else {
            fwrite(&buffer, sizeof(buffer), 1, fMenor); 
            printf("\nMENOR: %i\n", buffer);
        }
    }

    fclose(fOrig);
    fclose(fMaior);
    fclose(fMaior);
    return;
}

void printBin(char *fname) {
    FILE *f;
    int buffer = 0;

    f = fopen(fname, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        printf("%i\n", buffer);
    }
}

int* criaVetor(char *nomeFich, int *tam) {
    int *v = NULL, *vMaior, buffer, i;
    FILE *f;

    *tam = 0;

    f = fopen(nomeFich, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", nomeFich);
        return v;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        if (buffer % 2 == 0 && buffer > 0) {
            vMaior = (int *)realloc(v, sizeof(int) * (*tam + 1));

            if (vMaior != NULL) {
                v = vMaior;
                v[*tam] = buffer;
                (*tam)++;
            } else {
                printf("Erro na alocação de memória!");
                free(v);
                *tam = 0;
                return NULL;
            }
        }
    }

    return v;
    printf("tam = %i", *tam);
}

void main() {
    /*char *fOriginal = "valoresEx2.bin";
    int *vetor;
    int n, i;

    printf("Bin original:\n");
    printBin(fOriginal);

    vetor = criaVetor(fOriginal, &n);
    printf("\nValores pares:\n");

    for (i = 0; i < n; i++)
        printf("%i\n", vetor[i]);*/

    char *fOriginal = "valoresEx2.bin";
    char *fMaior = "maiores.bin";
    char *fMenor = "menores.bin";

    float media = 0;
    int soma = 0;
    SomaMedia(fOriginal, &media, &soma);
    printf("Soma = %i\nMedia = %f\n", soma, media);

    separaValores(fOriginal, fMaior, fMenor);

    printf("Bin original:\n");
    printBin(fOriginal);

    printf("Bin maior e igual a media:\n");
    printBin(fMaior);

    printf("Bin menor que a media:\n");
    printBin(fMenor);
}