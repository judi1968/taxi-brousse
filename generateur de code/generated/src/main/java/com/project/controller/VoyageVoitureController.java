package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.VoyageVoitureDTO;
import com.project.model.table.VoyageVoiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class VoyageVoitureController {

    @PostMapping("/saveVoyageVoiture")
    public String saveVoyageVoiture(@ModelAttribute VoyageVoitureDTO voyage_voitureDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet VoyageVoiture à partir du DTO
            VoyageVoiture voyage_voiture = new VoyageVoiture();
            
            // Récupération de l'objet Voiture
            if (voyage_voitureDTO.getVoitureId() != null) {
                Voiture voiture = Voiture.getById(voyage_voitureDTO.getVoitureId(), connection);
                voyage_voiture.setVoiture(voiture);
            }
            
            // Récupération de l'objet Voyage
            if (voyage_voitureDTO.getVoyageId() != null) {
                Voyage voyage = Voyage.getById(voyage_voitureDTO.getVoyageId(), connection);
                voyage_voiture.setVoyage(voyage);
            }
            
            // Sauvegarder dans la base de données
            DB.save(voyage_voiture, connection);
            
            // Message de succès
            model.addAttribute("success", "VoyageVoiture enregistré avec succès !");
            model.addAttribute("voyage_voitureDTO", new VoyageVoitureDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("voyage_voitureDTO", voyage_voitureDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voyage_voiture/creation";
    }

    @GetMapping("/listeVoyageVoiture")
    public String listeVoyageVoiture(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voyage_voitures depuis la base
            List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) DB.getAll(new VoyageVoiture(), connection);

            // Ajouter la liste des voyage_voitures au modèle
            model.addAttribute("voyage_voitures", voyage_voitures);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voyage_voitures: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voyage_voiture/listeVoyageVoiture";
    }
}
