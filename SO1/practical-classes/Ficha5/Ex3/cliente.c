#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

#define SERV_PIPE "SERV_PIPE"

void main() {
    int fd_serv;

    fd_serv = open(SERV_PIPE,);
}