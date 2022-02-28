# Exame de Época Especial 2020/2021

## 1.

Os delegates em C# são parecidos com pointeiros para métodos/funções em C++. A grande vantagem dos delegates é que podem ser passados para outros métodos e, para além disso, podem também ter argumentos associados, evitando assim ter que invocar o mesmo método com os mesmos argumentos vezes e vezes sem conta, podendo apenas usar um delegate para isso.


## 2.

¯\\_(ツ)\_/¯


## 3.

Nem sempre é necessário efetuar validação no client-side e no server-side mas é boa prática fazê-lo pois, um utilizador com experiência, pode sempre ultrapassar a verificação feita no client-side, sendo por isso importante também se validar a informação no server-side para evitar erros.


## 4.

Os models é onde os dados estão guardados. Estes dados são alterados pelos controllers que, por sua vez, também são responsáveis por enviar estes dados às views para poderem ser mostrados ao utilizador. O utilizador vê os dados com recurso às views, mas interage com os controllers.


## 5.
### a)

Language-Integrated Query, ou seja, pedidos a bases de dados integrados na linguagem (de programação). A sua utilização é especialmente útil pois esta serve de camada de abstração para pedidos a bases de dados, oferecendo também extension methods que tornam a sintaxe das queries muito mais agradável.


### b)

Excluíndo algumas operações que são impossíveis fazer com Extension Methods, usar Query Syntax ou o referido anteriormente é indiferente.


## 6.
### a)

O => é usado para separar o input (que fica no lado esquerdo) do corpo da função (que fica do lado direito), eliminando também a necessidade do uso da keyword `return`.

Pode ser usado para definir qualquer expressão lamba quando usado como operador lambda. Para além disso, pode também ser usado para definir o corpo de um método, respeitando as mesmas regras.


### b)

Nesta instrução o => é usado para definir uma expressão lambda, sendo chamado de operador lambda.

Esta instrução irá criar uma nova lista com todos os alunos da lista `alunos` onde o último nome comece por T.

- .Where(...): faz recurso a LINQ para filtrar a lista
- .Split(' '): separa a string nome por espaços
- .Last(): do novo array de strings, vai selecionar a última (ou seja, o último nome)
- .StartsWith("T"): verifica se a string começa com a string passada por argumento, neste caso "T"


## 7.

Code first e database first.


## 8.

O LINQ serve de camada de abstração para fazer queries SQL. Como em C# tudo é um objeto, o LINQ devolve o resultado da query em forma de objeto, o que torna esta arquitetura necessária.

O que podemos ver nesta figura é que a aplicação faz um pedido através do LINQ, que por sua vez vai fazer um pedido ao servidor SQL. Este devolve o resultado do pedido em linhas e o LINQ transforma estas linhas em objeto(s), de modo a puderem ser utilizados no C#.