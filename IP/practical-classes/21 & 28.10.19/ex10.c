#include <stdio.h>

void main() {

	float num = 0, numA = 0, soma = 0, media = 0, i = 0;

	printf("Insira numeros em ordem crescente:\n");
	printf("\n");
	scanf_s("%f", &num);

	soma = num;
	i = 1;

	do {
		numA = num;

		scanf_s("%f", &num);

		if (num > numA) {
			soma += num;
			i++;
		}
	} while (num > numA);

	media = soma / i;

	printf("\n");
	printf("\n");

	printf("Soma: %.2f\n", soma);
	printf("Media: %.2f\n", media);
}