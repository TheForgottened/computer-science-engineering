#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main() {
    int a = 10;
    
    if (fork() == 0)
        a++;
    else
        a--;
        
    printf("\na = %d\n", a);
    
    return 0;
}