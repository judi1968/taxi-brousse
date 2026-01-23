package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.PlaceVoitureDTO;
import com.project.model.table.PlaceVoiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class PlaceVoitureController {

    @PostMapping("/savePlaceVoiture")
    public String savePlaceVoiture(@ModelAttribute PlaceVoitureDTO place_voitureDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet PlaceVoiture à partir du DTO
            PlaceVoiture place_voiture = new PlaceVoiture();
            
            // Récupération de l'objet Voiture
            if (place_voitureDTO.getVoitureId() != null) {
                Voiture voiture = Voiture.getById(place_voitureDTO.getVoitureId(), connection);
                place_voiture.setVoiture(voiture);
            }
            place_voiture.setNumero(place_voitureDTO.getNumero());
            
            // Sauvegarder dans la base de données
            DB.save(place_voiture, connection);
            
            // Message de succès
            model.addAttribute("success", "PlaceVoiture enregistré avec succès !");
            model.addAttribute("place_voitureDTO", new PlaceVoitureDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("place_voitureDTO", place_voitureDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/place_voiture/creation";
    }

    @GetMapping("/listePlaceVoiture")
    public String listePlaceVoiture(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des place_voitures depuis la base
            List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) DB.getAll(new PlaceVoiture(), connection);

            // Ajouter la liste des place_voitures au modèle
            model.addAttribute("place_voitures", place_voitures);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des place_voitures: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/place_voiture/listePlaceVoiture";
    }
}
