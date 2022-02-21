#include <stdio.h>
#define N 10

void ReadVector(int a[], int b)

void main() {
	int x[N];
	int n, smax;

	n = ValueInRange(1, N);

	ReadVector(x, N);

	smax = SumMax(x, n);

	printf("\nSoma dos valores maximos e %d\n", smax);
}

int SumMax(int a[], int b) {
	int i, max, sum;

	sum = max = a[0];

	for (i = 1; i < b; i++)
		if (a[i] > max)
			sum = max = a[i];
		else if (a[i] == max)
			sum += max;

	return sum;
}

void ReadVector(int a[], int b) {
	int i;

	for (i = 0; i < b - 1; i++)
		scanf_s("%i", &a[i])
}