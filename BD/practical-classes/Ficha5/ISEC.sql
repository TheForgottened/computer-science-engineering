-- Ex 3

select count(TITULO) as "Total livros de Informática"
from LIVROS
where upper(GENERO) like '%INFORMÁTICA%';

EXEC LEIBD20_SQLCHECK('FEAHQXIAJQTWFEG');

-- Ex 4

select count(TITULO) as "Total de Livros", avg(PRECO_TABELA) as "Preço Médio", sum(UNIDADES_VENDIDAS) as "Total de livros vendidos"
from LIVROS
where CODIGO_EDITORA is not NULL;

EXEC LEIBD20_SQLCHECK('FETVZGABLESUGNU');

-- Ex 5

select GENERO, count(TITULO) as QUANTIDADE
from LIVROS
group by GENERO
order by GENERO;

EXEC LEIBD20_SQLCHECK('FEHHHERCGBTHHIN');

-- Ex 6

select TITULO, min(V.PRECO_UNITARIO) as P_MAIS_BAIXO, max(V.PRECO_UNITARIO) as P_MAIS_ALTO, round(avg(V.PRECO_UNITARIO), 2) as P_MEDIO
from LIVROS L, VENDAS V
where L.CODIGO_LIVRO = V.CODIGO_LIVRO
and upper(GENERO) like '%INFORMÁTICA%'
group by TITULO
order by TITULO;

EXEC LEIBD20_SQLCHECK('FEGFYBLDUJATION');

-- Ex 7

select GENERO, max(PRECO_TABELA) - min(PRECO_TABELA) as DIFERENCA
from LIVROS
group by GENERO
order by GENERO;

EXEC LEIBD20_SQLCHECK('FECASYJEHNOCJLA');


-- Ex 8

select L.TITULO, L.PRECO_TABELA as PRECO, sum(V.TOTAL_VENDA) as NUM_VENDIDOS, (L.PRECO_TABELA * sum(L.UNIDADES_VENDIDAS)) as REC_ESPERADA, sum(PRECO_UNITARIO) as REC_EFETIVA
from LIVROS L, VENDAS V
where L.CODIGO_LIVRO = V.CODIGO_LIVRO
group by L.TITULO, V.TOTAL_VENDA, L.PRECO_TABELA
order by L.TITULO;

EXEC LEIBD20_SQLCHECK('FEPZSNIFJMVFKFO');

-- Ex 9

select GENERO, ceil(avg(PRECO_TABELA)) as "Preço Médio"
from LIVROS
group by GENERO
having count(TITULO) > 4
order by 2;

EXEC LEIBD20_SQLCHECK('FETBBIEGOHCHLSK');

-- Ex 10

select A.NOME, min(L.PRECO_TABELA) as "Preco Minimo"
from AUTORES A, LIVROS L
where L.CODIGO_AUTOR = A.CODIGO_AUTOR
group by A.NOME having min(L.PRECO_TABELA) >= 30
order by 2;

EXEC LEIBD20_SQLCHECK('FEJGQHLHMZOLMUK');

-- Ex 11

select C.CODIGO_CLIENTE, C.NOME, sum(V.QUANTIDADE) as "N.Livros", round(avg(V.PRECO_UNITARIO), 2) as "Preco Medio", count(distinct L.CODIGO_AUTOR) as "N. Autores Diferentes"
from CLIENTES C, LIVROS L, VENDAS V
where C.CODIGO_CLIENTE = V.CODIGO_CLIENTE
and L.CODIGO_LIVRO = V.CODIGO_LIVRO
and upper(C.MORADA) like '%LISBOA%'
group by C.CODIGO_CLIENTE, C.NOME
having count(distinct L.CODIGO_EDITORA) = 1
and sum(V.QUANTIDADE) > 3
order by 2;

EXEC LEIBD20_SQLCHECK('FEUVKRRIJJGINIO');

-- Ex 12

select A.NOME, replace(count(CODIGO_LIVRO), 0, 'Nenhum') as "Num .Livros"
from AUTORES A, LIVROS L
where A.CODIGO_AUTOR = L.CODIGO_AUTOR(+)
group by A.NOME
having count(distinct L.TITULO) <= 3
order by 1;

EXEC LEIBD20_SQLCHECK('FENMESWJTYLQODP');

select distinct CODIGO_AUTOR
from AUTORES
order by CODIGO_AUTOR;

select distinct CODIGO_AUTOR
from LIVROS
order by CODIGO_AUTOR;