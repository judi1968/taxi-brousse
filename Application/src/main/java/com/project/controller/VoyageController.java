package com.project.controller;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.DB;
import com.project.dto.VoyageDTO;
import com.project.model.table.Voyage;

@Controller
public class VoyageController {
    

    @PostMapping("/saveVoyage")
    public String saveVoyage(@ModelAttribute VoyageDTO voyageDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Créer l'objet Voyage à partir du DTO
            Voyage voyage = new Voyage();
            voyage.setNom(voyageDTO.getNom());
            
            // Convertir la date string en Date
            if (voyageDTO.getDateStr() != null && !voyageDTO.getDateStr().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date date = sdf.parse(voyageDTO.getDateStr());
                voyage.setDate(date);
            }
            
            // Sauvegarder dans la base de données
            DB.save(voyage, connection);
            
            // Message de succès
            model.addAttribute("success", "Voyage enregistré avec succès !");
            model.addAttribute("voyageDTO", new VoyageDTO()); // Réinitialiser le formulaire
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("voyageDTO", voyageDTO); // Garder les données saisies
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return "pages/voiture/creationVoyage";
    }
    
   
}