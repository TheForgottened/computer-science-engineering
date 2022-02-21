#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct sala local, *plocal;

struct sala {
    int id; // id numérico do local
    int capacidade; // capacidade máxima
    int liga[3]; // id das ligações (-1 nos casos não usados)
};

void main() {
    FILE *f;
    char *fname;
    int i, total;
    local l;

    printf("Nome do ficheiro (com extensao .bin): ");
    gets(fname);

    f = fopen(fname, "wb");

    printf("\nNumero de locais no espaco: ");
    scanf("%i", &total);

    printf("\n");

    for (i = 0; i < total; i++) {
        printf("\nLocal %i\nID: ", (i + 1));
        scanf("%i", &l.id);

        printf("Capacidade: ");
        scanf("%i", &l.capacidade);

        printf("Ligacoes (maximo 3, separadas por espaco, usar -1 para quando nao ha ligacoes): ");
        scanf("%i %i %i", &l.liga[0], &l.liga[1], &l.liga[2]);

        fwrite(&l, sizeof(local), 1, f);
    }

    fclose(f);
    
    printf("\n\nFicheiro acabado!\nPrima uma tecla para sair... ");
    fflush(stdin);
    fflush(stdout);
    getchar();
}