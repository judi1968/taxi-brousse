CREATE SCHEMA IF NOT EXISTS "public";

CREATE  TABLE gare ( 
	id                   serial  NOT NULL  ,
	nom                  varchar(255)  NOT NULL  ,
	CONSTRAINT pk_gare PRIMARY KEY ( id )
 );

CREATE  TABLE mouvement_prix_pub ( 
	id                   serial  NOT NULL  ,
	montant              double precision    ,
	date_mouvement       timestamp    ,
	CONSTRAINT pk_mouvement_prix_pub PRIMARY KEY ( id )
 );

CREATE  TABLE societe ( 
	id                   serial  NOT NULL  ,
	nom                  varchar(255)    ,
	CONSTRAINT pk_societe PRIMARY KEY ( id )
 );

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
	id_gare_depart       integer    ,
	id_gare_arrive       integer    ,
	CONSTRAINT pk_voyage PRIMARY KEY ( id ),
	CONSTRAINT fk_voyage_gare FOREIGN KEY ( id_gare_depart ) REFERENCES gare( id )   ,
	CONSTRAINT fk_voyage_gare_0 FOREIGN KEY ( id_gare_arrive ) REFERENCES gare( id )   
 );

CREATE  TABLE voyage_voiture ( 
	id_voiture           integer  NOT NULL  ,
	id_voyage            integer  NOT NULL  ,
	id                   serial  NOT NULL  ,
	CONSTRAINT pk_voyage_voiture PRIMARY KEY ( id ),
	CONSTRAINT fk_voyage_voiture_voiture FOREIGN KEY ( id_voiture ) REFERENCES voiture( id )   ,
	CONSTRAINT fk_voyage_voiture_voyage FOREIGN KEY ( id_voyage ) REFERENCES voyage( id )   
 );

CREATE  TABLE diffusion_societe ( 
	id                   serial  NOT NULL  ,
	id_societe           integer    ,
	date_diffusion       timestamp    ,
	nombre_pub           double precision    ,
	id_voyage_voiture    integer    ,
	CONSTRAINT pk_diffusion_societe PRIMARY KEY ( id ),
	CONSTRAINT fk_diffusion_societe_societe FOREIGN KEY ( id_societe ) REFERENCES societe( id )   ,
	CONSTRAINT fk_diffusion_societe_voyage_voiture FOREIGN KEY ( id_voyage_voiture ) REFERENCES voyage_voiture( id )   
 );

CREATE  TABLE parametre_caclul_prix_type ( 
	id                   serial  NOT NULL  ,
	id_reference_type_client integer    ,
	id_object_type_client integer    ,
	pourcentage          double precision    ,
	signe                integer    ,
	CONSTRAINT pk_parametre_caclul_prix_type PRIMARY KEY ( id ),
	CONSTRAINT fk_parametre_caclul_prix_type_type_client FOREIGN KEY ( id_reference_type_client ) REFERENCES type_client( id )   ,
	CONSTRAINT fk_parametre_caclul_prix_type_type_client_0 FOREIGN KEY ( id_object_type_client ) REFERENCES type_client( id )   
 );

CREATE  TABLE payement_diffusion ( 
	id                   serial  NOT NULL  ,
	id_societe_diffusion integer    ,
	montant              double precision    ,
	date_payement        timestamp    ,
	CONSTRAINT pk_payement_diffusion PRIMARY KEY ( id ),
	CONSTRAINT fk_payement_diffusion_diffusion_societe FOREIGN KEY ( id_societe_diffusion ) REFERENCES diffusion_societe( id )   
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

CREATE OR REPLACE VIEW v_diffusion_ca AS SELECT ds.id,
    ds.id_societe,
    ds.date_diffusion,
    ds.nombre_pub,
    ( SELECT COALESCE(mpb.montant, (0)::double precision) AS "coalesce"
           FROM mouvement_prix_pub mpb
          WHERE (mpb.date_mouvement <= ds.date_diffusion)
          ORDER BY mpb.date_mouvement DESC
         LIMIT 1) AS pu,
    (ds.nombre_pub * ( SELECT COALESCE(mpb.montant, (0)::double precision) AS "coalesce"
           FROM mouvement_prix_pub mpb
          WHERE (mpb.date_mouvement <= ds.date_diffusion)
          ORDER BY mpb.date_mouvement DESC
         LIMIT 1)) AS montant_totale,
    ( SELECT COALESCE(sum(pd.montant), (0)::double precision) AS "coalesce"
           FROM payement_diffusion pd
          WHERE (pd.id_societe_diffusion = ds.id)) AS payer,
    ((ds.nombre_pub * ( SELECT COALESCE(mpb.montant, (0)::double precision) AS "coalesce"
           FROM mouvement_prix_pub mpb
          WHERE (mpb.date_mouvement <= ds.date_diffusion)
          ORDER BY mpb.date_mouvement DESC
         LIMIT 1)) - ( SELECT COALESCE(sum(pd.montant), (0)::double precision) AS "coalesce"
           FROM payement_diffusion pd
          WHERE (pd.id_societe_diffusion = ds.id))) AS reste
   FROM diffusion_societe ds;

