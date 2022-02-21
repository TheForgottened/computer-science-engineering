/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     18/11/2020 12:46:27                          */
/*==============================================================*/


alter table POSSUI
   drop constraint FK_POSSUI_REFERENCE_MOTOR;

alter table POSSUI
   drop constraint FK_POSSUI_REFERENCE_VEICULO;

drop table MOTOR cascade constraints;

drop table POSSUI cascade constraints;

drop table VEICULO cascade constraints;

/*==============================================================*/
/* Table: MOTOR                                                 */
/*==============================================================*/
create table MOTOR 
(
   IDMOTOR              NUMBER               not null,
   CILINDRADA           NUMBER,
   COMBUSTIVEL          NUMBER,
   CAVALOS              NUMBER,
   constraint PK_MOTOR primary key (IDMOTOR)
);

/*==============================================================*/
/* Table: POSSUI                                                */
/*==============================================================*/
create table POSSUI 
(
   IDMOTOR              NUMBER               not null,
   IDVEICULO            NUMBER               not null,
   constraint PK_POSSUI primary key (IDMOTOR, IDVEICULO)
);

/*==============================================================*/
/* Table: VEICULO                                               */
/*==============================================================*/
create table VEICULO 
(
   IDVEICULO            NUMBER               not null,
   MATRICULA            NUMBER               not null,
   MARCA                NUMBER,
   MODELO               NUMBER,
   constraint PK_VEICULO primary key (IDVEICULO)
);

alter table POSSUI
   add constraint FK_POSSUI_REFERENCE_MOTOR foreign key (IDMOTOR)
      references MOTOR (IDMOTOR);

alter table POSSUI
   add constraint FK_POSSUI_REFERENCE_VEICULO foreign key (IDVEICULO)
      references VEICULO (IDVEICULO);

