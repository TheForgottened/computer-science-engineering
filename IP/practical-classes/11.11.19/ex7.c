#include <stdio.h>

void main() {

	int num = 0, linhas = 0;

	scanf_s("%i", &num);

	printf("\n");

	int numT = num;

	if (num > 1 && num <= 9) {
		do {
			drawS(num);
			drawF(num);

			printf("\n");
			linhas++;

			spaces(num, linhas);

			num -= 1;
		} while (num != 1);

		printf("1");

		printf("\n");
		printf("\n");
	}
}

int drawS(int n) {

	int i = 1;

	do {
		printf("%i ", (n - (n - i)));
		i++;
	} while ((n - (n - i)) != (n + 1));

	return 0;
}

int drawF(int n) {

	int i = 1;

	do {
		printf("%i ", (n - i));
		i++;
	} while ((n - i) != 0);

	return 0;

}

int spaces(int n, int l) {

	int x = 0;

	do {
		printf("  ");
		x++;
	} while (x != l);

	return 0;
}