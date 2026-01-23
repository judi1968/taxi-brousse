CREATE OR REPLACE VIEW v_diffusion_ca AS
SELECT 
    * ,
    (SELECT 
        COALESCE(montant, 0)
    FROM 
        mouvement_prix_pub mpb
    WHERE 
        mpb.date_mouvement <= ds.date_diffusion
    ORDER BY mpb.date_mouvement DESC
    LIMIT 1
    ) AS pu,
    (nombre_pub * (SELECT 
        COALESCE(montant, 0)
    FROM 
        mouvement_prix_pub mpb
    WHERE 
        mpb.date_mouvement <= ds.date_diffusion
    ORDER BY mpb.date_mouvement DESC
    LIMIT 1
    )) AS montant_totale,
    (SELECT 
        COALESCE(SUM(montant), 0)
    FROM 
        payement_diffusion pd
    WHERE 
        pd.id_societe_diffusion = ds.id
    ) AS payer,
    ((nombre_pub * (SELECT 
        COALESCE(montant, 0)
    FROM 
        mouvement_prix_pub mpb
    WHERE 
        mpb.date_mouvement <= ds.date_diffusion
    ORDER BY mpb.date_mouvement DESC
    LIMIT 1
    )) - (SELECT 
        COALESCE(SUM(montant), 0)
    FROM 
        payement_diffusion pd
    WHERE 
        pd.id_societe_diffusion = ds.id
    )) as reste
FROM
    diffusion_societe ds;
