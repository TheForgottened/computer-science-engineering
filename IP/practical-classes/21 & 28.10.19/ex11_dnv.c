#include <stdio.h>

void main() {

	int aulasT = 50, aulasF = 0;
	int faltas = 0;
	float test1 = 0, test2 = 0, media = 0;
	int rep = 0;
	int aprovados = 0, reprovados = 0, reprovadosM = 0;


	do {
		
		rep = 0;

		printf("Insira o numero de aulas que o aluno faltou: \n");

		do {
			scanf_s("%i", &aulasF);

			if (aulasF < 0) {
				printf("\n");
				printf("Numero invalido! Insira um numero igual ou superior a 0.");
				printf("\n");
			}
		} while (aulasF < 0);

		printf("\n");

		if (aulasF > (aulasT * 0.25)) {
			printf("Este aluno faltou a mais de 25%% das aulas, logo reprovou. \n");
			printf("Se desejar insirir mais algum aluno escreva 1, se nao escreva 0.\n");

			do {
				scanf_s("%i", &rep);

				if (rep != 0 && rep != 1) {
					printf("\n");
					printf("Numero invalido! Insira 1 ou 0.\n");
				}
			} while (rep != 0 && rep != 1);

			reprovados++;
		}
		else if (aulasF <= (aulasT * 0.25)) {
			printf("Insira a nota do primeiro teste numa escala de 0 - 20.\n");

			do {
				scanf_s("%f", &test1);

				if (test1 < 0 || test1 > 20) {
					printf("\n");
					printf("Nota invalida! Insira um valor entre 0 e 20.\n");
				}
			} while (test1 < 0 || test1 > 20);

			printf("\n");
			printf("Insira a nota do segundo teste numa escala de 0 - 20.\n");

			do {
				scanf_s("%f", &test2);

				if (test2 < 0 || test2 > 20) {
					printf("\n");
					printf("Nota invalida! Insira um valor entre 0 e 20.\n");
				}
			} while (test2 < 0 || test2 > 20);
		}

		media += ((test1 + test2) / 2);

		printf("\n");

		if (((test1 + test2) / 2) < 9.5 && aulasF <= (aulasT * 0.25)) {
			printf("Este aluno chumbou com media de %.2f.\n", ((test1 + test2) / 2));

			reprovados++;
			reprovadosM++;

			printf("Se desejar insirir mais algum aluno escreva 1, se nao escreva 0.\n");

			do {
				scanf_s("%i", &rep);

				if (rep != 0 && rep != 1) {
					printf("\n");
					printf("Numero invalido! Insira 1 ou 0.\n");
				}
			} while (test2 < 0 || test2 > 20);
		}
		else if (((test1 + test2) / 2) > 9.5 && aulasF <= (aulasT * 0.25)) {
			printf("Este aluno foi aprovado com media de %.2f.\n", ((test1 + test2) / 2));

			aprovados++;

			printf("Se desejar insirir mais algum aluno escreva 1, se nao escreva 0.\n");

			do {
				scanf_s("%i", &rep);

				if (rep != 0 && rep != 1) {
					printf("\n");
					printf("Numero invalido! Insira 1 ou 0.\n");
				}
			} while (test2 < 0 || test2 > 20);
		}

		printf("\n");
	} while (rep == 1);

	media /= (reprovadosM + aprovados);

	printf("\n");
	printf("A turma tem %i alunos aprovados, %i alunos reprovados. ", aprovados, reprovados);
	if (media < 0)
		printf("A media da turma e %.2f.", media);
	else
		printf("A media nao ta disponivel.");
}