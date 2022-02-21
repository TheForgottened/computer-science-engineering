#ifndef PEOPLES_H
#define PEOPLES_H

typedef struct person person, *nperson;

struct person {
    char name[100];
    int age;
    char state;
    int sickD;
    int idlocal;

    nperson next;
};

// Função responsável por ler o ficheiro txt com as pessoas a popular os locais
nperson readPeople(char *fname);

// Função cuja utilidade é colocar a última pessoa lida no final da lista ligada. No caso da lista estar vazia esta torna-se a primeira
nperson insertFinal(nperson p, nperson new);

// Função que imprime toda a lista ligada, indicando, da pessoa em questão, o nome, a idade, o estado e o número de dias a que está doente. Feita para uso em debug.
void printPeople(nperson p);

// Função que verifica se está tudo bem com as pessoas da lista ligada
nperson checkPeople(nperson p);

// Função para libertar uma lista ligada 
void freePeople(nperson p);

// Função que vai criar uma lista ligada nova igual à lista ligada inserida
nperson createEqual(nperson o);

// Função para guardar a população num ficheiro com nome fname
void savePeople(nperson p, char *fname);
#endif /*PEOPLES_H*/