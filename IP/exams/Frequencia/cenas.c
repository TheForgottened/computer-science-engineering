#include <stdio.h>
#define N 50

void main(){
	int i = 1, max = 0, UC, numA;

	do {
		do {
			printf("Numero de alunos inscritos no UC %i: ", i);
			scanf_s("%i", &numA);
		} while (numA < 0);

		if (numA > 0)
			printf("Numero de salas necessario para a UC %i: %i\n", i, distribuir(numA, N));

		if (distribuir(numA, N) > max) {
			max = distribuir(numA, N);
			UC = i;
		}

		i++;
	} while (numA != 0);

	printf("\nMaior numero de salas por exame: %i\n", max);
	printf("Ocorre na UC %i.", UC);
}

int distribuir(int a, int b) {
	if ((a % b) == 0)
		return (a / b);
	else
		return ((a / b) + 1);
}