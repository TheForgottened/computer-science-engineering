#include <stdio.h>

void main()
{

    float horas, precoHora, taxa_n = 1.25, pagamentoIliquido3;
    int dia = 1, tipoHorario;

    printf("Insira quanto recebe por hora: ");
    scanf("%f", &precoHora);

    printf("\n");
    printf("\n");

    while (dia <= 7)
    {
        printf("Dia %i\n", dia);

        printf("Que tipo de horario? Insira 1 para noturno: ");
        scanf("%i", &tipoHorario);
        printf("Insira o numero de horas que trabalhou: ");
        scanf("%f", &horas);

        if (tipoHorario == 1)
            pagamentoIliquido3 += (horas * precoHora * taxa_n);
        else
            pagamentoIliquido3 += (horas * precoHora);

        printf("\n");
        dia ++;
    }

    printf("%.2f e o seu salario.", pagamentoIliquido3);
}
