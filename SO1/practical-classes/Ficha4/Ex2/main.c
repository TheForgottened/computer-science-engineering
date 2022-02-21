#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <time.h>

int N = 20, error = 2, score = 0;

void sig_handler(int signum) {
    if (signum != SIGALRM)
        return;
    
    printf("\nDemorou demasiado tempo!\nTeve um score de %i!\n", score);
    kill(getpid(), SIGINT);
}

int main(void) {
    srand(time(NULL));
    setbuf(stdout, NULL);

    int a, b, r;

    signal(SIGALRM, sig_handler);
    alarm(N);

    do {       
        a = rand()%101;
        b = rand()%101;

        printf("%i + %i = ", a, b);
        scanf("%i", &r);

        if (r != (a + b))
            error--;
        else {
            score++;
            sig_handler(0);
        }

        N--;
    } while (error > 0 && N > 0);

    printf("Teve um score de %i!\n", score);
    return 0;
}