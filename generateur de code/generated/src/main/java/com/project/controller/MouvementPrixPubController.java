package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.MouvementPrixPubDTO;
import com.project.model.table.MouvementPrixPub;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class MouvementPrixPubController {

    @GetMapping("/creationMouvementPrixPub")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Aucune donnée à charger

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("mouvement_prix_pubDTO", new MouvementPrixPubDTO());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des données: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/mouvement_prix_pub/creation";
    }

    @PostMapping("/saveMouvementPrixPub")
    public String saveMouvementPrixPub(@ModelAttribute MouvementPrixPubDTO mouvement_prix_pubDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet MouvementPrixPub à partir du DTO
            MouvementPrixPub mouvement_prix_pub = new MouvementPrixPub();
            mouvement_prix_pub.setMontant(mouvement_prix_pubDTO.getMontant());
            mouvement_prix_pub.setDateMouvement(mouvement_prix_pubDTO.getDateMouvement());
            
            // Sauvegarder dans la base de données
            DB.save(mouvement_prix_pub, connection);
            
            // Message de succès
            model.addAttribute("success", "MouvementPrixPub enregistré avec succès !");
            model.addAttribute("mouvement_prix_pubDTO", new MouvementPrixPubDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("mouvement_prix_pubDTO", mouvement_prix_pubDTO); // Garder les données saisies
            
            // Recharger les listes en cas d'erreur
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/mouvement_prix_pub/creation";
    }

    @GetMapping("/listeMouvementPrixPub")
    public String listeMouvementPrixPub(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des mouvement_prix_pubs depuis la base
            List<MouvementPrixPub> mouvement_prix_pubs = (List<MouvementPrixPub>) DB.getAll(new MouvementPrixPub(), connection);

            // Ajouter la liste des mouvement_prix_pubs au modèle
            model.addAttribute("mouvement_prix_pubs", mouvement_prix_pubs);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des mouvement_prix_pubs: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/mouvement_prix_pub/listeMouvementPrixPub";
    }
}
