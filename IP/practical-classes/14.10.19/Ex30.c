#include <stdio.h>

void main() {

	int t = 0;
	float crescA = 0, crescB = 0, popA = 0, popB = 0;

	do {
		do {
			printf("Insira a populacao atual do bairro A: ");
			scanf_s("%f", &popA);
			printf("Insira a taxa anual de crescimento do mesmo: ");
			scanf_s("%f", &crescA);
		} while (popA <= 0);

		printf("\n");

		do {
			printf("Insira a populacao atual do bairro B: ");
			scanf_s("%f", &popB);
			printf("Insira a taxa anual de crescimento do mesmo: ");
			scanf_s("%f", &crescB);
		} while (popB <= 0);
	} while (crescB >= crescA);

	printf("\n");

	// transformar percentagem em decimal para facilitar o cálculo //

	crescA /= 100.0;
	crescB /= 100.0;

	crescA += 1;
	crescB += 1;

	while (popA <= popB) {
		t++;

		popA *= crescA;
		popB *= crescB;
	}

	printf("Serao precisos %i anos para a populacao de A ultrapassar a de B, ficando A com %.0f e B com %.0f.", t, popA, popB);
}