/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     18/11/2020 12:53:59                          */
/*==============================================================*/


alter table APLICA
   drop constraint FK_APLICA_APLICA_VEICULO;

alter table APLICA
   drop constraint FK_APLICA_APLICA2_MECANICO;

alter table APLICA
   drop constraint FK_APLICA_APLICA3_PECA;

drop index APLICA3_FK;

drop index APLICA2_FK;

drop index APLICA_FK;

drop table APLICA cascade constraints;

drop table MECANICO cascade constraints;

drop table PECA cascade constraints;

drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: APLICA                                                */
/*==============================================================*/
create table APLICA 
(
   IDVEICULO            NUMBER               not null,
   IDMECANICO           NUMBER               not null,
   IDPECA               NUMBER               not null,
   constraint PK_APLICA primary key (IDVEICULO, IDMECANICO, IDPECA)
);

/*==============================================================*/
/* Index: APLICA_FK                                             */
/*==============================================================*/
create index APLICA_FK on APLICA (
   IDVEICULO ASC
);

/*==============================================================*/
/* Index: APLICA2_FK                                            */
/*==============================================================*/
create index APLICA2_FK on APLICA (
   IDMECANICO ASC
);

/*==============================================================*/
/* Index: APLICA3_FK                                            */
/*==============================================================*/
create index APLICA3_FK on APLICA (
   IDPECA ASC
);

/*==============================================================*/
/* Table: MECANICO                                              */
/*==============================================================*/
create table MECANICO 
(
   IDMECANICO           NUMBER               not null,
   DESCRICAO            NUMBER,
   IDADE                NUMBER,
   constraint PK_MECANICO primary key (IDMECANICO)
);

/*==============================================================*/
/* Table: PECA                                                  */
/*==============================================================*/
create table PECA 
(
   IDPECA               NUMBER               not null,
   DESCRICAO            NUMBER,
   FABRICANTE           NUMBER,
   constraint PK_PECA primary key (IDPECA)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   IDVEICULO            NUMBER               not null,
   MARCA                NUMBER,
   MODELO               NUMBER,
   constraint PK_VEICULO primary key (IDVEICULO)
);

alter table APLICA
   add constraint FK_APLICA_APLICA_VEICULO foreign key (IDVEICULO)
      references VEICULO (IDVEICULO);

alter table APLICA
   add constraint FK_APLICA_APLICA2_MECANICO foreign key (IDMECANICO)
      references MECANICO (IDMECANICO);

alter table APLICA
   add constraint FK_APLICA_APLICA3_PECA foreign key (IDPECA)
      references PECA (IDPECA);

