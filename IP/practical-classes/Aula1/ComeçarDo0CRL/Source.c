#include <stdio.h>

void main()
{
	float hora = 0, precoHora = 0, salario = 0;

	obterDados(&hora, &precoHora);

	salario = calcula(hora, precoHora);

	mostrarPagamento(salario);
}

float obterDados(float h, float ph)
{
	h = 0; ph = 0;

	printf("Insira o numero de horas que trabalhou: ");
	scanf_s("%f", &h);
	printf("Insira quanto recebe por hora: ");
	scanf_s("%f", &ph);

	return 0;
}

float calcula(float H, float pH)
{
	float pagamento;

	pagamento = H * pH;

	return pagamento;
}

float mostrarPagamento(float p)
{
	printf("Vai receber %.2f.", p);

}
