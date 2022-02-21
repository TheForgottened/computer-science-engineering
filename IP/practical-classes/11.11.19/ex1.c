#include <stdio.h>

void main() {

	int a = 0;

	printf("0 para fechar o programa.\n");
	printf("\n");

	do {

		scanf_s("%i", &a);

		if (a != 0)
			printf("%i\n", square(a));
		else if (a == 0)
			printf("Funcao terminada.");

		printf("\n");

	} while (a != 0);

}

int square(int n) {

	n *= n;

	return n;

}