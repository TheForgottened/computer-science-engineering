#include <stdio.h>
#include <string.h>
#include <ctype.h>

int Palavratings(char a[]);

void main() {
	char string[100];

	gets(string);

	puts(string);

	printf_s("\n\n\n");
	printf_s("%i\n\n\n\n\n\n", Palavratings(string));
}

int Palavratings(char a[]) {
	char word[100], temp[100];
	int i, j, times = 1;

	for (i = 0, j = 0; a[i] != ' '; i++, j++)
		word[j] = toupper(a[i]);

	word[j] = '\0';

	puts(word);

	for (; a[i] != '\0'; i++) {
		while (a[i] == ' ')
			i++;

		j = 0;

		while (a[i] != ' ' && a[i] != '\0') {
			temp[j] = toupper(a[i]);
			j++;
			i++;
		}

		temp[j] = '\0';

		puts(temp);

		if (strcmp(temp, word) == 0)
			times++;


	}

	puts(word);

	return times;
}