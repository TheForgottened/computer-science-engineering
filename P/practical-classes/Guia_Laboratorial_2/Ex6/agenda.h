#ifndef AGENDA_H
#define AGENDA_H
#define TNAME 20

typedef struct contacto cont;

struct contacto{
    char name[TNAME];
    int number;
};

cont* novoContacto(cont *a, int *n);
int indiceContacto(char *name, int tam, cont *a);
void printAgenda(cont *a, int tam);
cont* delContacto(cont *a, int *n, char *name);

#endif