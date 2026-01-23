-- Insertion des deux gares
INSERT INTO gare (nom) VALUES ('Toamasina');
INSERT INTO gare (nom) VALUES ('Mahajanga');
INSERT INTO gare (nom) VALUES ('Antananarivo');

-- Insertion du véhicule Soatrans
INSERT INTO voiture (numero, nom) VALUES ('1244TBK', 'Soatrans');

-- Création des voyages de Tananarivo vers Mahajanga
INSERT INTO voyage (nom, "date", id_gare_depart, id_gare_arrive) VALUES 
('Tana-Mahajanga', '2026-01-20 10:00:00', 3, 2),
('Tana-Mahajanga', '2026-01-21 10:00:00', 3, 2),
('Tana-Mahajanga', '2026-01-21 15:00:00', 3, 2);

-- Création des associations voiture-voyage
INSERT INTO voyage_voiture (id_voiture, id_voyage) VALUES 
(1, 1),
(1, 2),
(1, 3);

-- Création de 120 places pour le véhicule Soatrans (id_voiture = 1)
INSERT INTO place_voiture (id_voiture, numero)
SELECT 1, generate_series(1, 120)::varchar(255);

-- Création du type de place "économique"
INSERT INTO type_place (nom) VALUES ('Economique');

-- Création du type client "adulte"
INSERT INTO type_client (nom) VALUES ('adulte');



-- Pour le voyage_voiture id 1 : 40 places économiques
INSERT INTO type_place_voyage (id_voyage_voiture, id_place, id_type_place)
SELECT 
    1, -- id_voyage_voiture
    p.id, -- id_place
    1 -- id_type_place (économique)
FROM place_voiture p
WHERE p.id_voiture = 1 -- Soatrans
ORDER BY p.id
LIMIT 40;

-- Pour le voyage_voiture id 2 : 30 places économiques
INSERT INTO type_place_voyage (id_voyage_voiture, id_place, id_type_place)
SELECT 
    2, -- id_voyage_voiture
    p.id, -- id_place
    1 -- id_type_place (économique)
FROM place_voiture p
WHERE p.id_voiture = 1 -- Soatrans
ORDER BY p.id
OFFSET 40
LIMIT 30;

-- Pour le voyage_voiture id 3 : 50 places économiques
INSERT INTO type_place_voyage (id_voyage_voiture, id_place, id_type_place)
SELECT 
    3, -- id_voyage_voiture
    p.id, -- id_place
    1 -- id_type_place (économique)
FROM place_voiture p
WHERE p.id_voiture = 1 -- Soatrans
ORDER BY p.id
OFFSET 70 -- 40 + 30 déjà utilisées
LIMIT 50;

-- Insertion du prix pour le billet économique adulte pour les 3 voyages
INSERT INTO prix_type_place_voyage (id_type_place, id_voyage, montant, id_type_client) VALUES 
(1, 1, 50000, 1), -- Voyage 1, type économique, adulte
(1, 2, 50000, 1), -- Voyage 2, type économique, adulte  
(1, 3, 50000, 1); -- Voyage 3, type économique, adulte


-- Insertion des achats pour tous les type_place_voyage avec type_client adulte
INSERT INTO achat_client (id_type_client, id_type_place_voyage)
SELECT 
    1, -- id_type_client (adulte)
    id -- id_type_place_voyage
FROM type_place_voyage;

-- Insertion des 3 sociétés
INSERT INTO societe (nom) VALUES 
('Vaniala'),
('Lewis'), 
('Socobis'),
('Jejoo');


-- Insertion du prix d'une pub en décembre 2020
INSERT INTO mouvement_prix_pub (montant, date_mouvement) VALUES 
(100000, '2020-12-01 00:00:00');