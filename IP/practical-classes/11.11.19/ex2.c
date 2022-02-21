#include <stdio.h>

void main() {

	int r1 = 0, r2 = 0, n = 0;

	printf("Insira 3 numeros inteiros:\n");

	scanf_s("%i", &r1);
	scanf_s("%i", &r2);
	scanf_s("%i", &n);

	printf("\n");

	if (range(r1, r2, n) == 1)
		printf("O numero %i pertence ao intervalo %i - %i.", n, r1, r2);
	else
		printf("O numero %i nao pertence ao intervalo %i - %i.", n, r1, r2);

}

int range(int r1, int r2, int n) {

	if (n >= r1 && n <= r2)
		return 1;

	return 0;
}