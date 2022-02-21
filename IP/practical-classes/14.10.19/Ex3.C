#include <stdio.h>

void main() {

	int nali = 0, ali = 0, produtos = 0;
	float pnali = 0, pali = 0, preco = 0, precoIVA = 0;

	float IVAali = 1.06, IVAnali = 1.23;

	do {
		printf("Insira o numero de artigos nao alimentares: ");
		scanf_s("%i", &nali);

		if (nali < 0) {
			printf("Numero de artigos invalido!\n");
			printf("\n");
		}
	} while (nali < 0);

	do {
		printf("Insira o preco dos artigos nao alimentares: ");
		scanf_s("%f", &pnali);

		if (pnali < 0) {
			printf("Preco invalido!\n");
			printf("\n");
		}
	} while (pnali < 0);

	printf("\n");

	preco += (nali * pnali);
	precoIVA += (nali * pnali * IVAnali);

	do {
		printf("Insira o numero de artigos alimentares: ");
		scanf_s("%i", &ali);

		if (ali < 0) {
			printf("Numero de artigos invalido!\n");
			printf("\n");
		}
	} while (ali < 0);

	do {
		printf("Insira o preco dos artigos alimentares: ");
		scanf_s("%f", &pali);

		if (pali < 0) {
			printf("Preco invalido!\n");
			printf("\n");
		}
	} while (pali < 0);

	printf("\n");
	printf("\n");

	preco += (ali * pali);
	precoIVA += (ali * pali * IVAali);

	produtos = ali + nali;
	
	printf("Numero de produtos alimentares: %i\n", ali);
	printf("Numero de produtos nao alimentares: %i\n", nali);
	printf("Numero total de produtos: %i\n", produtos);
	printf("Preco s/ IVA: %.2f euros\n", preco);
	printf("Preco c/ IVA: %.2f euros\n", precoIVA);
}