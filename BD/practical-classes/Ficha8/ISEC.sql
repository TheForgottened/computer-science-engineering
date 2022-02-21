-- Ex 3

select distinct L.TITULO, L.PRECO_TABELA as PRECO, round(A.PRECO_MEDIO, 1) as PRECO_MEDIO, round(L.PRECO_TABELA - A.PRECO_MEDIO, 1) as DIFERENÇA
from LIVROS L, (select CODIGO_AUTOR, avg(PRECO_TABELA) as PRECO_MEDIO
                from LIVROS
                where upper(GENERO) = 'INFORMÁTICA'
                group by CODIGO_AUTOR) A
where A.CODIGO_AUTOR = L.CODIGO_AUTOR 
and upper(GENERO) = 'INFORMÁTICA'
order by 1;

EXEC LEIBD20_SQLCHECK('FHMAWEECEBWOKOY');

-- Ex 4

select GENERO, TITULO, UNIDADES_VENDIDAS
from LIVROS
where (GENERO, UNIDADES_VENDIDAS) in (select GENERO, max(UNIDADES_VENDIDAS)
                                      from LIVROS
                                      group by GENERO)
order by 1;
                                 
EXEC LEIBD20_SQLCHECK('FHRXDZFDXEJULJQ');

-- Ex 5

select L.TITULO, to_char(round(100 * L.UNIDADES_VENDIDAS / B.SOMA, 1), '990.99') as PERCENT
from LIVROS L, EDITORAS E, (select sum(L.UNIDADES_VENDIDAS) as SOMA
                            from LIVROS L, EDITORAS E
                            where L.CODIGO_EDITORA = E.CODIGO_EDITORA
                            and upper(E.NOME) like '%FCA%') B
where L.CODIGO_EDITORA = E.CODIGO_EDITORA
and upper(E.NOME) like '%FCA%'
order by 2 DESC, 1;

EXEC LEIBD20_SQLCHECK('FHNDXPBEEVCAMXH');

-- Ex 6

select L.TITULO
from LIVROS L, CLIENTES C, VENDAS V
where L.CODIGO_LIVRO = V.CODIGO_LIVRO
and V.CODIGO_CLIENTE = C.CODIGO_CLIENTE
and upper(C.MORADA) like '%LISBOA%'
and V.QUANTIDADE = (select max(QUANTIDADE)
                    from VENDAS);

EXEC LEIBD20_SQLCHECK('FHUMAALFQLEXNMJ');

-- Ex 7

select count(distinct L.TITULO) as "Total de Livros", count(distinct A.CODIGO_AUTOR) as "Total de Autores", count(distinct E.CODIGO_EDITORA) as "Total de Editoras"
from LIVROS L, AUTORES A, EDITORAS E;

EXEC LEIBD20_SQLCHECK('FHYBTGRGYHEQORP');

-- Ex 8

select 'O autor ' || B.AUTORES || ' escreveu ' || B.SEDITORA || ' e ' || C.CEDITORA || ' sob a alçada da editora FCA – EDITORA' as "Resultado"
from AUTORES A, (select A.NOME as AUTORES, count(L.CODIGO_LIVRO) as SEDITORA
                           from AUTORES A, LIVROS L
                           where A.CODIGO_AUTOR = L.CODIGO_AUTOR
                           group by A.NOME) B, (select A.NOME as AUTORES, count(L.CODIGO_LIVRO) as CEDITORA
                                                from AUTORES A, LIVROS L, EDITORAS E
                                                where A.CODIGO_AUTOR = L.CODIGO_AUTOR
                                                and L.CODIGO_EDITORA = E.CODIGO_EDITORA
                                                and upper(E.NOME) like '%FCA%'
                                                group by A.NOME) C
where A.NOME = B.AUTORES
and A.NOME = C.AUTORES
order by 1;
                
EXEC LEIBD20_SQLCHECK('FHOOKBTHLZKTPZP');