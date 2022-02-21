#include <stdio.h>
#include <string.h>

#define TAM 100

void main() {
	char str[TAM];
	int i;

	gets(str);

	printf("\n\n");
	for (i = (strlen(str) - 1); i >= 0; i--) {
		printf("%c", str[i]);
	}

	printf("\n\n\n");
}