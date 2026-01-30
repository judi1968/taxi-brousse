package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.VoitureDTO;
import com.project.model.table.Voiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class VoitureController {

    @GetMapping("/creationVoiture")
    public String goToCreate(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();


            // Aucune donnée à charger

            // Initialiser un DTO vide pour le formulaire
            model.addAttribute("voitureDTO", new VoitureDTO());

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
        return "pages/voiture/creation";
    }

    @PostMapping("/saveVoiture")
    public String saveVoiture(@ModelAttribute VoitureDTO voitureDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Voiture à partir du DTO
            Voiture voiture = new Voiture();
            voiture.setNumero(voitureDTO.getNumero());
            voiture.setNom(voitureDTO.getNom());
            
            // Sauvegarder dans la base de données
            DB.save(voiture, connection);
            
            // Message de succès
            model.addAttribute("success", "Voiture enregistré avec succès !");
            model.addAttribute("voitureDTO", new VoitureDTO()); // Réinitialiser le formulaire
            
            // Recharger les listes pour les foreign keys
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("voitureDTO", voitureDTO); // Garder les données saisies
            
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
        
        return "pages/voiture/creation";
    }

    @GetMapping("/listeVoiture")
    public String listeVoiture(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voitures depuis la base
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);

            // Ajouter la liste des voitures au modèle
            model.addAttribute("voitures", voitures);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voitures: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voiture/listeVoiture";
    }
}
