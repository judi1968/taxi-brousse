CREATE SCHEMA IF NOT EXISTS "public";

CREATE  TABLE type_client ( 
	id                   serial  NOT NULL  ,
	nom                  varchar(255)    ,
	CONSTRAINT pk_type_client PRIMARY KEY ( id )
 );

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
	id_voiture           integer  NOT NULL  ,
	id_voyage            integer  NOT NULL  ,
	id                   serial  NOT NULL  ,
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
	id_type_client       integer    ,
	CONSTRAINT pk_prix_type_place_voyage PRIMARY KEY ( id ),
	CONSTRAINT fk_prix_type_place_voyage_type_place FOREIGN KEY ( id_type_place ) REFERENCES type_place( id )   ,
	CONSTRAINT fk_prix_type_place_voyage_voyage FOREIGN KEY ( id_voyage ) REFERENCES voyage( id )   ,
	CONSTRAINT fk_prix_type_place_voyage_type_client FOREIGN KEY ( id_type_client ) REFERENCES type_client( id )   
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

CREATE  TABLE achat_client ( 
	id                   serial  NOT NULL  ,
	id_type_client       integer    ,
	id_type_place_voyage integer    ,
	CONSTRAINT pk_achat_client PRIMARY KEY ( id ),
	CONSTRAINT fk_achat_client_type_client FOREIGN KEY ( id_type_client ) REFERENCES type_client( id )   ,
	CONSTRAINT fk_achat_client_type_place_voyage FOREIGN KEY ( id_type_place_voyage ) REFERENCES type_place_voyage( id )   
 );

INSERT INTO type_place( id, nom ) VALUES ( 1, 'VIP');
INSERT INTO type_place( id, nom ) VALUES ( 2, 'Premium');
INSERT INTO type_place( id, nom ) VALUES ( 3, 'Economique');
INSERT INTO voiture( id, numero, nom ) VALUES ( 1, 'V001', 'Taxi brousse Randria');
INSERT INTO voiture( id, numero, nom ) VALUES ( 2, 'V002', 'Sonatra');
INSERT INTO voiture( id, numero, nom ) VALUES ( 3, 'V003', 'Mercedes');
INSERT INTO voiture( id, numero, nom ) VALUES ( 4, 'TDI8842', 'BMWx');
INSERT INTO voiture( id, numero, nom ) VALUES ( 5, '3566TBA', 'Sprinter CDI');
INSERT INTO voiture( id, numero, nom ) VALUES ( 6, 'vot1', 'Voiture 1');
INSERT INTO voyage( id, nom, "date" ) VALUES ( 1, 'V1124 - Tana - Sava', '2026-01-12 03:57:00 PM');
INSERT INTO voyage_voiture( id_voiture, id_voyage, id ) VALUES ( 5, 1, 1);
INSERT INTO voyage_voiture( id_voiture, id_voyage, id ) VALUES ( 6, 1, 2);
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
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 80, 2, '1');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 81, 5, '2');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 82, 6, 'p1');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 83, 6, 'p2');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 84, 6, 'p3');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 85, 6, 'p4');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 86, 6, 'p5');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 87, 6, 'p6');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 88, 6, 'p7');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 89, 6, 'p8');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 90, 6, 'p9');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 91, 6, 'p10');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 92, 6, 'p11');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 93, 6, 'p12');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 94, 6, 'p13');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 95, 6, 'p14');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 96, 6, 'p15');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 97, 6, 'p16');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 98, 6, 'p17');
INSERT INTO place_voiture( id, id_voiture, numero ) VALUES ( 99, 6, 'p18');
INSERT INTO prix_type_place_voyage( id, id_type_place, id_voyage, montant, id_type_client ) VALUES ( 1, 1, 1, 180000.0, null);
INSERT INTO prix_type_place_voyage( id, id_type_place, id_voyage, montant, id_type_client ) VALUES ( 2, 2, 1, 140000.0, null);
INSERT INTO prix_type_place_voyage( id, id_type_place, id_voyage, montant, id_type_client ) VALUES ( 3, 3, 1, 90000.0, null);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 1, 1, 81, 1);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 2, 2, 82, 1);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 3, 2, 83, 1);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 4, 2, 84, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 5, 2, 85, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 6, 2, 86, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 7, 2, 87, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 8, 2, 88, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 9, 2, 89, 2);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 10, 2, 90, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 11, 2, 91, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 12, 2, 92, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 13, 2, 93, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 14, 2, 94, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 15, 2, 95, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 16, 2, 96, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 17, 2, 97, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 18, 2, 98, 3);
INSERT INTO type_place_voyage( id, id_voyage_voiture, id_place, id_type_place ) VALUES ( 19, 2, 99, 3);
