-- Ex 3

select TITULO, PRECO_TABELA
from LIVROS L
where PRECO_TABELA = (select max(PRECO_TABELA)
                      from LIVROS
                      where upper(GENERO) like '%INFORM햀ICA%')
and upper(GENERO) like '%INFORM햀ICA%';

EXEC LEIBD20_SQLCHECK('FFPITHOCONBWIOQ');

-- Ex 4

select TITULO, PRECO_TABELA
from LIVROS
where PRECO_TABELA = ALL (select max(PRECO_TABELA)
                          from LIVROS
                          where upper(GENERO) like '%INFORM햀ICA%')
and upper(GENERO) like '%INFORM햀ICA%';
                          
EXEC LEIBD20_SQLCHECK('FFWEIURDXWFRJDS');

-- Ex 5

select TITULO, PRECO_TABELA
from LIVROS L1
where NOT EXISTS (select TITULO
                  from LIVROS L2
                  where L1.PRECO_TABELA < L2.PRECO_TABELA
                  and upper(GENERO) like '%INFORM햀ICA%')
and upper(GENERO) like '%INFORM햀ICA%';

EXEC LEIBD20_SQLCHECK('FFJTNKDEIBSUKMX');

-- Ex 6

select L.TITULO, L.PRECO_TABELA
from LIVROS L, (select max(PRECO_TABELA) as PRECO_TABELA
                from LIVROS
                where upper(GENERO) like '%INFORM햀ICA%') MAX
where L.PRECO_TABELA = MAX.PRECO_TABELA;

EXEC LEIBD20_SQLCHECK('FFONRKIFWHBFLQG');

-- Ex 7

select distinct A.NOME
from AUTORES A, LIVROS L
where A.CODIGO_AUTOR = L.CODIGO_AUTOR
and L.PAGINAS > (select avg(PAGINAS)
                 from LIVROS)
order by 1;

EXEC LEIBD20_SQLCHECK('FFFMTIWGIRFSMOG');

-- Ex 8

select distinct A.NOME
from AUTORES A, LIVROS L
where A.CODIGO_AUTOR = L.CODIGO_AUTOR
group by 1
having count(L.TITULO) > (select avg(count(*)) 
                          from livros
                          group by codigo_autor);

EXEC LEIBD20_SQLCHECK('FFNZNFHHLRSMNNQ');