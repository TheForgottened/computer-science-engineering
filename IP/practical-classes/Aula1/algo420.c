#include <stdio.h>

void main()
{
    float hora, precoHora, salario;

    obterDados(&hora, &precoHora);

    salario = calcula(hora, precoHora);

    mostrarPagamento(salario);
}

void obterDados(float h, float ph)
{
    printf("Insira o numero de horas que trabalhou: ");
    scanf("%f", &h);
    printf("Insira quanto recebe por hora: ");
    scanf("%f", &ph);

    return (h, ph);
}

void calcula(float H, float pH)
{
    float pagamento;

    pagamento = H * pH;

    return pagamento;
}

void mostrarPagamento(float p)
{
    printf("Vai receber %.2f.", p);

}
