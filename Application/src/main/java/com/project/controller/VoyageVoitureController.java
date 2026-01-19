package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.VoyageVoitureDTO;
import com.project.model.table.Voiture;
import com.project.model.table.Voyage;
import com.project.model.table.VoyageVoiture;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class VoyageVoitureController {
    
    @PostMapping("/saveVoyageVoiture")
    public String saveVoyageVoiture(@ModelAttribute VoyageVoitureDTO voyageVoitureDTO, Model model) {
        
        Connection connection = null;
        try {
            connection = MyConnection.connect();
            
            // Récupérer la voiture sélectionnée
            Voiture voiture = (Voiture) DB.getById(new Voiture(), voyageVoitureDTO.getIdVoiture(), connection);
            
            // Récupérer le voyage sélectionné
            Voyage voyage = (Voyage) DB.getById(new Voyage(), voyageVoitureDTO.getIdVoyage(), connection);
            
            // Créer l'objet VoyageVoiture
            VoyageVoiture voyageVoiture = new VoyageVoiture();
            voyageVoiture.setVoiture(voiture);
            voyageVoiture.setVoyage(voyage);
            
            // Sauvegarder
            DB.save(voyageVoiture, connection);
            
            // Recharger les listes pour le formulaire
            List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
            
            model.addAttribute("voitures", voitures);
            model.addAttribute("voyages", voyages);
            model.addAttribute("success", "Association voiture-voyage enregistrée avec succès !");
            model.addAttribute("voyageVoitureDTO", new VoyageVoitureDTO());
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            
            // Recharger les listes même en cas d'erreur
            try {
                if (connection != null) {
                    List<Voiture> voitures = (List<Voiture>) DB.getAll(new Voiture(), connection);
                    List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);
                    model.addAttribute("voitures", voitures);
                    model.addAttribute("voyages", voyages);
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
        
        return "pages/voiture/voitureVoyage";
    }
}