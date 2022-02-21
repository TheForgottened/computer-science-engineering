#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>

void sig_handler(int signal, siginfo_t *info, void *extra) {
    int valor = info->si_value.sival_int;
    int pid = info->si_pid;

    if (valor < -1 || valor > 1) {
        perror("Deu asneira colega.");
        return;
    }

    if (valor == -1)
        printf("O numero a adivinhar e maior!\n");
    else if (valor == 1)
        printf("O numero a adivinhar e menor!\n");
    else {
        printf("Parabens! Acertaste e nao ganhaste nada!\n\n");
        exit(0);
    }
}

int main (int argc, char *argv[]) {
    if (argc != 2) {
        printf("Numero invalido de argumentos!\n");
        return -1;
    }

    int pid = getpid(), pid_d = atoi(argv[1]), num;

    union sigval sv;

    struct sigaction action;
    action.sa_flags = SA_SIGINFO;
    action.sa_sigaction = sig_handler;

    if (sigaction(SIGUSR2, &action, NULL) == -1) {
        printf("\nErro ao colocar a função ao serviço do sinal.");
        return -1;
    }

    setbuf(stdout, NULL);

    printf("Ola! Eu sou o %i e o meu primo e o %i\n", pid, pid_d);

    while (1) {
        printf("Tenta adivinhar o numero que o meu primo esta a pensar! ");
        scanf("%i", &num);
        sv.sival_int = num;
        sigqueue(pid_d, SIGUSR1, sv);
        sleep(5);
    }

    return 0;
}