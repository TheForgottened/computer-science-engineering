#include <stdio.h>
#include <string.h>

void separatings(char a[]);

void main() {
	char string[100];

	gets(string);

	separatings(string);

	printf("\n\n\n\n\n\n\n");
}

void separatings(char a[]) {
	int i;

	for (i = 0; i < strlen(a); i++) {
		if (a[i] == ' ' && a[i + 1] != ' ')
			printf("\n");
		else if (a[i] != ' ')
			printf("%c", a[i]);
	}
}