#include <stdio.h>
#include <string.h>

#define N	5

void escreve_sin(char *sin[][2], int tot_lin){
	int i;

    for(i = 0; i < tot_lin; i++){
        printf("%10s = %-10s\n", *(sin[i] + 0), *(sin[i] + 1));
    }
}

char *pesquisa_sinonimo(char *sin[][2], int tot_lin, char *p){
    int i, j;

    for(i = 0; i < tot_lin; i++){
        for(j = 0; j < 2; j++)
            if(strcmp(p, *(sin[i] + j)) == 0){
                if(j == 0)
                    return *(sin[i] + 1);
                else
                    return *(sin[i]);                               
            }
    }

    return NULL;
}

void frase(char *sin[][2], int tot_lin){
    char *f;

    printf("Insira uma frase: ");
    gets(f);

}

int repetidas(char *sin[][2], int tot_lin){
    int i, j, k, l, repetidas = 0;

/*
    for(i = 0; i < tot_lin; i++){
        for(j = 1; (i + j) < tot_lin; j++){
            if(strcmp(*(sin[i] + 0), *(sin[i + j] + 0)) == 0)
                repetidas++;              
            else if(strcmp(*(sin[i] + 1), *(sin[i + j] + 1)) == 0)
                repetidas++;
            else if(strcmp(*(sin[i] + 1), *(sin[i + j] + 0)) == 0)
                repetidas++;
            else if(strcmp(*(sin[i] + 0), *(sin[i + j] + 1)) == 0)
                repetidas++;
        }
    }
*/

    for(i = 0; i < tot_lin; i++){
        for(j = 1; (i + j) < tot_lin; j++){
            for(k = 0; k < 2; k++){
                if(strcmp(*(sin[i] + 0), *(sin[i] + k)) == 0 && k != 0)
                    printf("%s %s\n", *(sin[i] + 0), *(sin[i] + k));

                for(l = 0; l < 2; l++)
                    if(strcmp(*(sin[i] + k), *(sin[i + j] + l)) == 0)
                        repetidas++;
            }
        }
    }

/*    
    for(i = 0; i < tot_lin; i++){
        for(k = 0; k < 2; k++){
            if(strcmp(*(sin[i] + 0), *(sin[i] + k)) == 0 && k != 0)
                printf("%s %s\n", *(sin[i] + 0), *(sin[i] + k));

            for(j = 1; (i + j) < tot_lin; j++){
                for(l = 0; l < 2; l++)
                    if(strcmp(*(sin[i] + k), *(sin[i + j] + l)) == 0)
                        repetidas++;
            }
        }
    }
*/

    return repetidas;
}


int main(){

	char palavra[50], *p;
	
	char *s[N][2] = {{"estranho", "bizarro"},
					 {"desconfiar", "desconfiar"},
					 {"vermelho", "encarnado"},
					 {"duvidar", "brah"},
					 {"carro", "automovel"}};	

	
	escreve_sin(s, N);		// alinea 22.1
	
	printf("\nPalavra a pesquisar: ");
	scanf(" %s", palavra);
	
	p = pesquisa_sinonimo(s, N, palavra); // alinea 22.2
	
	if(p == NULL)
		printf("A palavra %s nao tem sinonimo conhecido\n", palavra);
	else
		printf("A palavra %s e sinonimo de %s\n", p, palavra);
    
    printf("\n\nNumero de repetidas: %i", repetidas(s, N));
	
	return 0;
}