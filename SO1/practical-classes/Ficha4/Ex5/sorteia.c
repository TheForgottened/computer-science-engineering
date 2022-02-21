#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>
#include <stdlib.h>

int a;

void sig_handler(int signal, siginfo_t *info, void *extra) {
    int valor = info->si_value.sival_int;
    int pid = info->si_pid;

    union sigval sv;

    // printf("Recebi signal: %d, número [%d], do processo com o pid %i.\n", signal, valor, pid);

    if (signal == SIGUSR1) {
        if (valor < a) {
            sv.sival_int = -1;
            sigqueue(pid, SIGUSR2, sv);
        } else if (valor > a){
            sv.sival_int = 1;
            sigqueue(pid, SIGUSR2, sv);
        } else {
            // a = rand()%101;
            // printf("O numero escolhido foi: %i\n", a);
            sv.sival_int = 0;
            sigqueue(pid, SIGUSR2, sv);
            remove("TEMPFILE");
            exit(0);
        }      
    }
}

int main (int argc, char *argv[]) {
    FILE *f = fopen("TEMPFILE", "rb");

    if (f != NULL) {
        printf("Servidor já está a correr, RIP!\n");
        fclose(f);
        return 0;
    } else {
        f = fopen("TEMPFILE", "wb");
    }

    if (fork() == 0)
        printf("Sou filho, vou ficar a correr em background!\n");
    else {
        printf("O meu filho e gay, nao vale a pena tar vivo... Vou-me matar.\n");
        exit(0);
    }

    int score, pid = getpid();
    char c;

    struct sigaction action;
    action.sa_flags = SA_SIGINFO;
    action.sa_sigaction = sig_handler;

    srand(time(NULL));
    setbuf(stdout, NULL);
    
    printf("Ola! Chamo-me %i.\n", pid);;

    if (sigaction(SIGUSR1, &action, NULL) == -1) {
        printf("\nErro ao colocar a função ao serviço do sinal.");
        return -1;
    }

    a = rand()%101;
    printf("O numero escolhido foi: %i\n", a);

    while (1) {
        sleep(1);
    }

    return 0;
}