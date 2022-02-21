#include <stdio.h>

void main() {

	int m = 0, n = 0, num = 0, i = 0, t = 0;
	char enter;

	do {
		printf("Insira o inicio do intervalo (maior ou igual a 100): ");
		scanf_s("%i", &m);

		if (m < 100 || m > 999) {
			printf("Valor invalido!\n");
			printf("\n");
		}
	} while (m < 100 || m > 999);

	printf("\n");

	do {
		printf("Insira o fim do intervalo (menor ou igual a 999): ");
		scanf_s("%i", &n);

		if (n > 999 || n < 100) {
			printf("Valor invalido!\n");
			printf("\n");
		}
	} while (n > 999 || n < 100);

	num = m;

	printf("\n");
	printf("\n");

	printf("Os numero cuja soma do cubo dos digitos e igual a ele proprio sao:\n");

	while (num <= n) {
		t = num;

		while (t != 0) {
			i += ((t % 10) * (t % 10) * (t % 10));

			t /= 10;
		}

		if (i == num)
			printf("%i\n", i);

		num++;

		i = 0;
	}
}