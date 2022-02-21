#include <stdio.h>
#define L1 3
#define C1 2
#define L2 4
#define C2 3

/*void escreve(int n_lin, int n_col, int v[n_lin][n_col]){
    int k;

    for(k = 0; k < n_lin * n_col; k++){
        printf("%6i", *(v[0] + k));

        if((k + 1) % n_col == 0)
            putchar('\n');
    }
} */

void escreve(int n_lin, int n_col, int v[n_lin][n_col]){
    int *p;

    for(p = v[0]; p < v[0] + n_lin * n_col; p++){
        printf("%6i", *p);

        if((p - v[0] + 1) % n_col == 0)
            putchar('\n');
    }
}

int main(){
    int mat1[L1][C1]={{1,2},{3,4},{5,6}};
    int mat2[L2][C2]={{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
    
    printf("\nMatriz mat1:\n");
    escreve(L1, C1, mat1);
    
    printf("\nMatriz mat2:\n");
    escreve(L2, C2, mat2);
    
    return 0; 
}