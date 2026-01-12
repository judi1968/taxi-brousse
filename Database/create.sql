CREATE SCHEMA IF NOT EXISTS "public";

CREATE  TABLE voiture ( 
	id                   serial  NOT NULL  ,
	numero               varchar(255)    ,
	nom                  varchar(255)    ,
	CONSTRAINT pk_voiture PRIMARY KEY ( id )
 );

CREATE  TABLE place_voiture ( 
	id                   serial  NOT NULL  ,
	id_voiture           integer  NOT NULL  ,
	numero               varchar(255)  NOT NULL  ,
	CONSTRAINT pk_place_voiture PRIMARY KEY ( id ),
	CONSTRAINT fk_place_voiture_voiture FOREIGN KEY ( id_voiture ) REFERENCES voiture( id )   
 );

