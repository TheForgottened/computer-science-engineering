#ifndef SERVIDOR_H
#define SERVIDOR_H

typedef struct cliente cliente;

struct cliente {
    char nome[256];
    int saldo;
    int livre;
    int close;
    int pid;
    pthread_t th;
};

#endif /* SERVIDOR_H */