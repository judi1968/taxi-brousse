package com.project.controller;

import java.sql.Connection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.VoyageDTO;
import com.project.model.table.Voyage;
import com.project.pja.databases.MyConnection;
import com.project.pja.databases.generalisation.DB;

@Controller
public class VoyageController {

    @PostMapping("/saveVoyage")
    public String saveVoyage(@ModelAttribute VoyageDTO voyageDTO, Model model) {
        
        Connection connection = null;
        try {
            // Établir la connexion
            connection = MyConnection.connect();
            
            // Créer l'objet Voyage à partir du DTO
            Voyage voyage = new Voyage();
            voyage.setNom(voyageDTO.getNom());
            voyage.setDate(voyageDTO.getDate());
            
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
        
        return "pages/voyage/creation";
    }

    @GetMapping("/listeVoyage")
    public String listeVoyage(Model model) {
        Connection connection = null;
        try {
            connection = MyConnection.connect();

            // Récupérer la liste des voyages depuis la base
            List<Voyage> voyages = (List<Voyage>) DB.getAll(new Voyage(), connection);

            // Ajouter la liste des voyages au modèle
            model.addAttribute("voyages", voyages);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement des voyages: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "pages/voyage/listeVoyage";
    }
}
