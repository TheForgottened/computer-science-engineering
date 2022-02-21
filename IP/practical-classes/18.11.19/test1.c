#include <stdio.h>

float velocidade(int km, float t) {
	float temp = t;

	while (temp > 1) {
		temp -= 1;
	}

	t -= temp;
	temp *= 100;
	t *= 60;
	t += temp;

	float kmh = (km / t) * 60;

	return kmh;
}

void main() {
	int numF = 0, numT = 0, km = 0, kmTemp = 0, i = 1, f = 1;
	float t = 0, tTemp = 0;

	printf("Numero de funcionarios > ");
	scanf_s("%i", &numF);

	printf("\n");
	printf("\n");

	while (numF > f) {
		printf("Funcionario: %i\n", f);

		printf("\n");

		printf("Numero de trajetos > ");
		scanf_s("%i", &numT);
		printf("\n");

		while (numT >= i) {
			printf("Trajeto %i\n", i);
			printf("Quilometros percorridos e tempo decorrido > ");
			scanf_s("%d%f", &km, &t);

			if (velocidade(km, t) < 30)
				printf("Abaixo do limite minimo %.2f\n", velocidade(km, t));
			else if (velocidade(km, t) > 100)
				printf("Acima do limite maximo %.2f\n", velocidade(km, t));
			else
				printf("Velocidade no trajeto = %.2f\n", velocidade(km, t));

			i++;
			printf("\n");

			kmTemp += km;
			tTemp += t;
		}

		f++;

		printf("Velocidade media do funcionario = %.2f\n", velocidade(kmTemp, tTemp));
		printf("\n");
		kmTemp = 0;
		tTemp = 0;
		i = 0;
	}
}