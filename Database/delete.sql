-- Script de nettoyage de toutes les tables avec CASCADE
TRUNCATE TABLE 
    achat_client,
    type_place_voyage, 
    prix_type_place_voyage, 
    place_voiture, 
    payement_diffusion, 
    parametre_caclul_prix_type, 
    diffusion_societe, 
    voyage_voiture, 
    voyage, 
    voiture, 
    type_place, 
    type_client, 
    societe, 
    mouvement_prix_pub, 
    gare 
CASCADE;

-- Réinitialisation des séquences si nécessaire
ALTER SEQUENCE achat_client_id_seq RESTART WITH 1;
ALTER SEQUENCE type_place_voyage_id_seq RESTART WITH 1;
ALTER SEQUENCE prix_type_place_voyage_id_seq RESTART WITH 1;
ALTER SEQUENCE place_voiture_id_seq RESTART WITH 1;
ALTER SEQUENCE payement_diffusion_id_seq RESTART WITH 1;
ALTER SEQUENCE parametre_caclul_prix_type_id_seq RESTART WITH 1;
ALTER SEQUENCE diffusion_societe_id_seq RESTART WITH 1;
ALTER SEQUENCE voyage_voiture_id_seq RESTART WITH 1;
ALTER SEQUENCE voyage_id_seq RESTART WITH 1;
ALTER SEQUENCE voiture_id_seq RESTART WITH 1;
ALTER SEQUENCE type_place_id_seq RESTART WITH 1;
ALTER SEQUENCE type_client_id_seq RESTART WITH 1;
ALTER SEQUENCE societe_id_seq RESTART WITH 1;
ALTER SEQUENCE mouvement_prix_pub_id_seq RESTART WITH 1;
ALTER SEQUENCE gare_id_seq RESTART WITH 1;