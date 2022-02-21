/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     18/11/2020 12:09:40                          */
/*==============================================================*/


alter table ENCOMENDA
   drop constraint FK_ENCOMEND_TEM_PADARIA;

alter table ENCOMENDA
   drop constraint FK_ENCOMEND_TRANSPORT_VEICULO;

alter table ENC_PAO
   drop constraint FK_ENC_PAO_REFERE_PAO;

alter table ENC_PAO
   drop constraint FK_ENC_PAO_TEM_1_ENCOMEND;

drop index TEM_FK;

drop index TRANSPORTA_FK;

drop table ENCOMENDA cascade constraints;

drop index TEM_1_FK;

drop index REFERE_FK;

drop table ENC_PAO cascade constraints;

drop table PADARIA cascade constraints;

drop table PAO cascade constraints;

drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: ENCOMENDA                                             */
/*==============================================================*/
create table ENCOMENDA 
(
   ID_ENCOMENDA         NUMBER(6)            not null,
   ID_PADARIA           NUMBER(4),
   ID_VEICULO           NUMBER(4),
   DATA                 DATE                 not null,
   LOCAL_ENTREGA        VARCHAR2(40),
   constraint PK_ENCOMENDA primary key (ID_ENCOMENDA)
);

/*==============================================================*/
/* Index: TRANSPORTA_FK                                         */
/*==============================================================*/
create index TRANSPORTA_FK on ENCOMENDA (
   ID_VEICULO ASC
);

/*==============================================================*/
/* Index: TEM_FK                                                */
/*==============================================================*/
create index TEM_FK on ENCOMENDA (
   ID_PADARIA ASC
);

/*==============================================================*/
/* Table: ENC_PAO                                               */
/*==============================================================*/
create table ENC_PAO 
(
   ID_PAO               NUMBER(4)            not null,
   ID_ENCOMENDA         NUMBER(6)            not null,
   QUANTIDADE           NUMBER(3),
   constraint PK_ENC_PAO primary key (ID_PAO, ID_ENCOMENDA)
);

/*==============================================================*/
/* Index: REFERE_FK                                             */
/*==============================================================*/
create index REFERE_FK on ENC_PAO (
   ID_PAO ASC
);

/*==============================================================*/
/* Index: TEM_1_FK                                              */
/*==============================================================*/
create index TEM_1_FK on ENC_PAO (
   ID_ENCOMENDA ASC
);

/*==============================================================*/
/* Table: PADARIA                                               */
/*==============================================================*/
create table PADARIA 
(
   ID_PADARIA           NUMBER(4)            not null,
   NOME                 VARCHAR2(30)         not null,
   CIDADE               VARCHAR2(20),
   TELEFONE             VARCHAR2(12),
   CONTRIBUINTE         VARCHAR2(9),
   constraint PK_PADARIA primary key (ID_PADARIA)
);

/*==============================================================*/
/* Table: PAO                                                   */
/*==============================================================*/
create table PAO 
(
   ID_PAO               NUMBER(4)            not null,
   DESIGNACAO           VARCHAR2(30)         not null,
   TIPO                 VARCHAR2(30),
   PESO                 NUMBER(5,2),
   constraint PK_PAO primary key (ID_PAO)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   ID_VEICULO           NUMBER(4)            not null,
   MATRICULA            VARCHAR2(8),
   MARCA                VARCHAR2(30),
   MODELO               VARCHAR2(30),
   TARA                 NUMBER(4),
   constraint PK_VEICULO primary key (ID_VEICULO)
);

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TEM_PADARIA foreign key (ID_PADARIA)
      references PADARIA (ID_PADARIA);

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TRANSPORT_VEICULO foreign key (ID_VEICULO)
      references VEICULO (ID_VEICULO);

alter table ENC_PAO
   add constraint FK_ENC_PAO_REFERE_PAO foreign key (ID_PAO)
      references PAO (ID_PAO);

alter table ENC_PAO
   add constraint FK_ENC_PAO_TEM_1_ENCOMEND foreign key (ID_ENCOMENDA)
      references ENCOMENDA (ID_ENCOMENDA);

