package com.project.controller;

import java.sql.Connection;

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
    
    
    @PostMapping("/saveVoiture")
    public String saveVoiture(@ModelAttribute VoitureDTO voitureDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Voiture à partir du DTO
            Voiture voiture = new Voiture();
            voiture.setNom(voitureDTO.getNom());
            voiture.setNumero(voitureDTO.getNumero());
            
            // Sauvegarder dans la base de données
            DB.save(voiture, connection);
            
            // Message de succès
            model.addAttribute("success", "Voiture enregistrée avec succès !");
            model.addAttribute("voitureDTO", new VoitureDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("voitureDTO", voitureDTO); // Garder les données saisies
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
}