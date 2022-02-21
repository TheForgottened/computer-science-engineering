#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <pthread.h>

struct bruh {
    int mayContinue;
    int pipe;
    char pipeName[32];
    char strText[4];
}; typedef struct bruh BRUH;

BRUH esquerda, direita;
pthread_t esq, dir;

void sig_handler(int signal, siginfo_t *info, void *extra) {
    if (signal == SIGINT) {
        esquerda.mayContinue = 0;
        pthread_join(esq, NULL);

        direita.mayContinue = 0;
        pthread_join(dir, NULL);

        close(esquerda.pipe);
        close(direita.pipe);

        unlink(esquerda.pipeName);
        unlink(direita.pipeName);

        printf("\n");
        exit(1);
    }

    return;
}

void* speaker(void* arg) {
    BRUH *a = (BRUH *)arg;
    int nbytes;
    char buffer[256];
    
    if (mkfifo(a->pipeName, 0777) == -1) {
        fprintf(stderr, "Erro ao criar o pipe %s!\n", a->pipeName);
        exit(-1);
    }

    a->pipe = open(a->pipeName, O_RDWR);

    if (a->pipe == -1) {
        fprintf(stderr, "Erro a abrir o pipe %s!\n", a->pipeName);
        exit(-1);
    }

    while (a->mayContinue == 1) {
        nbytes = read(a->pipe, buffer, sizeof(buffer) - sizeof(char));
        buffer[nbytes - 1] = '\0';
        printf("%s - %s\n", a->strText, buffer);
    }

}

int main() {
    setbuf(stdout, NULL);
    strcpy(esquerda.pipeName, "p_esq");
    strcpy(direita.pipeName, "p_dir");

    strcpy(esquerda.strText, "esq");
    strcpy(direita.strText, "dir");

    esquerda.mayContinue = 1;
    direita.mayContinue = 1;
    
    pthread_create(&esq, NULL, speaker, &esquerda);
    pthread_create(&dir, NULL, speaker, &direita);

    struct sigaction action;
    action.sa_flags = SA_SIGINFO;
    action.sa_sigaction = sig_handler;

    if (sigaction(SIGINT, &action, NULL) == -1) {
        fprintf(stderr, "Erro ao colocar a função ao serviço do sinal.\n");
        exit(-1);
    }

    while (1)
        sleep(5);

    return 0;
}