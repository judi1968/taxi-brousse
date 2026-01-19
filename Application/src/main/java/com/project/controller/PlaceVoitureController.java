package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.PlaceVoitureDTO;
import com.project.model.table.Voiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;
import com.project.model.table.PlaceVoiture;

@Controller
public class PlaceVoitureController {
    
    @PostMapping("/savePlaceVoiture")
    public String savePlaceVoiture(@ModelAttribute PlaceVoitureDTO placeDTO, 
                                   @RequestParam("idVoiture") Integer idVoiture,
                                   Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Récupérer la voiture sélectionnée
            Voiture voiture = (Voiture) DB.getById(new Voiture(), idVoiture, connection);
            
            // Créer l'objet PlaceVoiture
            PlaceVoiture placeVoiture = new PlaceVoiture();
            placeVoiture.setNumero(placeDTO.getNumero());
            placeVoiture.setVoiture(voiture);
            
            // Sauvegarder
            DB.save(placeVoiture, connection);
            
            // Recharger la liste des voitures pour le formulaire
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
            model.addAttribute("voitures", voitures);
            model.addAttribute("success", "Place créée avec succès !");
            model.addAttribute("placeDTO", new PlaceVoitureDTO()); // Réinitialiser
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            
            // Recharger les voitures même en cas d'erreur
            try {
                if (connection != null) {
                    List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
                    model.addAttribute("voitures", voitures);
                }
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
        
        return "pages/voiture/creationPlace";
    }
}