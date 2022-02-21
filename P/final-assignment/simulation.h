#ifndef SIM_H
#define SIM_H
typedef struct person person, *nperson;

typedef struct sala local, *plocal;

// Função que imprime o menu da fase de simulação e verifica se a opção inserida é válida
int menuSim(int d, int b);

// Função que distribui as pessoas pelos locais, aleatoriamente, verificando se respeita a capacidade máxima de cada. Quando a capacidade máxima do espaço é antigida, o resto das pessoas são ignoradas
void assignLocal(nperson p, local *e, int eSize);

// Função que mostra as estatística
void showStatistics(nperson p, local *e, int eSize);

// Função para mover X pessoas dum local para outro desde que estes tenham ligação direta
void movePeople(nperson p, local *e, int eSize);

// Função para recuar X dias na simulação
nperson goBack(nperson current, nperson *one, nperson *two, nperson *three, int *day, int *back);

// Função para avançar 1 interação na simulação
void nextDay(nperson p, local *e, int eSize, int day);

// Insere uma pessoa doente no final da lista ligada. Recebe o espaço para poder verificar que o que é inserido está correto / faz sentido.
nperson insertSFinal(nperson p, local *e, int eSize);

// Preenche uma estrutura do tipo person com a pessoa doente por defeito. Recebe o espaço para poder verificar que o ID para o qual a pessoa é associada existe de modo a não causar problemas na execução do programa.
void fillSick(nperson new, nperson p, local *e, int eSize);

// Acabar o programa
void finishSim(nperson p, local *e, int eSize, int day, char *fPeople, char *fEnv);
#endif /*SIM_H*/