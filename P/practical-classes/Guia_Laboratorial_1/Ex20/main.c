#include <stdio.h>
#include <string.h>
#include <strings.h>
#include <ctype.h>

void translation(char s[]){
    char *en[12] = {  
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };

    char *pt[12] = {  
        "Janeiro",
        "Fevereiro",
        "Marco",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro"
    };

    int i;
    char *t = NULL;

    for(i = 0; i < 12; i++){
        if(strcmp(pt[i], s) == 0)
            t = en[i];
    }

    if(t == NULL)
        printf("\n%s nao e um nome valido.\n", s);
    else
        printf("\n%s em ingles sera %s.", s, t);
}

void main(){
    char v[20];

    printf("Insere um mes em portugues: ");
    gets(v);

    translation(v);
}