#include <stdio.h>

void exmain1() {

	int expoente = 0, p = 0;
	float base = 0, potencia = 1;

	do {
		printf("Base: ");
		scanf_s("%f", &base);
		printf("Expoente: ");
		scanf_s("%i", &expoente);

		if (expoente <= 0) {
			printf("\n");
			printf("Expoente invalido! Insira um numero inteiro positivo.\n");
			printf("\n");
		}
	}
	while (expoente <= 0);

	while (p < expoente) {
		potencia *= base;
		p ++;
	}

	printf("\n");
	printf("O numero %.2f elevado a %i e igual a %.2f.", base, expoente, potencia);
}