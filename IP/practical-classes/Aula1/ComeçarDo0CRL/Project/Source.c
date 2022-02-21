#include <stdio.h>

#define taxaN 1.25

float obterHora();

float obterPH();

int obterTH();

float calcula(float H, float pH, int tH);

float mostrarPagamento(float p, int th);

void main()
{
	float hora = 0, precoHora = 0, salario = 0;

	int tipoHora = 0;

	hora = obterHora();

	precoHora = obterPH();

	tipoHora = obterTH();

	salario = calcula(hora, precoHora, tipoHora);

	mostrarPagamento(salario, tipoHora);
}

float obterHora()
{
	float h = 0;

	printf("Insira o numero de horas que trabalhou: ");
	scanf_s("%f", &h);

	return h;
}

float obterPH()
{
	float ph = 0;

	printf("Insira quanto recebe por hora: ");
	scanf_s("%f", &ph);

	return ph;
}

int obterTH()
{
	int th = 0;

	printf("Qual o tipo de horario? Insira 1 para noturno e 0 para diurno. ");
	scanf_s("%i", &th);
	printf("\n");
	printf("\n");

	return th;
}

float calcula(float H, float pH, int tH)
{
	float pagamento = 0;

	if (tH == 1)
		pagamento = H * pH * taxaN;
	else if (tH == 0)
		pagamento == H * pH;
	else
		printf("Horario invalido! ");

	return pagamento;
}

float mostrarPagamento(float p, int th)
{
	if (th == 1 || th == 0)
		printf("Vai receber %.2f euros.", p);
	else
		printf("Nao foi possivel calcular o seu salario.");

}
