#include <stdio.h>
#include <string.h>
#include <ctype.h>

int contaPrimeiro(char a[]);

void main() {
	char string[100];

	gets(string);
	
	printf_s("%i\n\n\n\n\n\n", contaPrimeiro(string));
}

int contaPrimeiro(char a[]) {
	int i, vezes = 0;
	char primeiro = ' ';

	for (i = 0; i < strlen(a); i++) {
		if (primeiro == ' ') {
			primeiro = toupper(a[i]);
			vezes = 1;
		}
		else if (toupper(a[i]) == primeiro && a[i] != ' ')
			vezes++;
	}

	return vezes;
}