#include <stdio.h>
#define TAM 10

int PositionInTheBanks(int a[], int size);
void GoToTheRight(int a[], int size);
void PrinterOfTheBiceps(int a[], int size);
void MaxToTheRight(int a[], int size);

void main() {
	int v[TAM], i;

	for (i = 0; i < TAM; i++) {
		printf("Indique o elemento %i: ", (i + 1));
		scanf_s("%i", &v[i]);
		printf("\n");
	}

	printf("\nMaior valor surge na posicao %i.\n", PositionInTheBanks(v, TAM));

	GoToTheRight(v, TAM);

	printf("\nMovendo tudo para a direita e o ultimo para o inicio, o vetor fica: ");
	PrinterOfTheBiceps(v, TAM);
	
	MaxToTheRight(v, TAM);

	printf("\n\nFazendo constantes rotacoes para a direita ate o maximo fica no extremo, o vetor fica: ");
	PrinterOfTheBiceps(v, TAM);

	printf("\n\n\n");
}

void PrinterOfTheBiceps(int a[], int size) {
	int i;

	for (i = 0; i < size; i++) {
		printf("%i ", a[i]);
	}
}

int PositionInTheBanks(int a[], int size) {
	int i, pos = a[0];

	for (i = 1; i < size; i++) {
		if (a[i] >= a[pos])
			pos = i;
	}

	return (pos + 1);
}

void GoToTheRight(int a[], int size) {
	int i, temp = a[(size - 1)];

	for (i = (size - 1); i > 0; i--) {
		a[i] = a[i - 1];
	}

	a[0] = temp;
}

void MaxToTheRight(int a[], int size) {
	int i, temp, max = a[0];

	for (i = 1; i < size; i++) {
		if (a[i] > max)
			max = a[i];
	}
	
	do {
		temp = a[(size - 1)];

		for (i = (size - 1); i > 0; i--) {
			a[i] = a[i - 1];
		}

		a[0] = temp;
	} while (a[(size - 1)] != max);
}