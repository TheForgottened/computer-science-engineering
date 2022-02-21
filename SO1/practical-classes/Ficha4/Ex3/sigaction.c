#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>

void sig_handler() {

}

int main(void) {
    char s[256] = {"\0"};
    struct sigaction new_action, old_action;

    new_action.sa_handler = sig_handler;
    sigemptyset (&new_action.sa_mask);
    new_action.sa_flags = 0;

    sigaction (SIGINT, NULL, &old_action);

    if (old_action.sa_handler != SIG_IGN)
        sigaction (SIGINT, );

    return 0;
}