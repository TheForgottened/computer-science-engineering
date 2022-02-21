#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <pthread.h>

int fd_pipe;

void sig_handler(int signal, siginfo_t *info, void *extra) {
    if (signal == SIGINT) {
        close(fd_pipe);
        printf("\n");
        exit(1);
    }

    return;
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Uso inválido! Deve ser invocado do tipo './producer <nome do pipe a usar>\n");
        exit(-1);
    }

    setbuf(stdout, NULL);

    int inteiro, n;
    char buffer[32];

    fd_pipe = open(argv[1], O_WRONLY);

    if (fd_pipe == -1) {
        fprintf(stderr, "Erro ao abrir o pipe específicado!\n");
        exit(-1);
    }

    struct sigaction action;
    action.sa_flags = SA_SIGINFO;
    action.sa_sigaction = sig_handler;

    if (sigaction(SIGINT, &action, NULL) == -1) {
        fprintf(stderr, "Erro ao colocar a função ao serviço do sinal.\n");
        exit(-1);
    }

    while (1) {
        printf("Nome a enviar: ");
        scanf(" %10s", buffer);
        printf("Inteiro a enviar: ");
        scanf("%i", &inteiro);
        sprintf(buffer, "%s %i", buffer, inteiro);

        printf("\n%s", buffer);

        n = write(fd_pipe, buffer, strlen(buffer) + sizeof(char));

        if (n == -1) {
            fprintf(stderr, "Erro a escrever no pipe específicado!\n");
            close(fd_pipe);
            exit(-1);
        }

        printf("\n");
    }

    return 0;
}