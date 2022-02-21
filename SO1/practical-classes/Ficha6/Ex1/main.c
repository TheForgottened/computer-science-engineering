#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

struct bruh {
    int mayContinue;
}; typedef struct bruh bruh;

void* printA(void* arg) {
    int a, i;
    bruh *aa = (bruh *)arg;

    while (1) {
        if (aa->mayContinue != 1)
            return NULL;
        
        a = rand() % 5 + 1;

        for (i = 0; i < a; i++) {
            printf(".");
            sleep(1);
        }

        for (i = 0; i < 3; i++) {
            printf("A");
            sleep(1);
        }
    }
}

void* printB(void *arg) {
    int a, i;
    bruh *bb = (bruh *)arg;

    while (1) {
        if (bb->mayContinue != 1)
            return NULL;

        a = rand() % 5 + 1;

        for (i = 0; i < a; i++) {
            printf(".");
            sleep(1);
        }

        for (i = 0; i < 3; i++) {
            printf("B");
            sleep(1);
        }
    }
}

int main() {
    setbuf(stdout, NULL);
    srand(time(NULL));
    pthread_t threads[2];
    bruh BRUH;
    char str[256];

    BRUH.mayContinue = 1;

    pthread_create(&threads[0], NULL, printA, &BRUH);
    pthread_create(&threads[1], NULL, printB, &BRUH);

    do {
        printf("\nSe pretender sair, escreva sair e pressione enter: ");
        scanf("%s", str);
    } while (strcmp(str, "sair") != 0);

    BRUH.mayContinue = 0;

    pthread_join(threads[0], NULL);
    pthread_join(threads[1], NULL);

    printf("\n");

    return 0;
}