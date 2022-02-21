-- Ex 3

create table AUTORES2
  as (select *
     from AUTORES);
   
update AUTORES2
  set NOME = upper(NOME);
   
alter table AUTORES2
  add constraint autores2_nome_cap
  check (NOME = upper(NOME));

alter table AUTORES2  
  add constraint autores2_idade_max
  check (IDADE < 200);
  
drop table AUTORES2;

EXEC LEIBD20_SQLCHECK('FJMDPQLCLSHJMHS');

-- Ex 4

create table AVALIACOES
  (CODIGO_LIVRO NUMBER(4),
  CODIGO_CLIENTE NUMBER(4),
  NOTA NUMBER(1),
  constraint codlivro_avaliacoes_fk FOREIGN KEY (CODIGO_LIVRO) references LIVROS (CODIGO_LIVRO),
  constraint codclient_avaliacoes_fk FOREIGN KEY (CODIGO_CLIENTE) references AUTORES (CODIGO_AUTOR),
  constraint avaliacoes_pk PRIMARY KEY (CODIGO_LIVRO, CODIGO_CLIENTE),
  constraint nota_avaliacoes_range check (NOTA between 1 and 5));
  
drop table AVALIACOES;

EXEC LEIBD20_SQLCHECK('FJJDDYQDKHXKNPW');

-- Ex 5

create table LIVROS_BACKUP
  as (select *
     from LIVROS);
   
update LIVROS_BACKUP
  set GENERO = upper(GENERO);
  
drop table LIVROS_BACKUP;

EXEC LEIBD20_SQLCHECK('FJLPTCFELJHXOCF');

-- Ex 6

create table LIVROS_BACKUP
  as (select *
     from LIVROS);

alter table LIVROS_BACKUP
  add ( 
  constraint FK_LIVBAK_ID_EDITOR_EDITORAS FOREIGN KEY (CODIGO_EDITORA) references EDITORAS (CODIGO_EDITORA),
  constraint FK_LIVBAK_ID_LIVRO_AUTORES FOREIGN KEY (CODIGO_AUTOR) references AUTORES (CODIGO_AUTOR),
  constraint PK_IDBAK_LIVRO PRIMARY KEY (CODIGO_LIVRO));
  
drop table LIVROS_BACKUP;
  
EXEC LEIBD20_SQLCHECK('FJQIMRZFKSZAPZG');

-- Ex 7

create table LIVROS_BACKUP
  as (select *
     from LIVROS);
     
insert into LIVROS_BACKUP
  (CODIGO_LIVRO, CODIGO_EDITORA, CODIGO_AUTOR, TITULO, ISBN, GENERO, PRECO_TABELA, PAGINAS, QUANT_EM_STOCK, UNIDADES_VENDIDAS, DATA_EDICAO)
  values
  (51, (select CODIGO_EDITORA from EDITORAS where upper(NOME) like '%FCA%'), (select CODIGO_AUTOR from AUTORES where upper(NOME) like '%SÉRGIO SOUSA%'), 'Informática para Todos', 132434, 'Informática', 24, 430, 0, 0, SYSDATE);
     
drop table LIVROS_BACKUP;
  
EXEC LEIBD20_SQLCHECK('FJAZZUPGMKTGQHJ');

-- Ex 8

create table AUTORES2
  as (select *
     from AUTORES);
     
insert into AUTORES2
  (CODIGO_AUTOR, NOME, N_CONTRIBUINTE)
  values
  (45, (upper('José de Magalhães')), 77665544);
     
drop table AUTORES2;

EXEC LEIBD20_SQLCHECK('FJEBORRHGVHSRMG');

-- Ex 9

delete from LIVROS_BACKUP
where CODIGO_LIVRO = 51;

EXEC LEIBD20_SQLCHECK('FJJYZQUIUYKHSMD');

-- Ex 10

update LIVROS_BACKUP
set PRECO_TABELA = PRECO_TABELA * 1.10
where upper(GENERO) = 'AVENTURA';

EXEC LEIBD20_SQLCHECK('FJUQRAKJCKDBTUE');

-- Ex 11

delete from LIVROS_BACKUP
where PRECO_TABELA < (select avg(PRECO_TABELA) from LIVROS_BACKUP where upper(GENERO) = 'INFORMÁTICA');

EXEC LEIBD20_SQLCHECK('FJODHUEKOKUGUOR');

-- Ex 12

create table AUTORES_BACKUP
as (select * from AUTORES);

drop table AUTORES_BACKUP;

EXEC LEIBD20_SQLCHECK('FJYIBUHLBFQNVHL');

-- Ex 13

alter table AUTORES_BACKUP
add NLIVROS NUMBER(3) default 0;

alter table AUTORES_BACKUP
add constraint AUTBAK_NL_RANGE check (NLIVROS between 0 and 150);
  
alter table AUTORES_BACKUP
modify (NLIVROS constraint AUTBAK_NL_NULL not null);

EXEC LEIBD20_SQLCHECK('FJFPEMBMQNPMWNX');

-- Ex 14

update AUTORES_BACKUP A
set NLIVROS = nvl((select count(CODIGO_LIVRO) from LIVROS L where A.CODIGO_AUTOR = L.CODIGO_AUTOR group by L.CODIGO_AUTOR), 0);

EXEC LEIBD20_SQLCHECK('FJOWYEZNGGWC@MD');

-- Ex 15

update AUTORES_BACKUP A
set GENERO_PREFERIDO = (select L.GENERO
                        from LIVROS L
                        where L.CODIGO_AUTOR = A.CODIGO_AUTOR
                        and ROWNUM = 1
                        group by L.CODIGO_AUTOR, L.GENERO
                        having count(L.CODIGO_LIVRO) = (select max(count(LL.CODIGO_LIVRO))
                                                        from LIVROS LL
                                                        where L.CODIGO_AUTOR = LL.CODIGO_AUTOR
                                                        group by LL.GENERO, LL.CODIGO_AUTOR));

EXEC LEIBD20_SQLCHECK('FJAWCEFOMPEJAYH');

-- Ex 16

create table EDITORAS_BACKUP
as (select * from EDITORAS);

drop table EDITORAS_BACKUP;

EXEC LEIBD20_SQLCHECK('FJVETAYPTOBEBEN');

-- Ex 17

drop table EDITORAS_BACKUP;

EXEC LEIBD20_SQLCHECK('FJVTSCKQNLKOCAL');

-- Ex 18

alter table EDITORAS_BACKUP
add EDICAO NUMBER(7);

EXEC LEIBD20_SQLCHECK('FJWIMDERNLBBDRW');

-- Ex 19

update LIVROS_BACKUP LB
set UNIDADES_VENDIDAS = (select count(V.CODIGO_LIVRO) from LIVROS L, VENDAS V where LB.CODIGO_LIVRO = L.CODIGO_LIVRO and L.CODIGO_LIVRO = V.CODIGO_LIVRO);

EXEC LEIBD20_SQLCHECK('FJQBWYUSGEIJEXA');

-- Ex 20

create table VENDAS_BACKUP
as (select * from VENDAS);

drop table VENDAS_BACKUP;

update VENDAS_BACKUP VB
set TOTAL_VENDA = (select V.QUANTIDADE * V.PRECO_UNITARIO from VENDAS V where V.CODIGO_VENDA = VB.CODIGO_VENDA);

EXEC LEIBD20_SQLCHECK('FJGFEARTDKIPFGW');

-- Ex 21

create table VENDAS_BACKUP
as (select * from VENDAS);

drop table VENDAS_BACKUP;

EXEC LEIBD20_SQLCHECK('FJQSTLIUCSCZGRC');

-- Ex 22

delete from VENDAS_BACKUP
where to_char(DATA_VENDA, 'dd-mm-yyyy') like '%-01-%';

EXEC LEIBD20_SQLCHECK('FJCFDURVMDMZHMY');

-- Ex 23

delete from VENDAS_BACKUP
where CODIGO_LIVRO = (select LIVRO_CARO
                      from (select L.CODIGO_LIVRO as LIVRO_CARO, max(L.PRECO_TABELA)
                            from LIVROS L
                            where L.CODIGO_AUTOR = (select CODIGO_AUTOR from AUTORES where upper(NOME) = 'SÉRGIO SOUSA')
                            group by L.CODIGO_LIVRO
                            order by 2 DESC)
                      where ROWNUM = 1);
                                              
EXEC LEIBD20_SQLCHECK('FJRAFVLWKEVHIEK');

-- TODOS OS DROP TABLES NECESSARIOS

drop table AUTORES_BACKUP;
drop table AUTORES2;
drop table EDITORAS_BACKUP;
drop table LIVROS_BACKUP;
drop table VENDAS_BACKUP;
drop table AVALIACOES;