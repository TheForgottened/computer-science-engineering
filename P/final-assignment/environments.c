#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "environments.h"

local *readEnv(char *fname, int *size) {
    FILE *f;

    local *v = NULL, *vTemp;
    local buffer;
    int i, j;

    f = fopen(fname, "rb");

    if (f == NULL) {
        printf("Erro na leitura do ficheiro %s!\n", fname);
        return v;
    }

    while (fread(&buffer, sizeof(buffer), 1, f) == 1) {
        vTemp = (local *)realloc(v, sizeof(local) * (*size + 1));

        if (vTemp != NULL) {
            v = vTemp;
            v[*size] = buffer;
            (*size)++;
        } else {
            printf("Erro na alocacao de memoria!");
            free(v);
            *size = 0;
            return NULL;
        }
    }

    fclose(f);
    return v;
}

void printEnv(local *v, int size) {
    int i;

    for (i = 0; i < size; i++) {
        printf("\nIndex: %i\nID: %i.\nCapac. Max.: %i.\nLigacoes: %i, %i, %i.\n", i, v[i].id, v[i].capacidade, v[i].liga[0], v[i].liga[1], v[i].liga[2]);
    }
}

local *checkEnv(local *v, int size) {
    int i, j, l, index;

    for (i = 0; i < size; i++) {
        if (v[i].id <= 0) {
            printf("Um dos locais do espaco tem ID nao positivo!\n");
            return NULL;
        }
        for (j = 0; j < 3; j++) {
            if (v[i].liga[j] != -1) {
                for (l = 0; l < size; l++) {
                    if (v[l].id == v[i].liga[j]) {
                        index = l;
                        l = size;
                    } else if (l == size - 1) {
                        printf("O local com ID = %i tem ligacao com um espaco que nao existe!\n", v[i].id);
                        return NULL;
                    }
                }
                for (l = 0; l < 3; l++) {
                    if (v[index].liga[l] != -1 && v[i].id == v[index].liga[l]) {
                        l = 3;
                    } else if (l == 2) {
                        printf("O local com ID = %i tem ligacao com o local com ID = %i, mas o contrario nao se verifica!\n", v[i].id, v[index].id);
                        return NULL;
                    }
                }
                for (l = 0; l < size; l++) {
                    if (v[l].id == v[i].id && l != i) {
                        printf("Dois ou mais locais do espaco partilham ID!\n");
                        return NULL;
                    }
                }
            }
        }
    }

    return v;
}