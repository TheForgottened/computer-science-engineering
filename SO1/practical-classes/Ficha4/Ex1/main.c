#include <stdio.h>
#include <signal.h>
#include <unistd.h>

void sig_handler(int signo) {
    printf("Olá\n");
} 

int main(void) {
    if (signal(SIGINT, sig_handler) == SIG_ERR)
        printf("\nErro ao colocar a função de serviço ao SIGINT\n");
    
    do {
        sleep(1);
    } while(1);
    
    return 0;
} 