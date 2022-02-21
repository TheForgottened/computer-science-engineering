#include <stdio.h>

int a = 4;

int func3(int a) {

	int x = a++;

	return x;

}

void main() {
	fprintf(stdout, "x = %2.2d\n", func3(a++));
	fprintf(stdout, "a = %2.2d\n", a);

	printf("Matem-me, isto serve so para entendermos variaveis locais... Que ranco do caralho.");
}