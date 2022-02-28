# \<Reversi\> - Manual do Utilizador

## Interface

### Menu Principal

No menu principal são encontrados três botões. Estes, tal como indica o texto neles escrito, abrem o respetivo modo de jogo.


### Durante o Jogo

Durante o jogo pode ser visto o nome e fotografias de cada jogador. 

Em volta de cada fotografia pode ser vista uma faixa. A cor dessa faixa é a cor das peças desse jogador.

Poderão também ser vistos três botões rádio e um botão tradicional. O botão tradicional, tal como o texto indica, serve para saltar de turno. Os três botões rádio são usados para escolher o tipo de peça que se vai jogar.


## Regras do Jogo

O primeiro jogador é decidido à sorte.

Cada peça deve ser colocada adjacente a uma peça do adversário de modo que a peça deste ou a linha de peças deste sejam flanqueadas pela nova peça e outra peça. Todas as peças do adversário entre essas duas ou mais peças serão "capturadas", ficando com a cor do jogador.


### Jogadas Especiais

A versão implementada do jogo possuí duas jogadas não presentes na versão tradicional. Cada jogadas destas só pode ser realizada uma vez por jogo por jogador.


#### Peça Bomba

Esta peça só pode substituir peças do jogador atual. Quando colocada, esta "explode", destruíndo todas as peças à volta num raio de uma célula (vertical, horizontal e diagonal).


#### Troca de Peças

Nesta jogada o jogador sacrifica duas peças à escolha em troca de uma peça do adversário. Deve começar por assinalar as duas peças que serão sacrificadas e por fim seleciona a peça que pretende.


### Ceder o Turno

Para além de ser obrigatório se fazer quando não há jogadas disponíveis, este pode ser usada estrategicamente para tentar derrotar o inimígo. Não há limite para o número de vezes que se pode ceder o turno.


### Condições de Fim de Jogo

O jogo acaba quando o tabuleiro está totalmente preenchido ou então quando nenhum dos jogadores poder jogar.


### Pontuação

A pontuação de cada jogador é calculada pelo número de peças que este tem no tabuleiro.


## Modos de Jogo

A aplicação oferece três modos de jogo diferentes.

### Jogo Local de Dois Jogadores

Neste modo de jogo, duas pessoas jogam no mesmo telemóvel, sendo o segundo jogador sempre anónimo e o primeiro o jogador local.


### Jogo na Rede de Dois Jogadores

Neste modo de jogo, duas pessoas jogam em telemóveis diferentes, tendo cada jogador o seu perfil.

Aqui, um telemóvel terá de fazer de servidor e o outro de cliente. Esta opção poderá ser selecionada facilmente no momento em que tentamos inicializar o jogo neste modo. O jogo será automaticamente iniciado pelo servidor assim que o outro jogador se conectar.


### Jogo na Rede de Três Jogadores

Neste modo de jogo, três pessoas jogam em telemóveis diferentes, tendo cada jogador o seu perfil.

Aqui, um telemóvel terá de fazer de servidor e os restantes de clientes. Esta opção poderá ser selecionada facilmente no momento em que tentamos inicializar o jogo neste modo. O jogo será automaticamente iniciado pelo servidor assim que os outros dois jogadores se conectarem.


## Perfil

O perfil pode ser acedido facilmente no ecrã principal usando o botão flutuante localizado no canto inferior direito.

Aqui, será facilmente vista a opção para alterar o nome e a opção para alterar o avatar. O avatar terá de ser uma foto tirada na hora, com a câmera traseira ou com a câmera dianteira.

Nesta página poderá também encontrar um botão para ver as melhores pontuações! São guardadas as cinco melhores pontuações de cada jogador.


## Autores

### Ângelo Paiva

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

a2019129023@isec.pt


### Jan Frank

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

a2017009793@isec.pt


### Pedro Henriques

Licenciatura em Engenharia Informática - Ramo de Desenvolvimento de Aplicações

2019129770@isec.pt


###### Rendered with md2pdf @ https://md2pdf.netlify.app/