/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     18/11/2020 13:10:23                          */
/*==============================================================*/


alter table HDD
   drop constraint FK_HDD_HERANCA3_PRODUTO;

alter table MONITOR
   drop constraint FK_MONITOR_HERANCA_PRODUTO;

alter table PORTATIL
   drop constraint FK_PORTATIL_HERANCA2_PRODUTO;

drop table HDD cascade constraints;

drop table MONITOR cascade constraints;

drop table PORTATIL cascade constraints;

drop table PRODUTO cascade constraints;

/*==============================================================*/
/* Table: HDD                                                   */
/*==============================================================*/
create table HDD 
(
   IDPRODUTO            NUMBER               not null,
   NOME                 NUMBER               not null,
   TIPO                 NUMBER,
   PRECO                NUMBER,
   CAPACIDADE           NUMBER,
   RPM                  NUMBER,
   CACHE                NUMBER,
   constraint PK_HDD primary key (IDPRODUTO)
);

/*==============================================================*/
/* Table: MONITOR                                               */
/*==============================================================*/
create table MONITOR 
(
   IDPRODUTO            NUMBER               not null,
   NOME                 NUMBER               not null,
   TIPO                 NUMBER,
   PRECO                NUMBER,
   RESOLUCAO            NUMBER,
   INTERFACE            NUMBER,
   TAMANHO              NUMBER,
   constraint PK_MONITOR primary key (IDPRODUTO)
);

/*==============================================================*/
/* Table: PORTATIL                                              */
/*==============================================================*/
create table PORTATIL 
(
   IDPRODUTO            NUMBER               not null,
   NOME                 NUMBER               not null,
   TIPO                 NUMBER,
   PRECO                NUMBER,
   GAMA                 NUMBER,
   PROCESSADOR          NUMBER,
   MEMORIA              NUMBER,
   constraint PK_PORTATIL primary key (IDPRODUTO)
);

/*==============================================================*/
/* Table: PRODUTO                                               */
/*==============================================================*/
create table PRODUTO 
(
   IDPRODUTO            NUMBER               not null,
   NOME                 NUMBER               not null,
   TIPO                 NUMBER,
   PRECO                NUMBER,
   constraint PK_PRODUTO primary key (IDPRODUTO)
);

alter table HDD
   add constraint FK_HDD_HERANCA3_PRODUTO foreign key (IDPRODUTO)
      references PRODUTO (IDPRODUTO);

alter table MONITOR
   add constraint FK_MONITOR_HERANCA_PRODUTO foreign key (IDPRODUTO)
      references PRODUTO (IDPRODUTO);

alter table PORTATIL
   add constraint FK_PORTATIL_HERANCA2_PRODUTO foreign key (IDPRODUTO)
      references PRODUTO (IDPRODUTO);

