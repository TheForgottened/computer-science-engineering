/*==============================================================*/
/* DBMS name:      SAP SQL Anywhere 17                          */
/* Created on:     18/11/2020 12:14:53                          */
/*==============================================================*/


if exists(select 1 from sys.sysforeignkey where role='FK_ENCOMEND_TEM_PADARIA') then
    alter table ENCOMENDA
       delete foreign key FK_ENCOMEND_TEM_PADARIA
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_ENCOMEND_TRANSPORT_VEICULO') then
    alter table ENCOMENDA
       delete foreign key FK_ENCOMEND_TRANSPORT_VEICULO
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_ENC_PAO_REFERE_PAO') then
    alter table ENC_PAO
       delete foreign key FK_ENC_PAO_REFERE_PAO
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_ENC_PAO_TEM_1_ENCOMEND') then
    alter table ENC_PAO
       delete foreign key FK_ENC_PAO_TEM_1_ENCOMEND
end if;

drop index if exists ENCOMENDA.TEM_FK;

drop index if exists ENCOMENDA.TRANSPORTA_FK;

drop index if exists ENCOMENDA.ENCOMENDA_PK;

drop table if exists ENCOMENDA;

drop index if exists ENC_PAO.TEM_1_FK;

drop index if exists ENC_PAO.REFERE_FK;

drop index if exists ENC_PAO.ENC_PAO_PK;

drop table if exists ENC_PAO;

drop index if exists PADARIA.PADARIA_PK;

drop table if exists PADARIA;

drop index if exists PAO.PAO_PK;

drop table if exists PAO;

drop index if exists VEICULO.VEICULO_PK;

drop table if exists VEICULO;

/*==============================================================*/
/* Table: ENCOMENDA                                             */
/*==============================================================*/
create or replace table ENCOMENDA 
(
   ID_ENCOMENDA         numeric(6)                     not null,
   ID_PADARIA           numeric(4)                     null,
   ID_VEICULO           numeric(4)                     null,
   DATA                 date                           not null,
   LOCAL_ENTREGA        varchar(40)                    null,
   constraint PK_ENCOMENDA primary key clustered (ID_ENCOMENDA)
);

/*==============================================================*/
/* Index: ENCOMENDA_PK                                          */
/*==============================================================*/
create unique clustered index ENCOMENDA_PK on ENCOMENDA (
ID_ENCOMENDA ASC
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
create or replace table ENC_PAO 
(
   ID_PAO               numeric(4)                     not null,
   ID_ENCOMENDA         numeric(6)                     not null,
   QUANTIDADE           numeric(3)                     null,
   constraint PK_ENC_PAO primary key clustered (ID_PAO, ID_ENCOMENDA)
);

/*==============================================================*/
/* Index: ENC_PAO_PK                                            */
/*==============================================================*/
create unique clustered index ENC_PAO_PK on ENC_PAO (
ID_PAO ASC,
ID_ENCOMENDA ASC
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
create or replace table PADARIA 
(
   ID_PADARIA           numeric(4)                     not null,
   NOME                 varchar(30)                    not null,
   CIDADE               varchar(20)                    null,
   TELEFONE             varchar(12)                    null,
   CONTRIBUINTE         varchar(9)                     null,
   constraint PK_PADARIA primary key clustered (ID_PADARIA)
);

/*==============================================================*/
/* Index: PADARIA_PK                                            */
/*==============================================================*/
create unique clustered index PADARIA_PK on PADARIA (
ID_PADARIA ASC
);

/*==============================================================*/
/* Table: PAO                                                   */
/*==============================================================*/
create or replace table PAO 
(
   ID_PAO               numeric(4)                     not null,
   DESIGNACAO           varchar(30)                    not null,
   TIPO                 varchar(30)                    null,
   PESO                 numeric(5,2)                   null,
   constraint PK_PAO primary key clustered (ID_PAO)
);

/*==============================================================*/
/* Index: PAO_PK                                                */
/*==============================================================*/
create unique clustered index PAO_PK on PAO (
ID_PAO ASC
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create or replace table VEICULO 
(
   ID_VEICULO           numeric(4)                     not null,
   MATRICULA            varchar(8)                     null,
   MARCA                varchar(30)                    null,
   MODELO               varchar(30)                    null,
   TARA                 numeric(4)                     null,
   constraint PK_VEICULO primary key clustered (ID_VEICULO)
);

/*==============================================================*/
/* Index: VEICULO_PK                                            */
/*==============================================================*/
create unique clustered index VEICULO_PK on VEICULO (
ID_VEICULO ASC
);

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TEM_PADARIA foreign key (ID_PADARIA)
      references PADARIA (ID_PADARIA)
      on update restrict
      on delete restrict;

alter table ENCOMENDA
   add constraint FK_ENCOMEND_TRANSPORT_VEICULO foreign key (ID_VEICULO)
      references VEICULO (ID_VEICULO)
      on update restrict
      on delete restrict;

alter table ENC_PAO
   add constraint FK_ENC_PAO_REFERE_PAO foreign key (ID_PAO)
      references PAO (ID_PAO)
      on update restrict
      on delete restrict;

alter table ENC_PAO
   add constraint FK_ENC_PAO_TEM_1_ENCOMEND foreign key (ID_ENCOMENDA)
      references ENCOMENDA (ID_ENCOMENDA)
      on update restrict
      on delete restrict;

