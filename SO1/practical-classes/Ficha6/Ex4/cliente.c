#include <stdio.h>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <ctype.h>
#include <sys/stat.h>
#include <sys/types.h>

#include "utils.h"

int main() {
    char pipe_w[256], pipe_r[256], buffer[256], nome[256], *token;
    int pid = getpid(), i, fd_w, fd_r, fd_serv, nBytes, flag = 1;

    setbuf(stdout, NULL);

    sprintf(pipe_r, "%i_w", pid);
    sprintf(pipe_w, "%i_r", pid);

    if (mkfifo(pipe_r, 0777) == -1) {
        fprintf(stderr, "Erro ao criar o pipe %s!\n", pipe_r);
        flag = 0;
    }

    if (mkfifo(pipe_w, 0777) == -1) {
        fprintf(stderr, "Erro ao criar o pipe %s!\n", pipe_w);
        flag = 0;
    }

    if ((fd_r = open(pipe_r, O_RDWR)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe %s!\n", pipe_r);
        flag = 0;
    }

    if ((fd_w = open(pipe_w, O_RDWR)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe %s!\n", pipe_w);
        flag = 0;
    }

    if ((fd_serv = open(SERV_PIPE, O_WRONLY)) == -1) {
        fprintf(stderr, "Erro ao abrir o pipe %s!\n", pipe_w);
        flag = 0;
    }

    printf("Qual e o seu nome? ");
    scanf(" %255s", nome);

    sprintf(buffer, "%s %i", nome, pid);

    if (nBytes = write(fd_serv, buffer, strlen(buffer) + sizeof(char)) == -1) {
        fprintf(stderr, "Erro a escrever no pipe específicado!\n");
        flag = 0;
    }

    while (flag) {
        printf("Insira um comando: ");
        scanf(" %s", buffer);

        if (nBytes = write(fd_serv, buffer, strlen(buffer) + sizeof(char)) == -1) {
            fprintf(stderr, "Erro a escrever no pipe específicado!\n");
            break;
        }

        for (i = 0; i < strlen(buffer); i++)
            buffer[i] = toupper(buffer[i]);

        if (strcmp(buffer, "SAIR") == 0)
            flag = 0;

        token = strtok(buffer, " ");

        if ((strcmp(token, "SAIR") != 0) && (strcmp(token, "LEVANTAR") != 0) && (strcmp(token, "DEPOSITAR") != 0)) {
            if ((nBytes = read(fd_serv, buffer, sizeof(buffer) - sizeof(char))) == -1) {
                fprintf(stderr, "Erro ao ler do pipe!\n");
                flag = 0;
            } else {
                printf("\n%s\n", buffer);
            }
        }

        putchar('\n');
    }

    close(fd_r);
    unlink(pipe_r);

    close(fd_w);
    unlink(pipe_w);

    close(fd_serv);

    return 0;
}