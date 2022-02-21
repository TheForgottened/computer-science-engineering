#include <stdio.h>
#include <stdlib.h>

typedef struct paragem paragem;

struct paragem {
    char nome[50];
    int minutos;
};

void PrintCP(char *fname) {
    FILE *f;
    paragem buffer;

    f = fopen(fname, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        printf("%15s %4i min\n", buffer.nome, buffer.minutos);
    }

    fclose(f);
    return;
}

int TotalPercurso(char *fname) {
    FILE *f;
    paragem buffer;
    int total = 0;

    f = fopen(fname, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return -1;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        total += buffer.minutos;
    }

    fclose(f);
    return total;
}

void main() {
    char *file1 = "cp_ex1.dat";

    PrintCP(file1);
    printf("\nTempo total de percurso = %i min", TotalPercurso(file1));
}