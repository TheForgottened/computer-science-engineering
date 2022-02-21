#include <stdio.h>

void main() {

	int max = 0, num = 0, pos = 1, posTemp = 0, tam = 0;

	printf("Insira os numeros que pretende. Para fechar a sequencia, insira 0.");
	printf("\n");
	printf("\n");

	do {
		do {
			scanf_s("%i", &num);

			if (num < 0) {
				printf("Numero invalido! Insira um valor inteiro positivo ou nulo.\n");
				printf("\n");
			}
		} while (num < 0);

		posTemp++;

		if (num > max) {
			max = num;
			pos = posTemp;
		}

		tam++;
	} while (num != 0);

	printf("Maximo: %i\tSurgiu na posicao: %i\tTamanho da sequencia: %i", max, pos, tam);
}