-- Ex 3

select TITULO
from LIVROS
where extract(YEAR from DATA_EDICAO) = 2011;

EXEC LEIBD20_SQLCHECK('FDUIIRTAKWEXEMS');

-- Ex 4

select to_char(sysdate, 'HH:MM:DD AM') as "Hora Actual", to_char(sysdate, 'DD, Month, YYYY') as "Data Actual"
from DUAL;

EXEC LEIBD20_SQLCHECK('FDWNLITBBLANFKS');

-- Ex 5

select L.TITULO as "Titulo", L.PRECO_TABELA as "Preço"
from DUAL D, LIVROS L
where (SYSDATE - L.DATA_EDICAO) < 3000;

EXEC LEIBD20_SQLCHECK('FDCAWZTCVLWYGAP');

-- Ex 6

select distinct L.TITULO
from VENDAS V, LIVROS L, CLIENTES C
where V.CODIGO_LIVRO = L.CODIGO_LIVRO
and C.CODIGO_CLIENTE = V.CODIGO_CLIENTE
and extract(MONTH from V.DATA_VENDA) = 2
and extract(YEAR from V.DATA_VENDA) = 2014
and upper(C.MORADA) like '%LISBOA%' 
order by L.TITULO;

EXEC LEIBD20_SQLCHECK('FDVHQSPDEEBFHIW');

-- Ex 7

select L.TITULO
from LIVROS L, VENDAS V
where L.CODIGO_LIVRO = V.CODIGO_LIVRO
and upper(GENERO) = 'INFORMÁTICA'
and to_char(V.DATA_VENDA, 'mm') = to_char(L.DATA_EDICAO, 'mm')
and to_char(V.DATA_VENDA, 'yyyy') = to_char(L.DATA_EDICAO, 'yyyy');

EXEC LEIBD20_SQLCHECK('FDRRBNZEHNWOISK');

-- Ex 8

select distinct A.NOME
from LIVROS L, AUTORES A
where L.CODIGO_AUTOR = A.CODIGO_AUTOR
and L.DATA_EDICAO between to_date('21-06-2013', 'dd-mm-yy') and to_date('22-09-2013', 'dd-mm-yyyy')
and L.PAGINAS > 190;

EXEC LEIBD20_SQLCHECK('FDYHZMQFGKWTJJM');

-- Ex 9

select TITULO as "Titulo", to_char(DATA_EDICAO, 'YYYY-MM-DD HH24:MI:SS') as "DATA_EDICAO", (to_char(sysdate, 'yyyy') - to_char(DATA_EDICAO, 'yyyy')) as "Num. de anos"
from LIVROS
where upper(GENERO) = 'INFORMÁTICA';

EXEC LEIBD20_SQLCHECK('FDLODEOGLOGAKUD');

-- Ex 10

select C.NOME
from CLIENTES C, VENDAS V, LIVROS L
where V.CODIGO_LIVRO = L.CODIGO_LIVRO
and V.CODIGO_CLIENTE = C.CODIGO_CLIENTE
and upper(L.GENERO) = 'INFORMÁTICA'
and to_char(V.DATA_VENDA, 'd') = 3
and to_char(V.DATA_VENDA, 'dd') <= 7
and to_char(V.DATA_VENDA, 'hh24') < 17;

EXEC LEIBD20_SQLCHECK('FDCYRYZHRTDHLVG');

-- Ex 11

select distinct A.NOME
from AUTORES A
minus select NOME
from AUTORES A, LIVROS L
where A.CODIGO_AUTOR = L.CODIGO_AUTOR;

EXEC LEIBD20_SQLCHECK('FDLBDDBIQETTMFB');

-- Ex 12

select distinct A.NOME
from AUTORES A
minus select NOME
from AUTORES A, LIVROS L
where A.CODIGO_AUTOR = L.CODIGO_AUTOR
or to_char(L.DATA_EDICAO, 'yyyy') = to_char(SYSDATE, 'yyyy');

EXEC LEIBD20_SQLCHECK('FDMBAEUJCCTONSE');