#include <stdio.h>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <ctype.h>
#include <sys/stat.h>
#include <sys/types.h>

#include "servidor.h"
#include "utils.h"

cliente cli[20];
int cliCount, fd_serv;

void sair(int nErr) {
    int i;

    for (i = 0; i < cliCount; i++) {
        cli[i].close = 1;

        if (cli[i].livre == 0)
            pthread_join(cli[i].th, NULL);
    }

    close(fd_serv);
    unlink(SERV_PIPE);

    putchar('\n');
    exit(nErr);
}

void readFile(char* fName) {
    FILE *f = fopen(fName, "r");
    char buffer[256];
    int saldo = 0;

    if (f == NULL) {
        fprintf(stderr, "Erro ao abrir o ficheiro %s!\n", fName);
        sair(0);
    }

    while (fscanf(f, "%s %i", buffer, &saldo) == 2) {
        strcpy(cli[cliCount].nome, buffer);
        cli[cliCount].saldo = saldo;
        cli[cliCount].close = 0;
        cli[cliCount++].livre = 1;
    }

    return;
}

void* readConsole(void* arg) {
    char buffer[256];
    int i;

    while (1) {
        scanf(" %255s", buffer);

        for (i = 0; i < strlen(buffer); i++)
            buffer[i] = toupper(buffer[i]);

        if (strcmp(buffer, "LISTAR") == 0) {
            printf("\nLista de Clientes:\n");

            for (i = 0; i < cliCount; i++)
                printf("%s - %i", cli[i].nome, cli[i].saldo);
        } else if (strcmp(buffer, "SAIR") == 0)
            sair(0);
    }
}

void* readClient(void* arg) {
    char buffer[256], *token;
    int nBytes, i, fd_cliW, fd_cliR;
    cliente* cli = (cliente*)arg;

    if ((nBytes = read(fd_serv, buffer, sizeof(buffer) - sizeof(char))) == -1) {
        fprintf(stderr, "Erro ao ler do pipe %s!\n", SERV_PIPE);
        sair(0);
    }

    sprintf(buffer, "%i_w", cli->pid);

    if ((fd_cliW = open(buffer, O_WRONLY)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe!\n");
        sair(0);
    }

    sprintf(buffer, "%i_r", cli->pid);

    if ((fd_cliR = open(buffer, O_RDONLY)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe\n");
        sair(0);
    }

    while (cli->close != 1 && strcmp(buffer, "SAIR")) {
        if ((nBytes = read(fd_cliR, buffer, sizeof(buffer) - sizeof(char))) == -1) {
            fprintf(stderr, "Erro ao ler do pipe!\n");
            sair(0);
        }

        buffer[nBytes - 1] = '\0';

        for (i = 0; i < strlen(buffer); i++)
            buffer[0] = toupper(buffer[0]);

        token = strtok(buffer, " ");

        if (strcmp(token, "SALDO") == 0) {
            sprintf(buffer, "Saldo: %i", cli->saldo);

            if (nBytes = write(fd_cliW, buffer, strlen(buffer) + sizeof(char)) == -1) {
                fprintf(stderr, "Erro a escrever no pipe específicado!\n");
                sair(0);
            }
        } else if (strcmp(token, "LEVANTAR") == 0) {
            token = strtok(NULL, " ");

            cli->saldo -= atoi(token);
        } else if (strcmp(token, "DEPOSITAR") == 0) {
            token = strtok(NULL, " ");

            cli->saldo += atoi(token);
        } else if (strcmp(token, "SAIR") == 0) {
            break;
        }
    }

    close(fd_cliW);
    close(fd_cliR);

    sair(0);
}

int main() {
    setbuf(stdout, NULL);

    pthread_t console;

    int i, nBytes;
    char buffer[256], *token;

    if (mkfifo(SERV_PIPE, 0777) == -1) {
        fprintf(stderr, "Erro ao criar o pipe %s!\n", SERV_PIPE);
        sair(0);
    }

    if ((fd_serv = open(SERV_PIPE, O_RDWR)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe %s!\n", SERV_PIPE);
        sair(0);
    }

    readFile("contas.txt");

    pthread_create(&console, NULL, readConsole, NULL);

    while (1) {
        if ((nBytes = read(fd_serv, buffer, sizeof(buffer) - sizeof(char))) == -1) {
            fprintf(stderr, "Erro ao ler do pipe %s!\n", SERV_PIPE);
            sair(0);
        }

        buffer[nBytes - 1] = '\0';

        token = strtok(buffer, " ");

        for (i = 0; i < cliCount; i++) {
            if (strcmp(cli[i].nome, token) == 0)
                if (cli[i].livre == 1) {
                    token = strtok(NULL, " ");
                    printf("\nbruh\n");

                    cli[i].pid = atoi(token);
                    pthread_create(&(cli[i].th), NULL, readClient, &(cli[i]));
                }
        }
    }

    return 0;
}