CREATE SCHEMA IF NOT EXISTS "public";

CREATE  TABLE type_place ( 
	id                   serial  NOT NULL  ,
	nom                  varchar(255)    ,
	CONSTRAINT pk_type_place PRIMARY KEY ( id )
 );

CREATE  TABLE voiture ( 
	id                   serial  NOT NULL  ,
	numero               varchar(255)    ,
	nom                  varchar(255)    ,
	CONSTRAINT pk_voiture PRIMARY KEY ( id )
 );

CREATE  TABLE voyage ( 
	id                   serial  NOT NULL  ,
	nom                  varchar(255)    ,
	"date"               timestamp    ,
	CONSTRAINT pk_voyage PRIMARY KEY ( id )
 );

CREATE  TABLE voyage_voiture ( 
	id                   serial  NOT NULL  ,
	id_voiture           integer  NOT NULL  ,
	id_voyage            integer  NOT NULL  ,
	CONSTRAINT pk_voyage_voiture PRIMARY KEY ( id ),
	CONSTRAINT fk_voyage_voiture_voiture FOREIGN KEY ( id_voiture ) REFERENCES voiture( id )   ,
	CONSTRAINT fk_voyage_voiture_voyage FOREIGN KEY ( id_voyage ) REFERENCES voyage( id )   
 );

CREATE  TABLE place_voiture ( 
	id                   serial  NOT NULL  ,
	id_voiture           integer  NOT NULL  ,
	numero               varchar(255)  NOT NULL  ,
	CONSTRAINT pk_place_voiture PRIMARY KEY ( id ),
	CONSTRAINT fk_place_voiture_voiture FOREIGN KEY ( id_voiture ) REFERENCES voiture( id )   
 );

CREATE  TABLE prix_type_place_voyage ( 
	id                   serial  NOT NULL  ,
	id_type_place        integer    ,
	id_voyage            integer    ,
	montant              double precision    ,
	CONSTRAINT pk_prix_type_place_voyage PRIMARY KEY ( id ),
	CONSTRAINT fk_prix_type_place_voyage_type_place FOREIGN KEY ( id_type_place ) REFERENCES type_place( id )   ,
	CONSTRAINT fk_prix_type_place_voyage_voyage FOREIGN KEY ( id_voyage ) REFERENCES voyage( id )   
 );

CREATE  TABLE type_place_voyage ( 
	id                   serial  NOT NULL  ,
	id_voyage_voiture    integer  NOT NULL  ,
	id_place             integer    ,
	id_type_place        integer    ,
	CONSTRAINT pk_type_place_voyage PRIMARY KEY ( id ),
	CONSTRAINT fk_type_place_voyage_voyage_voiture FOREIGN KEY ( id_voyage_voiture ) REFERENCES voyage_voiture( id )   ,
	CONSTRAINT fk_type_place_voyage_place_voiture FOREIGN KEY ( id_place ) REFERENCES place_voiture( id )   ,
	CONSTRAINT fk_type_place_voyage_type_place FOREIGN KEY ( id_type_place ) REFERENCES type_place( id )   
 );

INSERT INTO voiture( id, numero, nom ) VALUES ( 1, 'V001', 'Taxi brousse Randria');
INSERT INTO voiture( id, numero, nom ) VALUES ( 2, 'V002', 'Sonatra');
INSERT INTO voiture( id, numero, nom ) VALUES ( 3, 'V003', 'Mercedes');
INSERT INTO voiture( id, numero, nom ) VALUES ( 4, 'TDI8842', 'BMWx');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 2, 1, '2');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 3, 1, '3');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 4, 1, '4');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 5, 1, '5');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 6, 1, '6');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 7, 1, '7');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 8, 1, '8');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 9, 1, '9');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 10, 1, '10');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 11, 1, '11');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 12, 1, '12');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 13, 1, '13');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 14, 1, '14');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 15, 1, '15');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 16, 1, '16');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 17, 1, '17');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 18, 1, '18');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 19, 1, '19');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 20, 1, '20');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 21, 1, '21');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 22, 1, '22');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 23, 1, '23');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 24, 1, '24');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 25, 1, '25');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 26, 1, '26');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 27, 1, '27');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 28, 2, '1');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 29, 2, '2');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 30, 2, '3');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 31, 2, '4');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 32, 2, '5');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 33, 2, '6');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 34, 2, '7');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 35, 2, '8');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 36, 2, '9');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 37, 2, '10');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 38, 2, '11');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 39, 2, '12');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 40, 2, '13');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 41, 2, '14');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 42, 2, '15');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 43, 2, '16');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 44, 2, '17');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 45, 2, '18');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 46, 2, '19');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 47, 2, '20');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 48, 2, '21');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 49, 2, '22');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 50, 2, '23');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 51, 2, '24');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 52, 2, '25');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 53, 2, '26');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 54, 2, '27');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 55, 2, '28');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 56, 2, '29');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 57, 2, '30');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 58, 2, '31');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 59, 2, '32');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 60, 3, '1');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 61, 3, '2');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 62, 3, '3');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 63, 3, '4');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 64, 3, '5');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 65, 3, '6');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 66, 3, '7');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 67, 3, '8');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 68, 3, '9');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 69, 3, '10');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 70, 3, '11');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 71, 3, '12');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 72, 3, '13');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 73, 3, '14');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 74, 3, '15');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 75, 3, '16');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 76, 3, '17');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 77, 3, '18');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 78, 3, '19');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 79, 3, '20');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 1, 2, '1');
