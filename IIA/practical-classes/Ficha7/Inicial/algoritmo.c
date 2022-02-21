#include <stdio.h>
#include <stdlib.h>
#include "algoritmo.h"
#include "funcao.h"
#include "utils.h"

#define PROB 0.01

// Gera um vizinho
// Parametros: solucao actual, vizinho, numero de vertices
//swap two vertices
void gera_vizinho(int a[], int b[], int n)
{
    int i, p1, p2;

    for(i=0; i<n; i++)
        b[i]=a[i];
	// Encontra posicao com valor 0
    do
        p1=random_l_h(0, n-1);
    while(b[p1] != 0);
	// Encontra posicao com valor 0
    do
        p2=random_l_h(0, n-1);
    while(b[p2] != 1);
	// Troca
    b[p1] = 1;
    b[p2] = 0;
}

// Trepa colinas first-choice
// Parametros: solucao, matriz de adjacencias, numero de vertices e numero de iteracoes
// Devolve o custo da melhor solucao encontrada
/* int trepa_colinas(int sol[], int *mat, int vert, int num_iter)
{
    int *nova_sol1, *nova_sol2, custo, custo_viz1, custo_viz2, i;

	nova_sol1 = malloc(sizeof(int)*vert);
    nova_sol2 = malloc(sizeof(int)*vert);

    if(nova_sol1 == NULL || nova_sol2 == NULL)
    {
        printf("Erro na alocacao de memoria");
        exit(1);
    }
	// Avalia solucao inicial
    custo = calcula_fit(sol, mat, vert);
    for(i=0; i<num_iter; i++)
    {
		// Gera vizinho
		gera_vizinho(sol, nova_sol1, vert);
        gera_vizinho(sol, nova_sol2, vert);
		// Avalia vizinho
		custo_viz1 = calcula_fit(nova_sol1, mat, vert);
        custo_viz2 = calcula_fit(nova_sol2, mat, vert);
		// Aceita vizinho se o custo diminuir (problema de minimizacao)
        if(custo_viz1 <= custo && custo_viz1 <= custo_viz2)
        {
			substitui(sol, nova_sol1, vert);
			custo = custo_viz1;
        } else if(custo_viz2 <= custo && custo_viz2 <= custo_viz1) {
            substitui(sol, nova_sol2, vert);
			custo = custo_viz2;
        } else 
    }
    free(nova_sol1);
    free(nova_sol2);
    return custo;
} */

int trepa_colinas(int sol[], int *mat, int vert, int num_iter)
{
    int *nova_sol, custo, custo_viz, i;

	nova_sol = malloc(sizeof(int)*vert);

    if(nova_sol == NULL)
    {
        printf("Erro na alocacao de memoria");
        exit(1);
    }
	// Avalia solucao inicial
    custo = calcula_fit(sol, mat, vert);
    for(i=0; i<num_iter; i++)
    {
		// Gera vizinho
		gera_vizinho(sol, nova_sol, vert);
		// Avalia vizinho
		custo_viz = calcula_fit(nova_sol, mat, vert);
		// Aceita vizinho se o custo diminuir (problema de minimizacao)
        if(custo_viz <= custo)
        {
			substitui(sol, nova_sol, vert);
			custo = custo_viz;
        } else {
            if (rand_01() < PROB) {
                substitui(sol, nova_sol, vert);
                custo = custo_viz;
            }
        }
    }
    free(nova_sol);
    return custo;
}
