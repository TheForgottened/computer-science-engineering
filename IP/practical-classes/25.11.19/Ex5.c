#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>

#define BOOL int
#define FALSE 0
#define TRUE 1
#define N 10

void main() {
	char x[N];
	int n;

	n = BuildNumber(x, N);
	printf("Numero de %1.1d digitos \n", n);

	if (Simetric(x, n))
		printf("Capicua\n");
	else
		printf("Nao e capicua\n");
}

int BuildNumber(char a[], int b){
	int p = 0;
	char c;
	BOOL digitos = TRUE;

	while (digitos && p < b) {
		c = getchar();
		if (isdigit(c))
			a[p++] = c;
		else
			digitos = FALSE;
	}

	return p;
}

BOOL Simetric(char a[], int b) {

	int start, end;

	for (start = 0, end = (b - 1); start < (b / 2); start++, end--)
		if (a[end] != a[start])
			return FALSE;

	return TRUE;
}
