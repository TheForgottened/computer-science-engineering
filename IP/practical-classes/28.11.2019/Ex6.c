#include <stdio.h>
#define TAM 15

int sumMAX(int a[], int b);

void main() {
	int i;

	printf("O array tem tamanho %i.", TAM);

	int v[TAM];

	printf("\n\nInsira os valores do array: \n");

	for (i = 1; i < TAM; i++) {
		scanf_s("%i", &v[i]);
	}


	printf("\n\nA soma do maior numero e %i.", sumMAX(v, TAM));
}

int sumMAX(int a[], int b) {
	int i, max = a[0], sum = max;

	for (i = 1; i < b; i++) {
		if (a[i] > max) {
			max = a[i];
			sum = 1;
		}
		else if (a[i] == max)
			sum += max;
	}

	return sum;
}