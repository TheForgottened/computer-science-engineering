#include <stdio.h>

void main() {

	int p = 0, n = 0, sum = 0;

	int i = 1;

	printf("Insira um numero p: ");

	do {
		scanf_s("%i", &p);

		if (p <= 0) {
			printf("\n");
			printf("Numero invalido! Insira um inteiro positivo: ");
		}
	} while (p <= 0);

	printf("\n");
	printf("Insira um numero n: ");

	do {
		scanf_s("%i", &n);

		if (n <= 0) {
			printf("\n");
			printf("Numero invalido! Insira um inteiro positivo: ");
		}
	} while (n <= 0);

	printf("\n");

	if (n >= p) {

		while ((p * i) <= n) {
			sum += (p * i);
			i++;
		}

		printf("A soma da %i.", sum);
	}
	else
		printf("Como p e maior que n, o programa deu erro.");

}