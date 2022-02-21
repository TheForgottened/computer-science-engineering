/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     18/11/2020 12:26:53                          */
/*==============================================================*/


drop table VEICULO2 cascade constraints;

/*==============================================================*/
/* Table: VEICULO2                                              */
/*==============================================================*/
create table VEICULO2 
(
   IDVEICULO            NUMBER               not null,
   IDMOTOR              NUMBER,
   MATRICULA            NUMBER,
   MARCA                NUMBER,
   MODELO               NUMBER,
   CILINDRADA           NUMBER,
   COMBUSTIVEL          NUMBER,
   CAVALOS              NUMBER,
   constraint PK_VEICULO2 primary key (IDVEICULO)
);

