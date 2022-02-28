# Exame de Época Recurso 2020/2021

## 1.
### a)

1. São tipos que permitem fazer referência a métodos
2. Podem ser usados para passar métodos como argumentos a outros métodos 
3. Pode ser usado para encapsular um método com nome ou anónimo


### b)

1. Selecionei esta opção porque são tipos que permitem fazer referência a métodos
2. Selecionei esta opção porque podem se rusados para passar métodos como argumentos a outros métodos
3. Selecionei esta opção porque pode ser usado para encapsular um método com nome ou anónimo


## 2.

Esta afirmação é incorreta pois C#, Razor e SGBD são coisas que precisam de correr em server-side e não client-side. Um cliente não precisa de ter estas coisas instaladas no computador para aceder um site desenvolvido em ASP.Net MVC 5.0.


## 3.
### a)

Considerando, por exemplo, o `Html.DisplayNameFor` e o `Html.DisplayFor`, o primeiro mostra o nome do campo e o segundo mostra o valor desse campo.

(a resposta faz pouco sentido)


### b)

As páginas dos sites desenvolvidos com a framework ASP.Net MVC 5.0 comportam-se como views porque estas são o que o utilizador vê e são atualizadas pelos respetivos controladores.


### c)

Esta afirmação é falsa uma vez que é possível utilizarmos o framework ASP.Net MVC 5.0 para desenvolver sites sem dar uso aos HTML Helpers e ao Razor. Podemos por exemplo utilizar views em aspx e assim já não precisariamos do Razor nem dos HTML Helpers, no entanto estes dois são uma vantagem enorme e é aconselhado ser usado.


## 4.
### a)

O LINQ serve de camada de abstração para fazer queries SQL. Como em C# tudo é um objeto, o LINQ devolve o resultado da query em forma de objeto. 

O que podemos ver nesta figura é que a aplicação faz um pedido através do LINQ, que por sua vez vai fazer um pedido ao servidor SQL. Este devolve o resultado do pedido em linhas e o LINQ transforma estas linhas em objeto(s), de modo a puderem ser utilizados no C#.


### b)

Excluíndo algumas operações que são impossíveis fazer com Extension Methods, usar Query Syntax ou o referido anteriormente é indiferente.


## 5.
### a)

Esta instrução irá criar uma nova lista com todos os alunos da lista `alunos` onde o último nome comece por T.

- .Where(...): faz recurso a LINQ para filtrar a lista
- .Split(' '): separa a string nome por espaços
- .Last(): do novo array de strings, vai selecionar a última (ou seja, o último nome)
- .StartsWith("T"): verifica se a string começa com a string passada por argumento, neste caso "T"


### b)

A variável alunosF é do tipo var. Este tipo pode ser usado sempre que no momento de declaração da variável lhe seja associado um valor.

O uso de var traz várias vantagens. Deste modo, sempre que o tipo de valor associado for alterado no momento de declaração é desnecessário voltar a alterar o tipo. Para além disso, em tipos que são complicados de definir, deixamos esse trabalho para o compilador porque, apesar de o tipo não ser declarado por nós, aquela variável manterá o mesmo tipo desde que é declarada, sendo o tipo `var` apenas uma maneira de facilitar o trabalho do programador ao definir tipos.


### c)

Nesta instrução o => é usado para definir uma expressão lambda, sendo chamado de operador lambda. Este é usado para separar o input (que fica no lado esquerdo) do corpo da função (que fica do lado direito), eliminando também a necessidade do uso da keyword `return`.

Pode ser usado para definir qualquer expressão lamba quando usado como operador lambda. Para além disso, pode também ser usado para definir o corpo de um método, respeitando as mesmas regras.