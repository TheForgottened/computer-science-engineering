#define MAX_OBJ 1000		// Numero maximo de objectos

// EStrutura para armazenar parametros
struct info
{
    // Tamanho da população
    int     popsize;
    // Probabilidade de mutação
    float   pm;
    // Probabilidade de recombinação
    float   pr;
    // Tamanho do torneio para seleção do pai da próxima geração
	int     tsize;
	// Constante para avaliação com penalização
	float   ro;
	// Número de objetos que se podem colocar na mochila
    int     numGenes;
	// Capacidade da mochila
	int     capacity;
	// Número de gerações
    int     numGenerations;
};

// Individuo (solução)
typedef struct individual chrom, *pchrom;

struct individual
{
    // Solução (objetos que estão dentro da mochila)
    int     p[MAX_OBJ];
    // Valor da qualidade da solução
	float   fitness;
    // 1 se for uma solução válida e 0 se não for
	int     valido;
};

void tournament(pchrom pop, struct info d, pchrom parents);

void genetic_operators(pchrom parents, struct info d, pchrom offspring);

void crossover(pchrom parents, struct info d, pchrom offspring);

void mutation(pchrom offspring,struct info d);
