#ifndef ENVIRONMENTS_H
#define ENVIRONMENTS_H

typedef struct sala local, *plocal;

struct sala {
    int id; // id numérico do local
    int capacidade; // capacidade máxima
    int liga[3]; // id das ligações (-1 nos casos não usados)
};

// Função que lê o ficheiro binário cujo nome é passado
local *readEnv(char *fname, int *size);

// Função que imprime o vetor de espaço, indicando, do local em questão, o ID, a capacidade máxima e as ligações. Feita para uso em debug.
void printEnv(local *v, int size);

// Função que verifica se todas as condições necessárias para o espaço ser válido se encontram
local *checkEnv(local *v, int size);

#endif /* ENVIRONMENTS_H */