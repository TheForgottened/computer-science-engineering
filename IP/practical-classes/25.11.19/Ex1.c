#include <stdio.h>
#define N 10

void main(){
	printf("N = %i\n\n", N);

	int array[N];
	int i = 0, sum = 0;

	while (i <= (N - 1)) {
		scanf_s("%i", &array[i]);
		printf("\n");

		i++;
	}

	i = 0;

	while (i <= (N - 1)) {
		sum += array[i];

		i++;
	}

	printf("\n");
	printf("Media e %i.", media(sum, N));

	i++;

	while (i <= (N - 1)) {
		if (array[i] < media(sum, N))
			array[i] = 0;

		i++;
	}


}

float media(int a, int b) {
	return (a / b);
}

void ler(int a[]) {

}