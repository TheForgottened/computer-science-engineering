#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#define SERV_PIPE "serv_pipe"

void main() {
    int fd_serv;

    fd_serv = open(SERV_PIPE, O_WRONLY);
}