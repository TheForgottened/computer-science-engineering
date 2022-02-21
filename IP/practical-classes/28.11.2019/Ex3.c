#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#define N 10

void nomequalquer(int j[]);
void interssection(int x[], int y[], int z[]);
void reunion(int x[], int y[], int z[]);
void minus(int x[], int y[], int z[]);
void printer(int j[]);

void main() {
	int a[N] = { 0 }, b[N] = { 0 }, I[N] = { 0 }, R[N] = { 0 }, D[N] = { 0 };

	printf("Insira os valores para o vetor a:\n");
	nomequalquer(a);

	printf("\n\nInsira os valores para o vetor b:\n");
	nomequalquer(b);

	printf("\n\n");
	printer(a);

	printf("\n\n");
	printer(b);

	interssection(a, b, I);
	reunion(a, b, R);
	minus(a, b, D);

	printf("\n\nIntersecao: ");
	printer(I);

	printf("\n\nReuniao: ");
	printer(R);

	printf("\n\nDiferenca: ");
	printer(D);

	printf("\n\n\n\n");
}

void nomequalquer(int j[]) {
	int i;
	char c;

	do {
		do {
			printf("\nNumero: ");
			scanf_s("%i", &i);
		} while (i < 0 || i > 9);

		j[i] = 1;

		printf("\nTerminar? (S/N) ");
		fflush(stdin);
		scanf_s(" %c", &c);
	} while (toupper(c) == 'N');

	return;
}

void interssection(int x[], int y[], int z[]) {
	int i;

	for (i = 0; i < N; i++) {
		if (x[i] == 1 && y[i] == 1)
			z[i] = 1;
	}

	return;
}

void reunion(int x[], int y[], int z[]) {
	int i;

	for (i = 0; i < N; i++) {
		if (x[i] == 1 || y[i] == 1)
			z[i] = 1;
	}

	return;
}

void minus(int x[], int y[], int z[]) {
	int i;

	for (i = 0; i < N; i++) {
		if (x[i] == 1 && y[i] == 0)
			z[i] = 1;
	}
	
	return;
}

void printer(int j[]) {
	int i;

	for (i = 0; i < N; i++) {
		printf("%i ", j[i]);
	}

	return;
}