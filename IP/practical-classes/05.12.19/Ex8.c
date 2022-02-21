#include <stdio.h>
#define DIM_TAB 10

void PrinterOfTheBiceps(int a[], int size);
void OrdenatingsInTheBanks(int a[], int size);

void main() {
	int array[DIM_TAB] = { 1, 2, 3, 4, 6, -9, -10, -11, -16, -27 };

	OrdenatingsInTheBanks(array, DIM_TAB);
}

void PrinterOfTheBiceps(int a[], int size) {
	int i;

	for (i = 0; i < size; i++) {
		printf("%i ", a[i]);
	}
}

void OrdenatingsInTheBanks(int a[], int size){
	int i, j, max = a[0];

	for (j = 1; a[j] >= max; j++)
		max = a[j];

	printf("%i ", max);

	for (i = (j - 2); i >= 0 && j < size; )
		if (a[i] >= a[j])
			printf("%i ", a[i--]);
		else
			printf("%i ", a[j++]);

	if (i < 0) // atingiu limite à esquerda
		while (j < size)
			printf("%i ", a[j++]);
	else // atingiu limite à direita
		while (i >= 0)
			printf("%i ", a[i--]);

	printf("\n\n\n");
}