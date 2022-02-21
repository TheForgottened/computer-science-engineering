#include <stdio.h>
#define N 5

float media(int a[], int b);

void check(int a[]);

void main() {
	int x[N], j = 0;

	printf("Insira 5 valores para o vetor:\n");

	while (j < N) {
		scanf_s("%i", &x[j]);
		j++;
	}

	printf("\n");
	printf("\nMedia e %.2f.\n", media(x, N));

	printf("\nNovo vetor e: ");
	check(x);

	j = 0;

	while (j < N) {
		printf("%i ", x[j]);
		j++;
	}
}

float media(int a[], int b) {
	int i = 0, sum = 0;


	while (i < b) {
		sum += a[i];
		i++;
	}

	return ((float)sum / b);
}

void check(int a[]) {

	int i;

	for (i = 0; i < N; i++) {
		if (a[i] < media(a, N)) {
			a[i] = 0;
		}
	}

	return;
}