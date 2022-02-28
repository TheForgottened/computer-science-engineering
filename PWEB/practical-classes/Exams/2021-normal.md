# Exame de Época Normal 2020/2021

## 1.
### a)

Apagar registos pode ter consequências. Imaginando, por exemplo, um site de comércio eletrónico, se apagar o registo de um produto que se deixou de vender, irão também ser apagadas todas as encomendas feitas que tenham esse produto associado mas o facto de se deixar de vender esse produto não implica que as encomendas não tenham sido feitas e não devam existir. Assim, como alternativa pode-se, por exemplo, ter uma coluna extra no produto chamada "STATE" ou poderia estar, por exemplo, com os valores "SELLING", "DISCONTINUED", de modo a decidir se deve aparecer em pesquisas ou não e se o utilizador pode encomendar mais ou não.


### b)

A desnormalização de algumas tabelas da base de dados é feita pois desnormalização é uma técnica de otimização onde se adiciona dados redundantes em uma ou mais tabelas, tirando parte do custo de fazer joins em queries. Isto é feito porque aumenta a velocidade de busca de informação.


### c)

A razão desse procedimento é para facilitar aos clientes, uma vez que por exemplo, se a loja com o id 1 tiver o nome Zara, é muito mais conveninete o cliente saber que essa loja é a Zara do que saber o seu Id.


## 2.
### a)

Open Authorization é um tipo de autenticação baseada em tokens. Esta é bastante utilizada por permitir às organizações partilhar informação em serviços de terceiros sem exporem as credenciais de login dos seus utilizadores.


### b)

Esta é especialmente útil quando se quer garantir a um website acesso a informação disponível noutro website sem ter que passar as credenciais de login como meio de autenticação.


## 3.
### a)

Autenticação é o processo de verificar quem o utilizador é, mas autorização é o processo de verificar que partes especificas do website o utilizador tem acesso, como páginas, ficheiros, etc. Para ocorrer autorização tem que ocorrer antes disso autenticação e autorização ocorre sempre após a autenticação, portanto, no caso de se querer usar uma destas, é necessário usar-se sempre a outra.


### b)

A validação dos dados introduzidos pelos utilizadores pode ser feito em sites que não usufruam de sistema de autenticação/autorização. Pode, por exemplo, validar-se se a informação introduzida pelo utilizador, num campo de texto aberto, é apenas composta por números, não sendo para isto necessário qualquer tipo de autenticação ou autorização.


### c)

Em sites desenvolvidos em ASP.Net MVC 5.0 o JavaScript é utilizado no Client-Side, ou seja, sempre que queremos gerar alguma coisa que seja visual para o utilizador. Neste caso, se quiséssemos utilizar C# no lugar de JavaScript, teriamos de utilizar Blazor que faz com que seja possível executar código C# em Client-Side.


## 4.
### a)

Code first e database first.

### b)

Code first: neste workflow, as classes são criadas com foco no domínio da aplicação, sem desenho explícito da database. Depois, quando o código é executado, o Entity Framework encarrega-se de criar a base de dados.

Database first: neste workflow, começa-se por criar a base de dados e, depois disso, usa-se o Entity Framework para gerar as classes a partir das tabelas.

Os vários workflows existem porque, por exemplo, o database first é muito mais útil que o code first quando já temos uma base de dados enquanto que o code first é muito melhor quando estamos a criar uma aplicação relativamente pequena ou então quando ainda não temos base de dados.


### c)

Pessoalmente usaria o code first por ser muito mais simples fazer alterações na base de dados e puder fazer tudo por código sem precisar de ferramentas externas.


## 5.
### a)

1. Poderão não serem necessários, mas são quase sempre usados!
2. A sua utilização implica a escrita de instruções mais compactas mas também mais complexas. 
3. Por causa da sua utilização é gerado código HTML!


### b)

1. O uso de HTML Helpers e de Razor num website ASP.Net MVC 5.0 não é obrigatório, sendo uma ferramenta opcional cujo objetivo é ajudar o programador
2. As instruções, apesar de serem por vezes mais compactas do que puro código HTML (por exemplo, inserir várias imagens, onde ao invés de se escrever uma a uma apenas se faz um loop), estas são maioria das vezes mais complexas
3. Estas permitem ao server criar e renderizar elementos HTML