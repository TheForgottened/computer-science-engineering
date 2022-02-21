#include <stdio.h>
#define N 10

void EscreverVetor(int a[], int b);
void reuniao(int a[], int b[], int n, int r[]);
void intersecao(int a[], int b[], int n, int r[]);
void diferenca(int a[], int b[], int n, int r[]);

void main() {
	int a[N] = {1,0,0,0,1,1,0,0,0,1};
	int b[N] = {1,0,0,0,0,0,0,0,0,1};
	int c[N];

	EscreverVetor(a, N);
	printf("\n");
	EscreverVetor(b, N);
	printf("\n\n");

	printf("Reuniao: ");
	reuniao(a, b, N, c);
	printf("\n");
	EscreverVetor(c, N);
	printf("\n\n");

	printf("Intersecao: ");
	intersecao(a, b, N, c);
	printf("\n");
	EscreverVetor(c, N);
	printf("\n\n");

	printf("Diferenca: ");
	diferenca(a, b, N, c);
	printf("\n");
	EscreverVetor(c, N);
}

void EscreverVetor(int a[], int b) {
	int i = 0;

	while (i <= (b - 1)) {
		printf("%i ", a[i]);

		i++;
	}

	return;
}

void reuniao(int a[], int b[], int n, int r[]) {
	int i = 0;

	for (i = 0; i < n; i++)
		if (a[i] || b[i])
			r[i] = 1;
		else
			r[i] = 0;

	return;
}

void intersecao(int a[], int b[], int n, int r[]) {
	int i = 0;

	for (i = 0; i < n; i++)
		if (a[i] && b[i])
			r[i] = 1;
		else
			r[i] = 0;

	return;
}

void diferenca(int a[], int b[], int n, int r[]) {
	int i = 0;

	for (i = 0; i < n; i++)
		if (a[i] && !b[i])
			r[i] = 1;
		else
			r[i] = 0;

	return;
}
