#include <stdio.h>

void pagamentoIliquido()
{
    float horas, precoHora, pagamentoIliquido;

    printf("Insira o numero de horas que trabalhou: ");
    scanf("%f", &horas);
    printf("Insira quanto recebe por hora: ");
    scanf("%f", &precoHora);

    pagamentoIliquido = horas * precoHora;

    printf("\n");
    printf("%.2f e o seu salario.", pagamentoIliquido);
}

void pagamentoIliquido2()
{
    float horas, precoHora, taxa_n = 1.25 ,pagamentoIliquido2;
    int tipoHorario;

    printf("Que tipo de horario? Insira 1 para noturno: ");
    scanf("%i", &tipoHorario);
    printf("Insira o numero de horas que trabalhou: ");
    scanf("%f", &horas);
    printf("Insira quanto recebe por hora: ");
    scanf("%f", &precoHora);


    if (tipoHorario == 1)
        pagamentoIliquido2 = horas * precoHora * taxa_n;
    else
        pagamentoIliquido2 = horas * precoHora;

       printf("%.2f e o seu salario.", pagamentoIliquido2);
}

void pagamentoIliquido3()
{
    fflush;

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

void main()
{
    int opcao = 0;

    printf("1 - pagamentoIliquido;\n2 - pagamentoIliquido2;\n3 - pagamentoIliquido3.\n");
    printf("Escolha uma das funcoes.\n");
    scanf("%i", &opcao);

    printf("\n");

    if (opcao == 1)
        pagamentoIliquido();
    else if (opcao == 2)
        pagamentoIliquido2();
    else if (opcao == 3)
        pagamentoIliquido3();
    else
        printf("ERROR 404: FUNCTION NOT FOUND");

    int esperar;

    scanf("%i", esperar);
}
