-- Ex 3

select A.NOME, L.TITULO, BRUH.LIVROS_CEDITORA as "LIVROS FCA", count(L.CODIGO_LIVRO) as "Total de Livros"
from AUTORES A, LIVROS L, (select A.CODIGO_AUTOR as COD_AUTOR, count(L.CODIGO_LIVRO) as LIVROS_CEDITORA
                          from LIVROS L, AUTORES A, (select max(L.PRECO_TABELA) as PRECOS_MAX
                                                    from LIVROS L, EDITORAS E
                                                    where L.CODIGO_EDITORA = E.CODIGO_EDITORA
                                                    and upper(E.NOME) like '%FCA%') B
                          where L.CODIGO_AUTOR = A.CODIGO_AUTOR
                          and B.PRECOS_MAX = L.PRECO_TABELA
                          group by A.CODIGO_AUTOR) BRUH
where A.CODIGO_AUTOR = L.CODIGO_AUTOR
and BRUH.COD_AUTOR = A.CODIGO_AUTOR
group by A.NOME, L.TITULO, BRUH.LIVROS_CEDITORA;

EXEC LEIBD20_SQLCHECK('FIMMYCCCAVOALSB');

-- Ex 4

select A.NOME as NOME, nvl(B.GENERO_PREF, 0) as "Genero Preferido", nvl(count(L.CODIGO_LIVRO), 0) as "Total de Livros"
from AUTORES A, LIVROS L, (select A.CODIGO_AUTOR as COD_AUTOR, count(L.CODIGO_LIVRO) as GENERO_PREF
                            from AUTORES A, LIVROS L
                            where A.CODIGO_AUTOR (+) = L.CODIGO_AUTOR
                            and upper(L.GENERO) = upper(A.GENERO_PREFERIDO)
                            group by A.CODIGO_AUTOR) B
where A.CODIGO_AUTOR = L.CODIGO_AUTOR (+)
and L.CODIGO_AUTOR = B.COD_AUTOR (+)
group by A.NOME, B.GENERO_PREF
order by 1;

EXEC LEIBD20_SQLCHECK('FIJCYREDSKVRMEI');

-- Ex 5

select distinct RESULTADO as RESULTADO
from (select 'O autor ' || A.NOME || ' escreveu ' || B.LIVROS_CEDITORA || ' de ' || C.LIVROS_SEDITORA || ' livros para a editora ' || E.NOME || '.' as RESULTADO
      from LIVROS L, EDITORAS E, AUTORES A, (select L.CODIGO_AUTOR as COD_AUTOR, count(L.CODIGO_LIVRO) as LIVROS_CEDITORA
                                              from EDITORAS E, LIVROS L
                                              where L.CODIGO_EDITORA = E.CODIGO_EDITORA
                                              and  E.NOME = (select E.NOME as E_NOME
                                                              from EDITORAS E, LIVROS L
                                                              where L.CODIGO_EDITORA = E.CODIGO_EDITORA
                                                              and L.UNIDADES_VENDIDAS = (select max(UNIDADES_VENDIDAS)
                                                                                         from LIVROS))
                                              group by L.CODIGO_AUTOR) B, (select L.CODIGO_AUTOR as COD_AUTOR, nvl(count(L.CODIGO_LIVRO), 0) as LIVROS_SEDITORA
                                                                           from LIVROS L
                                                                           group by L.CODIGO_AUTOR) C
      where L.CODIGO_EDITORA = E.CODIGO_EDITORA
      and L.CODIGO_AUTOR = A.CODIGO_AUTOR
      and B.COD_AUTOR = A.CODIGO_AUTOR
      and C.COD_AUTOR = B.COD_AUTOR
      and upper(E.NOME) like '%FCA%')
order by RESULTADO;

EXEC LEIBD20_SQLCHECK('FIBLAOOEDJMVNZU');

-- Ex 6

select B.NOME_CLI as NOME
from (select C.CODIGO_CLIENTE, C.NOME as NOME_CLI, min(L.PRECO_TABELA) as MIN_PRECO
      from CLIENTES C, LIVROS L, VENDAS V
      where C.CODIGO_CLIENTE = V.CODIGO_CLIENTE
      and L.CODIGO_LIVRO = V.CODIGO_LIVRO
      group by C.CODIGO_CLIENTE, C.NOME) B
where B.MIN_PRECO = (select min(PRECO_TABELA) from LIVROS L, VENDAS V where L.CODIGO_LIVRO = V.CODIGO_LIVRO)
order by 1;

EXEC LEIBD20_SQLCHECK('FIKIDKRIMVTPRFN');