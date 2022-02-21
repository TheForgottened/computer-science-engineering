#include <stdio.h>

void main() {

	int num = 1, numT = num;

	while (num < 10) {
		numT = num;
		numT *= 4;

		if (numT == num)
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
		num++;
	}

	while (num >= 10 && num < 100) {
		numT = num;
		numT *= 4;

		if (numT == (((num % 10) * 10) + (num / 10)))
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
		num++;
	}

	while (num >= 100 && num < 1000) {
		numT = num;
		numT *= 4;

		if (numT == (((num % 10) * 100) + (num / 10)))
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
		num++;
	}

	while (num >= 1000 && num < 10000) {
		numT = num;
		numT *= 4;

		if (numT == (((num % 10) * 1000) + (num / 10)))
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
		num++;
	}

	while (num >= 10000 && num < 100000) {
		numT = num;
		numT *= 4;

		if (numT == (((num % 10) * 10000) + (num / 10)))
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
		num++;
	}

	while (num >= 100000 && num < 1000000) {
		numT = num;
		numT *= 4;

		if (numT == (((num % 10) * 100000) + (num / 10))) {
			printf("O menor inteiro positivo onde se retirarmos o algarismo das unidades e o colocarmos do lado esquerdo, obtemos um numero 4 vezes maior e %i.", num);
			num = 0;
		}
		num++;
	}
}	