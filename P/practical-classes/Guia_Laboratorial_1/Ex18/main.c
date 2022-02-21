#include <stdio.h>
#define LINES 4
#define COLUMNS 6

void media(float *m, int n_l, int n_c, float *h_m, float *l_m){
    int i, j;
    float media;

    *l_m = 0;
    *h_m = 0;

    for(i = 0, media = 0; i < n_c; i++){
        for(j = 0; j < n_l; j++){
            media += *(m + i + j * n_c);
            printf("%f\t", *(m + i + j * n_c));
        }

        printf("Premedia = %.2f\t", media);
        media /= n_l;
        printf("Media = %.2f\n", media);

        if(*l_m == 0 && *h_m == 0){
            *l_m = media;
            *h_m = media;
        } else if(media < *l_m)
            *l_m = media;
        else if(media > *h_m)
            *h_m = media;

        media = 0;
    }
}

void main(){
    float h_media, l_media;
    float v[LINES][COLUMNS] = { {1.5, 1.7, 9, 5, 6, 9.5},
                                {1, 10.3, 8, 8.95, -9.1, 9},
                                {9, 10, 15, 16, -10, 21},
                                {9.1, 7.9, 9.5, 77.7, 0.5, 76}
    };

    media(&v[0][0], LINES, COLUMNS, &h_media, &l_media);

    printf("\n\nMaior media: %.2f\n", h_media);
    printf("Menor media: %.2f\n", l_media);
}