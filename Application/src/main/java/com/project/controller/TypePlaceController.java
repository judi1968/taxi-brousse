package com.project.controller;

import java.sql.Connection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.TypePlaceDTO;
import com.project.model.table.TypePlace;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class TypePlaceController {
    
    @PostMapping("/saveTypePlace")
    public String saveTypePlace(@ModelAttribute TypePlaceDTO typePlaceDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Créer l'objet TypePlace à partir du DTO
            TypePlace typePlace = new TypePlace();
            typePlace.setNom(typePlaceDTO.getNom());
            
            // Sauvegarder
            DB.save(typePlace, connection);
            
            // Message de succès
            model.addAttribute("success", "Type de place enregistré avec succès !");
            model.addAttribute("typePlaceDTO", new TypePlaceDTO()); // Réinitialiser
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            model.addAttribute("typePlaceDTO", typePlaceDTO); // Garder les données
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/typePlaceVoyage";
    } 
}